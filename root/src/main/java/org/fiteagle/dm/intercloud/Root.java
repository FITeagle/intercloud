package org.fiteagle.dm.intercloud;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class Root {
    private String server;
    private int port;
    
    private ConnectionConfiguration config;
    private XMPPConnection connectionAdd;
    private XMPPConnection connectionQuery;

    private ChatManager chatManagerAdd;    
    private ChatManager chatManagerQuery;

    private MessageListener messageListenerAdd;
    private MessageListener messageListenerQuery;
        
    private DatasetAccessor sparqlEndPointAdd;
    private Dataset sparqlEndPointQuery;
    private static String rootAddPointName = "rootAdd";
    private static String rootQueryPointName = "rootQuery";
    
    private ArrayList<String> messageList;
    
    public Root(String server, int port) {
        this.server = server;
        this.port = port;
    }
    
    public ArrayList<String> getMessage() {
    	return messageList;
    }
        
    public void init(String serviceURI) {
        try {      	
	        config = new ConnectionConfiguration(server, port);
	
	        connectionAdd = new XMPPConnection(config);
			connectionAdd.connect();
	        
	        messageListenerAdd = new AddModelListener();
	        chatManagerAdd = connectionAdd.getChatManager();
	        chatManagerAdd.addChatListener(new AddChatListener());
	        
	
	        connectionQuery = new XMPPConnection(config);
	        connectionQuery.connect();
	
	        messageListenerQuery = new QueryModelListener();
	        chatManagerQuery = connectionQuery.getChatManager();
	        chatManagerQuery.addChatListener(new QueryChatListener());
	        
	        performLogin(connectionAdd, rootAddPointName, rootAddPointName);
	        performLogin(connectionQuery, rootQueryPointName, rootQueryPointName);
	        
	        messageList = new ArrayList<String>();
	        
        } catch (XMPPException e) {
        	e.printStackTrace();
        }
        connectToSparql(serviceURI);
    }
    
    public boolean performLogin(XMPPConnection connection, String username, String password)  {
        if (connection!=null && connection.isConnected()) {
            if(!connection.isAuthenticated()){
          	  try {
    				connection.getAccountManager().createAccount(username, password);
    				}catch (XMPPException e) {
    					e.printStackTrace();
    					}
    				}
  			try{
  					connection.login(username, password);
  				}catch(XMPPException e2) {
  				e2.printStackTrace();
  				return false; 
            }
  		return true;
          }
          else return false;
    }
    
    public void connectToSparql(String serviceURI) {
    	sparqlEndPointAdd = DatasetAccessorFactory.createHTTP(serviceURI);
    	//Model model = ModelFactory.createDefaultModel();
    	//sparqlEndPoint.putModel(model);
    	sparqlEndPointQuery = DatasetFactory.create(serviceURI+"?default");
    }
    
    public void destroy() {
        if (connectionAdd!=null && connectionAdd.isConnected()) {
            connectionAdd.disconnect();
        }
        if (connectionQuery!=null && connectionQuery.isConnected()) {
            connectionQuery.disconnect();
        }
    }
    
    public String RDFToString(Model model) {
    	StringWriter out = new StringWriter();
    	model.write(out, "JSON-LD");
    	String modelXML = out.toString();
    	return modelXML;
    }
    
    public void sendMessage(MessageListener messageListener, String message, String buddyJID) throws XMPPException {
        Chat chat = chatManagerAdd.createChat(buddyJID, messageListener);
        chat.sendMessage(message);
    }
    
	public Model stringToRDF(String modelInString) {
		InputStream in = new ByteArrayInputStream(modelInString.getBytes(StandardCharsets.UTF_8));
		Model model = ModelFactory.createDefaultModel();
		model.read(in, null, "JSON-LD");
		return model;
	}
	
	public void addRDFToSparql(Model model) {
		sparqlEndPointAdd.add(model);
	}
    
    public String querySparql(String queryMessage) {
        Query query = QueryFactory.create(queryMessage);
        QueryExecution qExe = QueryExecutionFactory.create(query, sparqlEndPointQuery);
        String resultMessage = new String();
        try {
        	ResultSet results = qExe.execSelect();
        	resultMessage = ResultSetFormatter.asText(results);
        } finally {
        	qExe.close();
        }
    	return resultMessage;
    }
        
    class AddModelListener implements MessageListener {
    	
    	public void processMessage(Chat chat, Message message) {
            String body = message.getBody();
            messageList.add(body);
            addRDFToSparql(stringToRDF(body));
        }
        
    }

    class QueryModelListener implements MessageListener {

    	
    	public void processMessage(Chat chat, Message message) {
            String body = message.getBody();
            messageList.add(body);
            try {
            	String resultMessage = querySparql(body);
            	chat.sendMessage(resultMessage);
            } catch (XMPPException e){
            	e.printStackTrace();
            }
        }
        
    }

    class AddChatListener implements ChatManagerListener {
    	
    	public void chatCreated(Chat chat, boolean createdLocally) {
    		if (!createdLocally) {
    			chat.addMessageListener(messageListenerAdd);
    		}
    	}
    }

    class QueryChatListener implements ChatManagerListener {
    	
    	public void chatCreated(Chat chat, boolean createdLocally) {
    		if (!createdLocally) {
    			chat.addMessageListener(messageListenerQuery);
    		}
    	}
    }

    public static int main() {
    	return 0;
    }
}
