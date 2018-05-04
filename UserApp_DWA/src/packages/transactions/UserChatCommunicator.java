package packages.transactions;

import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import packages.beans.LoginData;
import packages.beans.MessageDTO;
import packages.beans.User;
import packages.controllers.AppController;
import packages.services.Service;

@Singleton
@ApplicationScoped
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class UserChatCommunicator extends Communicator   {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
			if (USE_LOCAL_TRANSACTIONS) 
				session = connection.createSession(true,
					0);//Session.AUTO_ACKNOWLEDGE);
			else
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);

			consumer = session.createConsumer(queue, "destination = 'user'");
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
				System.out.println("Poruka stigla na userchat!: " + text);
				
				ObjectMapper mapper = new ObjectMapper();
				MessageDTO clientMessage = mapper.readValue(text, MessageDTO.class);
				String content = clientMessage.getContent();
				String loggedUserName = clientMessage.getLoggedUserName();
				
				switch(clientMessage.getMessageType()) {
				case("login"):
					LoginData logData = mapper.readValue(content, LoginData.class);
					User u = service.userLogin(logData);
					String stringToSend = mapper.writeValueAsString(u);
					System.out.println("UserChat salje poruku ka ChatApp: " + stringToSend);
					send(stringToSend);
					break;
				default:
					break;
				}
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
			msg.setObjectProperty("destination", "chat");
		    msg.setLongProperty("sent", sent);
		    producer.send(msg);
			if (USE_LOCAL_TRANSACTIONS)
				session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void hackz() {
		System.out.println("-----------************For the hax**************---------------");
	}
}
