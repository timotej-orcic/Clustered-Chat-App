package packages.controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import packages.beans.Group;
import packages.beans.LoginData;
import packages.beans.User;
import packages.beans.UserDTO;

import org.bson.json.JsonParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import packages.modelView.GroupDTO;
import packages.modelView.GroupLeaveDTO;
import packages.modelView.UserRegistrationInfo;
import packages.servise.Service;

@Singleton
@Path("/app")
public class AppController {

	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private Service service;

	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		
		return "Hello it works again";
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(LoginData logData) {	
		return service.userLogin(logData);
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String register(UserRegistrationInfo userData) throws JsonParseException, IOException {
		String ret = "Register failed. Please, try again.";
		
		if(service.userRegister(userData)) {
			ret = "Success!";
		}
		
		return ret;
	}
	
	@GET
	@Path("/getFriends/userName={userName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFriends(@PathParam(value="userName") String userName) throws JsonProcessingException {
		
		return mapper.writeValueAsString(service.getFriends(userName));
	}
	
	@GET
	@Path("/getNonFriends/userName={userName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getNonFriends(@PathParam(value="userName") String userName) throws JsonProcessingException {
		
		return mapper.writeValueAsString(service.getNonFriends(userName));
	}
	
	@DELETE
	@Path("/deleteFriend/userName={userName}&toDelete={toDelete}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFriends(@PathParam(value="userName") String userName, @PathParam(value="toDelete") String toDelete) {	
		return service.deleteFriend(userName, toDelete);			
	}
	
	@PUT
	@Path("/addFriend/userName={userName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addFriends(@PathParam(value="userName") String userName, String toAdd) {	
		return service.addFriend(userName, toAdd);			
	}
	
	@GET
	@Path("/searchFriends/loggedUserName={loggedUserName}&userName={userName: .*}&name={name: .*}&lastName={lastName: .*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchFriends(@PathParam(value="loggedUserName") String loggedUserName,
								@PathParam(value="userName") String userName,
								@PathParam(value="name") String name,
								@PathParam(value="lastName") String lastName) throws JsonProcessingException {
		
		return mapper.writeValueAsString(service.searchFriends(loggedUserName, userName, name, lastName));
	}
	
	@GET
	@Path("/searchNonFriends/loggedUserName={loggedUserName}&userName={userName: .*}&name={name: .*}&lastName={lastName: .*}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchNonFriends(@PathParam(value="loggedUserName") String loggedUserName,
								@PathParam(value="userName") String userName,
								@PathParam(value="name") String name,
								@PathParam(value="lastName") String lastName) throws JsonProcessingException {
		
		return mapper.writeValueAsString(service.searchNonFriends(loggedUserName, userName, name, lastName));
	}
	
	
	@POST
	@Path("/groups/new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GroupDTO createGroup(GroupDTO g) {
		return service.createGroup(g);
	}
	
	@GET
	@Path("/groups/getForUser/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GroupDTO> getGroupsForUser(@PathParam("userName") String userName) {
		return service.getGroups(userName);
	}
	
	@GET
	@Path("/groups/getOne/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public GroupDTO getOne(@PathParam("id") Integer grpId) {
		return service.getOne(grpId);
	}
	
	@PUT
	@Path("/groups/leave")
	@Produces(MediaType.APPLICATION_JSON)
	public GroupDTO leaveGroup(GroupLeaveDTO leaveDto) {
		return service.leaveGroup(leaveDto);
	}
}
