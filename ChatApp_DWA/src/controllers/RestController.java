package controllers;


import java.io.IOException;

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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.App;
import beans.LoginData;
import modelView.UserRegistrationInfo;

public class RestController {

	private Client restClient;
	private WebTarget webTarget;
	private static String SERVER_URL = "http://localhost:8081/UserApp_DWA/rest/app";
	private JSONParser parser = new JSONParser();
	
	public Response loginRest(String loginData) throws ParseException {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/login");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(loginData);
		LoginData logData = new LoginData(json.get("uname").toString(),json.get("password").toString());
		return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(logData, MediaType.APPLICATION_JSON));
	}
	
	public String registerRest(String registerData) throws ParseException {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/register");
		JSONObject obj = (JSONObject) parser.parse(registerData);
		
		UserRegistrationInfo regInfo = new UserRegistrationInfo();
		regInfo.setLastName(obj.get("lastname").toString());
		regInfo.setName(obj.get("name").toString());
		regInfo.setPassword(obj.get("password").toString());
		regInfo.setUserName(obj.get("username").toString());
		regInfo.setHost(App.getHost());
		
		return webTarget.request(MediaType.APPLICATION_JSON)
						.post(Entity.json(regInfo), String.class);
	}
	
	public Response getFriends(String userName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/getFriends/userName="+userName);
			
		return webTarget.request(MediaType.APPLICATION_JSON).get();
	}
	
	public Response getNonFriends(String userName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/getNonFriends/userName="+userName);
			
		return webTarget.request(MediaType.APPLICATION_JSON).get();
	}
	
	public Response deleteFriend(String userName, String toDelete) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/deleteFriend/userName="+userName+"&toDelete="+toDelete);
		
		return webTarget.request(MediaType.APPLICATION_JSON).delete();
	}
	
	public Response addFriend(String userName, String toAdd) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/addFriend/userName="+userName);
		
		return webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(toAdd, MediaType.APPLICATION_JSON));
	}
	
	
}
