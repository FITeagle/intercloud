package org.fiteagle.dm.intercloud;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.ChatManagerListener;
import java.util.ArrayList;

public class Gateway {
        
    private String server;
    private int port;
    
    private ConnectionConfiguration config;
    private XMPPConnection connection;

    private ChatManager chatManager;
    private MessageListener messageListener;
    
    private ArrayList<String> messageList;
    
    public Gateway(String server, int port) {
        this.server = server;
        this.port = port;
    }
    
    public ArrayList<String> getMessage() {
    	return messageList;
    }
    
    public void init(String username, String password) throws XMPPException {
        
 //       System.out.println(String.format("Initializing connection to server %1$s port %2$d", server, port));
        
        config = new ConnectionConfiguration(server, port);
        connection = new XMPPConnection(config);
		connection.connect();
			
        
 //       System.out.println("Connected: " + connection.isConnected());
        
        chatManager = connection.getChatManager();
        chatManager.addChatListener(new MyChatListener());
        messageListener = new MyMessageListener();

        messageList = new ArrayList<String>();
        
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
    
    public void destroy() {
        if (connection!=null && connection.isConnected()) {
            connection.disconnect();
        }
    }
    
    public void sendMessage(String message, String buddyJID) throws XMPPException {
//        System.out.println(String.format("Sending message '%1$s' to user %2$s", message, buddyJID));
        Chat chat = chatManager.createChat(buddyJID, messageListener);
        chat.sendMessage(message);
    }
        
    class MyMessageListener implements MessageListener {

        public void processMessage(Chat chat, Message message) {
            String body = message.getBody();
            messageList.add(body);
        }
        
    }
    class MyChatListener implements ChatManagerListener {
    	public void chatCreated(Chat chat, boolean createdLocally) {
    		if (!createdLocally) {
    			chat.addMessageListener(messageListener);
    		}
    	}
    }
    
}
