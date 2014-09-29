package org.fiteagle.dm.intercloud;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPEndPoint {
	private ConnectionConfiguration connectionConfigurationXMPP;
	private XMPPConnection xmppConnection;
	private ChatManager chatManager;
	private MessageListener messageListener;
	XMPPEndPoint(String server, int port) {
		try {
			connectionConfigurationXMPP = new ConnectionConfiguration(server, port);
			xmppConnection = new XMPPConnection(connectionConfigurationXMPP);
			xmppConnection.connect();
			chatManager = xmppConnection.getChatManager();
			chatManager.addChatListener(new ChatListener());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public boolean performLogin(String username, String password) {
    	if (xmppConnection!=null && xmppConnection.isConnected()) {
    		if(!xmppConnection.isAuthenticated()) {
    			try {
    				xmppConnection.getAccountManager().createAccount(username, password);
    			} catch (XMPPException e) {
    				e.printStackTrace();
    			}
        	}
  			try {
  				xmppConnection.login(username, password);
  			} catch(XMPPException e2) {
  				e2.printStackTrace();
  				return false; 
  			}
  			return true;
  		} else { 
  			return false;
  		}
    }
 
    public void destroy() {
        if (xmppConnection!=null && xmppConnection.isConnected()) {
        	xmppConnection.disconnect();
        }
    }

    public void sendMessage(String message, String buddyJID) throws XMPPException {
        	Chat chat = chatManager.createChat(buddyJID, messageListener);
			chat.sendMessage(message);
    }

    public void setMessageListener(MessageListener msgL) {
    	messageListener = msgL;
    }

    class ChatListener implements ChatManagerListener {  	
    	public void chatCreated(Chat chat, boolean createdLocally) {
    		if (!createdLocally) {
    			chat.addMessageListener(messageListener);
    		}
    	}
    }
}
