package controllers;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import beans.LoginData;

public class RestController {

	private Client restClient;
	private WebTarget webTarget;
	private static String SERVER_URL = "http://localhost:8081/UserApp_DWA/rest/app";
	
	public Response loginRest(String loginData) throws ParseException {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/login");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(loginData);
		LoginData logData = new LoginData(json.get("uname").toString(),json.get("password").toString());
		return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(logData, MediaType.APPLICATION_JSON));
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
