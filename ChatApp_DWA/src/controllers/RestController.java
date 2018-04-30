package controllers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RestController {

	private Client restClient;
	private WebTarget webTarget;
	private static String SERVER_URL = "http://localhost:8081/UserApp_DWA/rest/app";
	
	public String loginRest(String loginData) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/login");
		
		return webTarget.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	public String getFriends(String userName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/getFriends/userName="+userName);
			
		return webTarget.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
	public String getNonFriends(String userName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/getNonFriends/userName="+userName);
			
		return webTarget.request(MediaType.APPLICATION_JSON).get(String.class);
	}
	
}
