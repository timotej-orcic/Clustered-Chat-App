package transactions;

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
	
	//Negde mora rollback transakcije da se radi, skontacemo gde
	
	@SuppressWarnings("unchecked")
	public JMSTransactions() {
		while(true) {
			try {
				@SuppressWarnings("rawtypes")
				Hashtable env = new Hashtable();
				env.put("java.naming.factory.initial",
						"org.jboss.naming.remote.client.InitialContextFactory");
				env.put("java.naming.provider.url",
				     "http-remoting://localhost:8080");
				
				Context ctx = new InitialContext(env);
				ConnectionFactory cf = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
				final Topic topic = (Topic) ctx.lookup("jms/topic/myTopic");
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
				break;
			}
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

}
