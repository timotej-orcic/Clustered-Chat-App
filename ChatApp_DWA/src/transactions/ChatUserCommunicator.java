package transactions;

import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

@Singleton
@ApplicationScoped
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ChatUserCommunicator extends Communicator   {
    
	private String response;
	
	@PostConstruct
	public void init() {
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
			
			final Queue queue = (Queue) context
					.lookup(this.QUEUE_DESTINATION);
			context.close();
			
			connection = cf.createConnection("appUser", "appUser");
		
			session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);

			consumer = session.createConsumer(queue, "destination = 'chat'");
			consumer.setMessageListener(this);
			producer = session.createProducer(queue);
			
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
				System.out.println("Poruka stigla na chatuser!!: " + text);
				response = text;
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
	public void send(String message) {
	    // The sent timestamp acts as the message's ID
	    long sent = System.currentTimeMillis();
	    
		TextMessage msg;		
		try {
			msg = session.createTextMessage(message);
			msg.setObjectProperty("destination", "user");
		    msg.setLongProperty("sent", sent);
		    producer.send(msg);
			if (USE_LOCAL_TRANSACTIONS)
				session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponse() {
		return response;
	}
	
	public void resetResponse() {
		response = null;
	}
}