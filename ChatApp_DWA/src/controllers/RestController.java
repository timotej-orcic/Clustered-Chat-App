package controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import beans.Group;
import beans.LoginData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.App;
import beans.LoginData;
import modelView.GroupDTO;
import modelView.GroupLeaveDTO;
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
		//regInfo.setHost(App.getHost());

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


	public Response searchFriends(String loggedUserName, String searchData) throws ParseException {
		restClient = ClientBuilder.newClient();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(searchData);
		webTarget = restClient.target(SERVER_URL + "/searchFriends/loggedUserName="+loggedUserName+"&userName="+json.get("userName")+"&name="+json.get("name")+"&lastName="+json.get("lastName"));

		return webTarget.request(MediaType.APPLICATION_JSON).get();
	}

	public Response searchNonFriends(String loggedUserName, String searchData) throws ParseException {
		restClient = ClientBuilder.newClient();
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(searchData);
		webTarget = restClient.target(SERVER_URL + "/searchNonFriends/loggedUserName="+loggedUserName+"&userName="+json.get("userName")+"&name="+json.get("name")+"&lastName="+json.get("lastName"));

		return webTarget.request(MediaType.APPLICATION_JSON).get();
	}

	public Response getGroup(String info) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/getOne/" + info);

		return webTarget.request().get();
	}

	public Response getGroups(String loggedUserName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/getForUser/" + loggedUserName);

		return webTarget.request().get();
	}
	
	

	public Response createGroup(String newGroup) throws ParseException {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/new");
		JSONObject obj = (JSONObject) parser.parse(newGroup);

		GroupDTO group = new GroupDTO();
		group.setGroupId(0);
		group.setGroupName(obj.get("groupName").toString());
		group.setParentUserId(obj.get("parentUserId").toString());
		JSONArray objArr = (JSONArray) obj.get("groupMemberList");
		Object[] arr = objArr.toArray();
		ArrayList<String> users = new ArrayList<String>();
		for(Object o : objArr) {
			users.add(o.toString());
		}
		group.setGroupMembersList(users);

		return webTarget.request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(group, MediaType.APPLICATION_JSON));
	}

	public Response deleteGroup(String groupId) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/delete/" + groupId);

		return webTarget.request().delete();
	}

	public Response leaveGroup(String info) throws ParseException {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/leave");
		
		JSONObject object = (JSONObject) parser.parse(info);
		
		GroupLeaveDTO gl = new GroupLeaveDTO();
		gl.setGroupId(Integer.parseInt(object.get("groupId").toString()));
		Object kickedBy = object.get("kickedBy");
		if(kickedBy != null)
			gl.setKickedBy(kickedBy.toString());
		else 
			gl.setKickedBy(null);
		gl.setLeaverUsername(object.get("leaverUsername").toString());

		return webTarget.request(MediaType.APPLICATION_JSON).put(Entity.entity(gl, MediaType.APPLICATION_JSON));
	}

	public Response addToGroup(String info) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/{id}/leave/{userId}");

		return null;
	}
	
	public Response getAllGroups() {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/getAll");

		return webTarget.request().get();
	}

	public Response getCreatedGroups(String loggedUserName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/getCreatedBy/" + loggedUserName);

		return webTarget.request().get();
	}
	
	public Response getGroupsAddedIn(String loggedUserName) {
		restClient = ClientBuilder.newClient();
		webTarget = restClient.target(SERVER_URL + "/groups/getAddedInto/" + loggedUserName);

		return webTarget.request().get();
	}
}
