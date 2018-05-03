package packages.services;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;


public class JMSTransactions implements MessageListener {
	
	private static final boolean USE_LOCAL_TRANSACTIONS = false;
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/topic/mojTopic";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String INITIAL_CONTEXT_NAMING = "org.jboss.ejb.client.naming";
    private static final String PROVIDER_URL = "http-remoting://localhost:8081";
	
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    
    public JMSTransactions() {
		try {
			Hashtable <String, String> env = new Hashtable <String, String>();
		      env.put("java.naming.factory.url.pkgs", 
		    		  INITIAL_CONTEXT_NAMING);
		      env.put("java.naming.factory.initial",
		    		     INITIAL_CONTEXT_FACTORY);
		      env.put("java.naming.provider.url",
		        PROVIDER_URL);
						
			Context context = new InitialContext(env);
			ConnectionFactory cf = (ConnectionFactory) context
					.lookup(DEFAULT_CONNECTION_FACTORY);
			final Topic topic = (Topic) context
					.lookup(DEFAULT_DESTINATION);
			context.close();
			
			connection = cf.createConnection("appUser", "appUser");
			if (USE_LOCAL_TRANSACTIONS) 
				session = connection.createSession(true,
					0);//Session.AUTO_ACKNOWLEDGE);
			else
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);

			consumer = session.createConsumer(topic);
			consumer.setMessageListener(this);
		    
			producer = session.createProducer(topic);
			
			connection.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
	@Override
	public void onMessage(Message msg) {
		try {
			TextMessage txtMsg = (TextMessage) msg;
			try {
				String text = txtMsg.getText();
				long time = txtMsg.getLongProperty("sent");
				System.out.println("Received new message from Queue : " + text + ", with timestamp: " + time);
			} catch(JMSException e) {
				e.printStackTrace();
				return;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void sendMessage(String message) {
	    // The sent timestamp acts as the message's ID
	    long sent = System.currentTimeMillis();
	    
		TextMessage msg;		
		try {
			msg = session.createTextMessage(message);
		    msg.setLongProperty("sent", sent);
		    producer.send(msg);
			if (USE_LOCAL_TRANSACTIONS)
				session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public String awaitResponse() {
		String retVal = "No response from UserApp, please try again.";
		Message msg = null;		
		
		try {
			msg = consumer.receive(3000);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		if(msg != null) {
			try {
				retVal = msg.getBody(String.class);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
		return retVal;
	}
	
	public void closeConnection() {
		try {
			producer.close();
			consumer.close();
			connection.stop();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
