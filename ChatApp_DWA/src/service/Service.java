package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import beans.MessageDB;
import beans.ParticipantInfo;
import beans.User;
import database.DatabaseConnectionProvider;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class Service {

	@Inject
	private DatabaseConnectionProvider dbConnectionProvider;
	
	private HashMap<String,User> activeUsers;
	
	@PostConstruct
	public void init() {
		activeUsers = new HashMap<String,User>();
	}
	
	public ArrayList<String>getParticipants(String loggedUserName){
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		ArrayList<String> retVal = new ArrayList<String>();
		
		DBObject clause1 = new BasicDBObject("sender", loggedUserName);  
		DBObject clause2 = new BasicDBObject("receiverUser", loggedUserName);    
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		BasicDBObject query = new BasicDBObject("$or", or);
		FindIterable<Document> documents = mc.find(query);
		for(Document d : documents) {
			String sender = d.getString("sender");
			String receiver = d.getString("receiverUser");
			if(!sender.equals(loggedUserName) && !retVal.contains(sender)) {
				retVal.add(sender);
			}
			if(receiver!=null) {
				if(!receiver.equals(loggedUserName) && !retVal.contains(receiver)) {
					retVal.add(receiver);
				}
			}
		}
		
		Collections.reverse(retVal);
		
		return retVal;	
	}
	
	//Prepravljena verzija getParticipants()
	/*public ArrayList<ParticipantInfo>getParticipants(String loggedUserName){
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		MongoCollection<Document> mcGr = md.getCollection("Groups");
		ArrayList<ParticipantInfo> retVal = new ArrayList<ParticipantInfo>();
		
		DBObject clause1 = new BasicDBObject("sender", loggedUserName);  
		DBObject clause2 = new BasicDBObject("receiverUser", loggedUserName);    
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		BasicDBObject query = new BasicDBObject("$or", or);
		FindIterable<Document> documents = mc.find(query);
		for(Document d : documents) {
			String sender = d.getString("sender");
			String receiver = d.getString("receiverUser");
			if(!sender.equals(loggedUserName) && !retVal.contains(sender)) {
				ParticipantInfo pi = new ParticipantInfo(sender, false);
				retVal.add(sender);
			}
			if(receiver!=null) {
				if(!receiver.equals(loggedUserName) && !retVal.contains(receiver)) {
					ParticipantInfo pi = new ParticipantInfo(receiver, false);
					retVal.add(receiver);
				}
			}
		}
		
		Collections.reverse(retVal);
		
		return retVal;	
	}*/
	
	public ArrayList<MessageDB> getMessages(String loggedUserName, String otherUser){
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		ArrayList<MessageDB> retVal = new ArrayList<MessageDB>();
		
		DBObject clauseAnd1 = new BasicDBObject("sender", loggedUserName);  
		DBObject clauseAnd2 = new BasicDBObject("receiverUser", otherUser);    
		BasicDBList and1 = new BasicDBList();
		and1.add(clauseAnd1);
		and1.add(clauseAnd2);
		BasicDBObject andQuery1 = new BasicDBObject("$and", and1);
		
		DBObject clauseAnd3 = new BasicDBObject("sender", otherUser);  
		DBObject clauseAnd4 = new BasicDBObject("receiverUser", loggedUserName);    
		BasicDBList and2 = new BasicDBList();
		and2.add(clauseAnd3);
		and2.add(clauseAnd4);
		BasicDBObject andQuery2 = new BasicDBObject("$and", and2);
		
		BasicDBList or = new BasicDBList();
		or.add(andQuery1);
		or.add(andQuery2);
		BasicDBObject query = new BasicDBObject("$or", or);
		
		FindIterable<Document> documents = mc.find(query);
		for(Document d : documents) {
			MessageDB m = new MessageDB();
			m.setSenderId(d.getString("sender"));
			m.setReceiverUser(d.getString("receiverUser"));
			m.setContent(d.getString("content"));
			m.setSendingTime(d.getDate("sentDate"));
			retVal.add(m);
		}
		
		
		return retVal;	
	}

	public MessageDB chat(String loggedUserName, String messageInfo) throws ParseException {
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(messageInfo);
		
		MessageDB m = new MessageDB();
		m.setSenderId(loggedUserName);
		m.setReceiverUser(json.get("active").toString());
		m.setContent(json.get("sendText").toString());
		m.setSendingTime(new Date());
		
		
		if(activeUsers.get(json.get("active").toString())==null) {		
			Document newMessage = new Document()
					.append("sender", m.getSenderId())
					.append("receiverUser", m.getReceiverUser())
					.append("content", m.getContent())
					.append("sentDate", m.getSendingTime());
			mc.insertOne(newMessage);
		}
		
		return m;
	}
	
	public HashMap<String, User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(HashMap<String, User> activeUsers) {
		this.activeUsers = activeUsers;
	}
	
}
