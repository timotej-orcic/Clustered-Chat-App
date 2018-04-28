package controllers;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketController {

	@OnMessage
    public String sayHello(String message) {
		//TU TREBA ISKORISTITI SPOJ SA USERAPP PREKO RESTa/JMSa
        System.out.println("Say hello to '" + message + "'");
        return ("Hello" + message);    
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
