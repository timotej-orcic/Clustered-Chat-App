package controllers;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Message;

@ServerEndpoint("/websocket")
public class WebSocketController {

	@OnMessage
    public String sayHello(String message, Session session) throws JsonParseException, JsonMappingException, IOException {
		
		RestController restController = new RestController();
		ObjectMapper mapper = new ObjectMapper();
		Message clientMessage = mapper.readValue(message, Message.class);
		
		if(clientMessage != null) {
			String content = clientMessage.getContent();
			switch (clientMessage.getMessageType()) {
			case "login":
				return(restController.loginRest(content));
			case "register":
				return(mapper.writeValueAsString(new Message("success","Register successfull!")));
			case "chat":
				return(mapper.writeValueAsString(new Message("success","Chat is working!")));
			default:
				return(mapper.writeValueAsString(new Message("faliure","Unrecognized message type!")));
			}
		}
		else
			return (mapper.writeValueAsString(new Message("faliure","Server error while parsing inputed data!")));    
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
