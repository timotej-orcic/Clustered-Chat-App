package transactions;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

public class ChatChatCommunicator extends Communicator {

	public ChatChatCommunicator() {
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
					.lookup(this.TOPIC_DESTINATION);
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
				System.out.println("*******LUDNICA ChatChat*****");
				System.out.println("Stiglo od: " + msg.getJMSDeliveryMode());
				System.out.println("Random podatak: " + msg.getJMSDestination().toString());
				System.out.println("Received new message from Queue On Chat : " + text + ", with timestamp: " + time);
				System.out.println("*******************");
			} catch(JMSException e) {
				e.printStackTrace();
				return;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void send(String text) {
		// The sent timestamp acts as the message's ID
	    long sent = System.currentTimeMillis();
	    
		TextMessage msg;		
		try {
			msg = session.createTextMessage(text);
		    msg.setLongProperty("sent", sent);
		    producer.send(msg);
			if (USE_LOCAL_TRANSACTIONS)
				session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
