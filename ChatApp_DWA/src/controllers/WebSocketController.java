package controllers;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Response;

import org.jboss.as.cli.CommandLineException;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.App;
import beans.Host;
import beans.Message;
import beans.User;
import service.Service;
import transactions.ChatChatCommunicator;
import transactions.ChatUserCommunicator;

@ServerEndpoint("/websocket")
public class WebSocketController {
		
	private static final boolean IS_JMS = true;
	
	@Inject
	private Service service;
	@Inject
	private ChatChatCommunicator ccc;
	@Inject
	private ChatUserCommunicator cuc;
	
	static Set<Session> userSessions = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnMessage
    public String sayHello(String message, Session session) throws JsonParseException, JsonMappingException, IOException, ParseException, InstanceNotFoundException, AttributeNotFoundException, MalformedObjectNameException, ReflectionException, MBeanException, CommandLineException {
		
		RestController restController = new RestController();
		ObjectMapper mapper = new ObjectMapper();
		Message clientMessage = mapper.readValue(message, Message.class);

		if(clientMessage != null) {
			String content = clientMessage.getContent();
			String loggedUserName = clientMessage.getLoggedUserName();
			Response resp = null;
            
			switch (clientMessage.getMessageType()) {
			case "login": 
			{	
				User loggedUser = null;
				if(IS_JMS) {
					try {
						cuc.send(message);
						String response = null;
						
						Thread.yield();
						Thread.sleep(4000);
						
						response = cuc.getResponse();
						cuc.resetResponse();
						loggedUser = mapper.readValue(response, User.class);
					} catch(Exception e) {
						System.out.println("ne mos uhvatiti hosta da si bog otac");
						System.out.println(e.getMessage());
					} finally {
						//jmsController.loginJMS(content);
					}
					
					return loggedUser.getUserName();
				}
				else {
					resp = restController.loginRest(content);
					loggedUser = resp.readEntity(User.class);
				}
				
				if(loggedUser != null) {
					service.getActiveUsers().put(loggedUser.getUserName(), loggedUser);
					String userName = (String) session.getUserProperties().get("userName");
					if(userName==null) {
						session.getUserProperties().put("userName", loggedUser.getUserName());
					}
					return loggedUser.getUserName();
				}
			}
			
			case "register": 
			{
				System.out.println("Registering user....");
				String succ = restController.registerRest(content);
				if(succ.toLowerCase().indexOf("fail") > -1) {
					System.out.println("Fail");
					return mapper.writeValueAsString(new Message("sucess", succ, null));
				} else {
					System.out.println("Uspeh");
					return mapper.writeValueAsString(new Message("fail", succ, null));		
				}
			}
			
			case "getParticipants":
				return mapper.writeValueAsString(new Message("getParticipants",mapper.writeValueAsString(service.getParticipants(loggedUserName)), loggedUserName));
			case "getMessages":
				return mapper.writeValueAsString(new Message("getMessages", mapper.writeValueAsString(service.getMessages(loggedUserName, content)),loggedUserName));
			case "chat":
				return mapper.writeValueAsString(new Message("chat", mapper.writeValueAsString(service.chat(loggedUserName, content)),loggedUserName));
			case "getFriends":
				resp = restController.getFriends(loggedUserName);
				return mapper.writeValueAsString(new Message("getFriends", resp.readEntity(String.class), loggedUserName));
			case "getNonFriends":
				resp = restController.getNonFriends(loggedUserName);
				return mapper.writeValueAsString(new Message("getNonFriends", resp.readEntity(String.class), loggedUserName));
			case "deleteFriend":
				resp = restController.deleteFriend(loggedUserName, content);
				return mapper.writeValueAsString(new Message("deleteFriend",resp.readEntity(String.class), loggedUserName));
			case "addFriend":
				resp = restController.addFriend(loggedUserName, content);
				return mapper.writeValueAsString(new Message("addFriend",resp.readEntity(String.class), loggedUserName));
			case "searchFriends":
				resp = restController.searchFriends(loggedUserName, content);
				return mapper.writeValueAsString(new Message("searchFriends", resp.readEntity(String.class), loggedUserName));
			case "searchNonFriends":
				resp = restController.searchNonFriends(loggedUserName, content);
				return mapper.writeValueAsString(new Message("searchNonFriends", resp.readEntity(String.class), loggedUserName));
			case "getGroups": {
				resp = restController.getGroups(loggedUserName);
				return mapper.writeValueAsString(new Message("getGroups", resp.readEntity(String.class), loggedUserName));
			}
			case "getCreatedGroups": {
				resp = restController.getCreatedGroups(loggedUserName);
				return mapper.writeValueAsString(new Message("getCreatedGroups", resp.readEntity(String.class), loggedUserName));
			}
			case "getGroupsAddedIn": {
				resp = restController.getGroupsAddedIn(loggedUserName);
				return mapper.writeValueAsString(new Message("getGroupsAddedIn", resp.readEntity(String.class), loggedUserName));
			}
			case "createGroup": {
				resp = restController.createGroup(content);
				return mapper.writeValueAsString(new Message("createGroup", resp.readEntity(String.class), loggedUserName));
			}
			case "getOneGroup": {
				resp = restController.getGroup(content);
				return mapper.writeValueAsString(new Message("getOneGroup", resp.readEntity(String.class), loggedUserName));
			}
			case "deleteGroup": {
				resp = restController.deleteGroup(content);
				return mapper.writeValueAsString(new Message("deleteGroup", resp.readEntity(String.class), loggedUserName));
			}
			case "leaveKickFromGroup": {
				resp = restController.leaveGroup(content);
				return mapper.writeValueAsString(new Message("leaveKickFromGroup", resp.readEntity(String.class), loggedUserName));
			}
			case "getAddableUsers": {
				resp = restController.getAddableUsers(content);
				return mapper.writeValueAsString(new Message("getAddableUsers", resp.readEntity(String.class), loggedUserName));
			}
			case "addToGroup": {
				resp = restController.addToGroup(content);
				return mapper.writeValueAsString(new Message("addToGroup", resp.readEntity(String.class), loggedUserName));
			}
			default:
				System.out.println("AAAA");
			}
		}

		return null;
    }

    @OnOpen
    public void helloOnOpen(Session session) {
    	userSessions.add(session);
        System.out.println("WebSocket opened: " + session.getId());
        
    }
    
    @OnClose
    public void helloOnClose(Session session, CloseReason reason) {
    	userSessions.remove(session);
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }
    
//    public static void waitForAnswer() {
//    	monitorState = true;
//    	while(monitorState) {
//    		synchronized (monitor) {
//				try {
//					monitor.wait();
//				} catch(Exception e) {}
//			}
//    	}
//    }
//    
//    public static void unlock() {
//    	synchronized(monitor) {
//    		monitorState = false;
//    		monitor.notifyAll();
//    	}
//    }
}
