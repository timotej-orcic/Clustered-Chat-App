package service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import beans.User;
import database.DatabaseConnectionProvider;

@Singleton
public class Service {

	@Inject
	private DatabaseConnectionProvider dbConnectionProvider;
	
	private HashMap<String,User> activeUsers;
	
	@PostConstruct
	public void init() {
		activeUsers = new HashMap<String,User>();
	}
	
	public ArrayList<String >getParticipants(){
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		
		return null;	
	}
	

	public HashMap<String, User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(HashMap<String, User> activeUsers) {
		this.activeUsers = activeUsers;
	}
	
}
