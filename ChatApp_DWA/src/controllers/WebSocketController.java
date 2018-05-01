package controllers;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Message;

@ServerEndpoint("/websocket")
public class WebSocketController {

	@OnMessage
    public String sayHello(String message, Session session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		
		RestController restController = new RestController();
		ObjectMapper mapper = new ObjectMapper();
		Message clientMessage = mapper.readValue(message, Message.class);

		if(clientMessage != null) {
			String content = clientMessage.getContent();
			String loggedUserName = clientMessage.getLoggedUserName();
			Response resp = null;
			switch (clientMessage.getMessageType()) {
			case "login":
				resp = restController.loginRest(content);
				return resp.readEntity(String.class);
			case "register": {
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
			case "chat":
				break;
			case "getFriends":
				resp = restController.getFriends(loggedUserName);
				return mapper.writeValueAsString(new Message("getFriends", resp.readEntity(String.class), loggedUserName));
			case "getNonFriends":
				break;
			case "deleteFriend":
				resp = restController.deleteFriend(loggedUserName, content);
				return mapper.writeValueAsString(new Message("deleteFriend",resp.readEntity(String.class), loggedUserName));
			case "addFriend":
				break;
			default:
				System.out.println("AAAA");
			}
		}

		return "lol";
    }

    @OnOpen
    public void helloOnOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
        
    }
    
    @OnClose
    public void helloOnClose(CloseReason reason) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }
}
