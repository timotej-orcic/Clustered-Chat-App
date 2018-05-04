package service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	
	public ArrayList<ParticipantInfo>getParticipants(String loggedUserName){
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		ArrayList<ParticipantInfo> retVal = new ArrayList<ParticipantInfo>();
		
		DBObject clause1 = new BasicDBObject("sender", loggedUserName); 
		DBObject clause2 = new BasicDBObject("receiverUsers", loggedUserName);  
		
		BasicDBList or = new BasicDBList();
		or.add(clause1);
		or.add(clause2);
		BasicDBObject query = new BasicDBObject("$or", or);
		FindIterable<Document> documents = mc.find(query);
		for(Document d : documents) {
			String sender = d.getString("sender");
			List<String> receivers = (List<String>) d.get("receiverUsers");
			int groupId = -1;
			if(d.containsKey("groupId")) {
				groupId = d.getInteger("groupId");
			}
			
			String groupName = d.getString("groupName");
			ParticipantInfo p = null;
			if(!sender.equals(loggedUserName) && groupId==-1) {
				p = new ParticipantInfo(sender,null,-1,null);
			}else if(!sender.equals(loggedUserName) && groupId!=-1) {
				receivers.remove(loggedUserName);
				receivers.add(sender);
				p = new ParticipantInfo(null,receivers,groupId,groupName);
			}else if(sender.equals(loggedUserName) && groupId==-1) {
				p = new ParticipantInfo(receivers.get(0),null,-1,null);
			}else if(sender.equals(loggedUserName) && groupId!=-1) {
				p = new ParticipantInfo(null,receivers,groupId,groupName);	
			}	
			
			if(!checkifParticipantExists(retVal,p))
				retVal.add(p);
		}
		
		Collections.reverse(retVal);
		
		
		return retVal;	
	}
	
	public ArrayList<MessageDB> getMessages(String loggedUserName, String participantInfo) throws ParseException{
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
		ArrayList<MessageDB> retVal = new ArrayList<MessageDB>();
		
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(participantInfo);
		String participant = null;
		if(json.get("participant")!=null) {
			participant = json.get("participant").toString();
		}
		
		int groupId = -1;
		
		Object gId = json.get("groupId");
		if(gId!=null) {
			groupId = Integer.parseInt(gId.toString());
		}
		
		
		BasicDBObject query;
		
		if(participant!=null) {
			
			DBObject clauseAnd1 = new BasicDBObject("sender", loggedUserName);  
			DBObject clauseAnd2 = new BasicDBObject("receiverUsers", participant);  
		    DBObject clauseAnd3 = new BasicDBObject("groupId", new BasicDBObject("$exists", false));
			BasicDBList and1 = new BasicDBList();
			and1.add(clauseAnd1);
			and1.add(clauseAnd2);
			and1.add(clauseAnd3);
			BasicDBObject andQuery1 = new BasicDBObject("$and", and1);
			
			DBObject clauseAnd4 = new BasicDBObject("sender", participant);  
			DBObject clauseAnd5 = new BasicDBObject("receiverUsers", loggedUserName);   
		    DBObject clauseAnd6 = new BasicDBObject("groupId", new BasicDBObject("$exists", false));
			BasicDBList and2 = new BasicDBList();
			and2.add(clauseAnd4);
			and2.add(clauseAnd5);
			and2.add(clauseAnd6);
			BasicDBObject andQuery2 = new BasicDBObject("$and", and2);
			BasicDBList or = new BasicDBList();
			or.add(andQuery1);
			or.add(andQuery2);
			query = new BasicDBObject("$or", or);
				
		}else {
			
			DBObject clauseAnd1 = new BasicDBObject("sender", loggedUserName);  
			DBObject clauseAnd2 = new BasicDBObject("groupId", groupId);    
			BasicDBList and1 = new BasicDBList();
			and1.add(clauseAnd1);
			and1.add(clauseAnd2);
			BasicDBObject andQuery1 = new BasicDBObject("$and", and1);
			
			DBObject clauseAnd3 = new BasicDBObject("receiverUsers", loggedUserName);  
			DBObject clauseAnd4 = new BasicDBObject("groupId", groupId);    
			BasicDBList and2 = new BasicDBList();
			and2.add(clauseAnd3);
			and2.add(clauseAnd4);
			BasicDBObject andQuery2 = new BasicDBObject("$and", and2);
			BasicDBList or = new BasicDBList();
			or.add(andQuery1);
			or.add(andQuery2);
			query = new BasicDBObject("$or", or);
	
		}
			
		FindIterable<Document> documents = mc.find(query);
		for(Document d : documents) {
			MessageDB m = new MessageDB();
			m.setSenderId(d.getString("sender"));
			m.setReceiverUsers((List<String>) d.get("receiverUsers"));
			m.setContent(d.getString("content"));
			m.setSendingTime(d.getDate("sentDate"));
			m.setGroupName(d.getString("groupName"));
			m.setGroupId(d.getInteger("groupId", -1));
			retVal.add(m);
		}
		
		
		return retVal;	
	}

	public void chat(MessageDB m) throws ParseException {
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Messages");
						
			Document newMessage = new Document()
					.append("sender", m.getSenderId())
					.append("content", m.getContent())
					.append("sentDate", m.getSendingTime())
					.append("receiverUsers", m.getReceiverUsers());
			
			if(m.getGroupId()!=-1) {
				newMessage.append("groupId", m.getGroupId());
			}
			if(m.getGroupName()!=null) {
				newMessage.append("groupName", m.getGroupName());
			}
			
			mc.insertOne(newMessage);
		
	}
	
	private boolean checkifParticipantExists(ArrayList<ParticipantInfo> info, ParticipantInfo p) {
		
		for(ParticipantInfo pi : info) {
			boolean equals1 = true;
			if(pi.getGroupParticipants()==null && p.getGroupParticipants()==null) {
				equals1 = true;
			}else if(pi.getGroupParticipants()==null || p.getGroupParticipants()==null) {
				equals1 = false;
			}else if(pi.getGroupParticipants().size()!=p.getGroupParticipants().size()) {
				equals1 = false;
			}else {
				for(String gp : pi.getGroupParticipants()) {
					if(!p.getGroupParticipants().contains(gp)) {
						equals1 = false;
						break;
					}
				}		
			}
			
			boolean equals2 = false;
			if(pi.getParticipant()==null && p.getParticipant()==null) {
				equals2 = true;
			}else if(pi.getParticipant()==null || p.getParticipant()==null) {
				equals2 = false;
			}else if(pi.getParticipant().equals(p.getParticipant())) {
				equals2 = true;
			}
			
			boolean equals3 = false;
			if(pi.getGroupName()==null && p.getGroupName()==null) {
				equals3 = true;
			}else if(pi.getGroupName()==null || p.getGroupName()==null) {
				equals3 = false;
			}else if(pi.getGroupName().equals(p.getGroupName())) {
				equals3 = true;
			}
			
			boolean equals4 = false;
			if(pi.getGroupId()==-1 && p.getGroupId()==-1) {
				equals4 = true;
			}else if(pi.getGroupId()==-1 || p.getGroupId()==-1) {
				equals4 = false;
			}else if(pi.getGroupId()==p.getGroupId()) {
				equals4 = true;
			}
			
			
			if(equals1 && equals2 && equals3 && equals4) {
				return true;
			}
				
		}
		
		return false;
	}
	
	public HashMap<String, User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(HashMap<String, User> activeUsers) {
		this.activeUsers = activeUsers;
	}
	
}
