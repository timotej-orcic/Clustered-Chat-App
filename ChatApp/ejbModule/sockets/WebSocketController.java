package sockets;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import sessions.SessionHandler;
import transactions.JMSTransactions;

@ServerEndpoint("/websocket")
public class WebSocketController {

	@Inject
	private SessionHandler sessionHandler;
	
	@Inject
	private JMSTransactions transactions;
	
	@OnMessage
    public String sayHello(String message) {
		//TU TREBA ISKORISTITI SPOJ SA USERAPP PREKO RESTa/JMSa
		//http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/HomeWebsocket/WebsocketHome.html
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
