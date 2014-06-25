package org.fiteagle.dm.intercloud;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
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
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.DatasetAccessor;

public class Root {
    private String server;
    private int port;
    
    private ConnectionConfiguration config;
    private XMPPConnection connection;

    private ChatManager chatManager;
    private MessageListener messageListener;
    
    private ArrayList<Model> messageList;
    
    private DatasetAccessor sparqlEndPoint;
    private Model datasetModel;
    
    public Root(String server, int port) {
        this.server = server;
        this.port = port;
    }
    
    public ArrayList<Model> getMessage() {
    	return messageList;
    }
    
    public void init() throws XMPPException {
                
        config = new ConnectionConfiguration(server, port);
        connection = new XMPPConnection(config);
		connection.connect();
        
        chatManager = connection.getChatManager();
        chatManager.addChatListener(new MyChatListener());
        messageListener = new MyMessageListener();

        messageList = new ArrayList<Model>();
        
    }
    
    public boolean performLogin(String username, String password)  {
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
    	sparqlEndPoint = DatasetAccessorFactory.createHTTP(serviceURI);
    	datasetModel = sparqlEndPoint.getModel();
    }
    
    public void destroy() {
        if (connection!=null && connection.isConnected()) {
            connection.disconnect();
        }
    }
    
    public String RDFToString(Model model) {
    	StringWriter out = new StringWriter();
    	model.write(out, "JSON-LD");
    	String modelXML = out.toString();
    	return modelXML;
    }
    
    public void sendMessage(Model model, String buddyJID) throws XMPPException {
//        System.out.println(String.format("Sending message '%1$s' to user %2$s", message, buddyJID));
        Chat chat = chatManager.createChat(buddyJID, messageListener);
        String message = RDFToString(model);
        chat.sendMessage(message);
    }
        
    class MyMessageListener implements MessageListener {

    	public Model stringToRDF(String modelInString) {
    		InputStream in = new ByteArrayInputStream(modelInString.getBytes(StandardCharsets.UTF_8));
    		Model model = ModelFactory.createDefaultModel();
    		model.read(in, null, "JSON-LD");
    		return model;
    	}
    	
    	public void processMessage(Chat chat, Message message) {
            String body = message.getBody();
            Model model = stringToRDF(body);
            sparqlEndPoint.add(model);
            messageList.add(model);
        }
        
    }
    class MyChatListener implements ChatManagerListener {
    	public void chatCreated(Chat chat, boolean createdLocally) {
    		if (!createdLocally) {
    			chat.addMessageListener(messageListener);
    		}
    	}
    }
    public static int main() {
    	return 0;
    }
}
