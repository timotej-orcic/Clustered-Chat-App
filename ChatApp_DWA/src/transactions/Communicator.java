package transactions;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

public abstract class Communicator implements MessageListener {
	protected static final boolean USE_LOCAL_TRANSACTIONS = false;
    protected static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    protected static final String TOPIC_DESTINATION = "jms/topic/mojTopic";
    protected static final String QUEUE_DESTINATION = "jms/queue/mojQueue";
    protected static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    protected static final String INITIAL_CONTEXT_NAMING = "org.jboss.ejb.client.naming";
    protected static final String PROVIDER_URL = "http-remoting://localhost:8081";
    
    protected Connection connection;
    protected Session session;
    protected MessageProducer producer;
    protected MessageConsumer consumer;
    
    
    public abstract void send(String text);
    public abstract void turnOnListener();
    public abstract void turnOffListener();
    
    public void killAllConnections() {
    	try {
			producer.close();
			consumer.close();
			connection.stop();
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    
    public String awaitResponse() {
		String retVal = "No response from UserApp, please try again.";
		Message msg = null;		
		
		try {
			msg = consumer.receiveNoWait();
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
}
