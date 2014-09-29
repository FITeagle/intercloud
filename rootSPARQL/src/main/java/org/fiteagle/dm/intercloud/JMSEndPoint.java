package org.fiteagle.dm.intercloud;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSEndPoint {
	private Topic topic;
	private Session topicSession;
	private TopicConnection topicConnection;
	private MessageProducer messageProducer;
	private MessageConsumer messageConsumer;
	
	JMSEndPoint(String topicName, String filter) {
		try {
			InitialContext context = new InitialContext();
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)context.lookup("ConnectionFactory");
			topicConnection = topicConnectionFactory.createTopicConnection();
			topicSession = topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = (Topic) context.lookup(topicName);
			messageProducer = topicSession.createProducer(topic);
			topicConnection.start();
			//String filter = "message = 'org.fiteagle.core.db.resources'";
			messageConsumer = topicSession.createConsumer(topic, filter);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message, String messageProperty) throws JMSException {
		TextMessage msg = topicSession.createTextMessage(message);
		msg.setStringProperty("message", messageProperty);
		messageProducer.send(msg);
	}
	
	public void setMessageListener(MessageListener msgL) {
		try {
			messageConsumer.setMessageListener(msgL);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
