package transactions;

import java.util.Hashtable;
import java.util.Properties;

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
	 // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";
    private static final String DEFAULT_MESSAGE_COUNT = "1";
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";
    
	public JMSTransactions() {
		
			/*try {
				final Properties env = new Properties();
	            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
	            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
	            env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
	            env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
	            
				Context ctx = new InitialContext(env);
				
				String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
				ConnectionFactory cf = (ConnectionFactory) ctx.lookup(connectionFactoryString);
				final Topic topic = (Topic) ctx.lookup("jms/myTopic");
				ctx.close();
				
				Connection conn = cf.createConnection();
				final Session session;
				
				if(USE_LOCAL_TRANSACTIONS) {
					session = conn.createSession(true, 0);
				} else {
					session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				}
				
				conn.start();
				
				MessageConsumer consumer = session.createConsumer(topic);
				consumer.setMessageListener(this);
				TextMessage msg = session.createTextMessage("Topic message!");
				long sent = System.currentTimeMillis();
				msg.setLongProperty("sent", sent);
				
				MessageProducer producer = session.createProducer(topic);
				producer.send(msg);
				
				if(USE_LOCAL_TRANSACTIONS) {
					session.commit();
				}
				
				producer.close();
				consumer.close();
				conn.stop();
				conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			*/
		
	}
		
	@Override
	public void onMessage(Message msg) {
		try {
			TextMessage txtMsg = (TextMessage) msg;
			try {
				String text = txtMsg.getText();
				long time = txtMsg.getLongProperty("sent");
				System.out.println("Received new message from Topic : " + text + ", with timestamp: " + time);
			} catch(JMSException e) {
				e.printStackTrace();
				return;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
