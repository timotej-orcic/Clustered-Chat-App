package controllers;

import java.io.IOException;

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
			switch (clientMessage.getMessageType()) {
			case "login":
				Response resp = restController.loginRest(content);
				return resp.readEntity(String.class);
			case "register":
				break;
			case "chat":
				break;
			case "getFriends":
				break;
			case "getNonFriends":
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
