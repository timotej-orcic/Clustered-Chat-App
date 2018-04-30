package packages.controllers;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import packages.beans.LoginData;
import packages.servise.Service;

@Singleton
@Path("/app")
public class AppController {

	@Inject
	Service service;
	
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
	public String login(LoginData logData) {
		
		return service.userLogin(logData);
	}
	
	@GET
	@Path("/getFriends/userName={userName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFriends(@PathParam(value="userName") String userName) {
		
		System.out.println("Prijatelji za "+userName);
		
		return "";
	}
	
	@GET
	@Path("/getNonFriends/userName={userName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getNonFriends(@PathParam(value="userName") String userName) {
		
		System.out.println("NePrijatelji za "+userName);
		
		return "";
	}
	
}
