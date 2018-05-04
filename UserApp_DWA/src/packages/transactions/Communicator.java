package packages.transactions;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import packages.services.Service;
@Singleton
@ApplicationScoped
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public abstract class Communicator implements MessageListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
    @Inject
    protected Service service;
    
    
    public abstract void send(String text);
    @PostConstruct
    public abstract void init();
    public void killAllConnections() {
    	try {
			producer.close();
			consumer.close();
			connection.stop();
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
}
