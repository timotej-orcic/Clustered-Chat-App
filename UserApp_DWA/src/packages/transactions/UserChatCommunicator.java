package packages.transactions;

import java.io.IOException;
import java.util.Hashtable;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.jboss.as.cli.CommandLineException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import packages.beans.LoginData;
import packages.beans.MessageDTO;
import packages.beans.User;
import packages.modelView.UserRegistrationInfo;
import packages.services.Service;

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
					.lookup(QUEUE_DESTINATION);
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
				System.out.println("*******LUDNICA UserChat*****");
				System.out.println("Stiglo od: " + msg.getJMSDeliveryMode());
				System.out.println("Random podatak: " + msg.getJMSDestination().toString());
				System.out.println("Received new message from Queue On Chat : " + text + ", with timestamp: " + time);
				System.out.println("*******************");
									
				doCases(text);
				
				AppController appCont = new AppController();
				ObjectMapper mapper = new ObjectMapper();
				MessageDTO clientMessage = mapper.readValue(text, MessageDTO.class);
				String content = clientMessage.getContent();
				String loggedUserName = clientMessage.getLoggedUserName();
				
				switch(clientMessage.getMessageType()) {
				case("login"):
					LoginData logData = mapper.readValue(content, LoginData.class);
					User u = service.userLogin(logData);
					send(mapper.writeValueAsString(u));
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
	    // The sent time-stamp acts as the message's ID
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
	
	public void doCases(String text) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		MessageDTO message = mapper.readValue(text, MessageDTO.class);	
		String content = message.getContent();				
		String loggedUserName = message.getLoggedUserName();		
		
		switch(message.getMessageType()) {
		case("login"): {
			LoginData logData = mapper.readValue(content, LoginData.class);
			User u = null;
			try {
				u = service.userLogin(logData);
			} catch (InstanceNotFoundException | AttributeNotFoundException | MalformedObjectNameException
					| ReflectionException | MBeanException | CommandLineException e) {
				e.printStackTrace();
			}
			send(mapper.writeValueAsString(u));
			break;
		}
		case("register"): {
			UserRegistrationInfo userData = mapper.readValue(content, UserRegistrationInfo.class);
			
			String ret = "Register failed. Please, try again.";
			if(service.userRegister(userData)) {
				ret = "Success!";
			}
			
			send(ret);
			break;
		}
		case "getParticipants":
			
		case "getMessages":
			
		case "chat":
			
		case "getFriends":

		case "getNonFriends":

		case "deleteFriend":

		case "addFriend":

		case "searchFriends":

		case "searchNonFriends":

		case "getGroups": 

		case "getCreatedGroups":

		case "getGroupsAddedIn":

		case "createGroup":

		case "getOneGroup":

		case "deleteGroup":

		case "leaveKickFromGroup":

		case "getAddableUsers":

		case "addToGroup":

		default:
			return;
		}
	}
	
	public void hackz() {
		System.out.println("-----------************For the hax**************---------------");
	}
}
