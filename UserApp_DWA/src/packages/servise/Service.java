package packages.servise;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import packages.beans.LoginData;
import packages.beans.User;
import packages.database.DatabaseConnectionProvider;
import org.bson.json.JsonParseException;
import org.json.simple.JSONArray;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.bson.Document;

import packages.application.App;
import packages.beans.Group;
import packages.beans.Host;
import packages.beans.UserDTO;
import packages.database.DatabaseConnectionProvider;
import packages.modelView.GroupDTO;
import packages.modelView.GroupLeaveDTO;
import packages.modelView.UserRegistrationInfo;

@Singleton
public class Service {

	@Inject
	private DatabaseConnectionProvider dbConnectionProvider;
	
	private HashMap<String,User> activeUsers;
	
	private final String GROUP_COLLECTION_NAME = "Groups";
	private final String USERS_COLLECTION_NAME = "Users";
	private final String MESSAGES_COLLECTION_NAME = "Messages";
	private final String COUNTERS_COLLECTION_NAME = "Counters";
	
	@PostConstruct
	public void init() {
		activeUsers = new HashMap<String,User>();
	}
	
	public User userLogin(LoginData logData) {
		
		MongoDatabase md = dbConnectionProvider.getDatabase();
		MongoCollection<Document> mc = md.getCollection("Users");
		BasicDBObject criteria = new BasicDBObject();
		criteria.append("userName", logData.getUname());
		criteria.append("password", logData.getPassword());
		
		User loggedUser = null;
		FindIterable<Document> documents = mc.find(criteria);
		for(Document doc : documents) {
			loggedUser = new User(doc.getString("userName"),doc.getString("password"),doc.getString("name"),doc.getString("lastName"),null);	
		}
			
		if(loggedUser==null) {
			return null;
		}
		
		activeUsers.put(loggedUser.getUserName(), loggedUser);
		
		return loggedUser;
	}
		
	public boolean userRegister(UserRegistrationInfo userInfo) throws JsonParseException, IOException {
		boolean ret = false;
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", userInfo.getUserName())).first();
		
		if(userDoc == null) {
			Document newUser = new Document()
									.append("userName", userInfo.getUserName())
									.append("name", userInfo.getName())
									.append("password", userInfo.getPassword())
									.append("lastName", userInfo.getLastName());

			System.out.println(userInfo.getHost().getAlias() + " : " + userInfo.getHost().getHostAddress());
			userCollection.insertOne(newUser);
			ret = true;
		}
		
		return ret;
	}
	
	public ArrayList<UserDTO> getFriends(String loggedUserName){
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", loggedUserName)).first();
		
		ArrayList<UserDTO> retVal = new ArrayList<UserDTO>();
		
		if(!userDoc.containsKey("myFriendsList")) {
			return retVal;		
		}
		
		List<Document> list = (List<Document>) userDoc.get("myFriendsList");
		for(Document doc : list) {
			UserDTO userDTO = new UserDTO(doc.getString("userName"), doc.getString("name"), doc.getString("lastName"));
			retVal.add(userDTO);
		}
		
		
		return retVal;
	}
	
	public ArrayList<UserDTO> getNonFriends(String loggedUserName){
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", loggedUserName)).first();
		FindIterable<Document> users = userCollection.find();
		
		ArrayList<UserDTO> retVal = new ArrayList<UserDTO>();
		
		if(!userDoc.containsKey("myFriendsList")) {
			
			for(Document d : users) {
				if(!d.getString("userName").equals(loggedUserName)) {
					retVal.add(new UserDTO(d.getString("userName"),d.getString("name"),d.getString("lastName")));
				}		
			}
			return retVal;
		}
		
		List<Document> list = (List<Document>) userDoc.get("myFriendsList");
		
		for(Document d : users) {
			
			boolean found = false;
			
			if(d.getString("userName").equals(loggedUserName))
				continue;
			
			for(Document doc : list) {
				if(doc.getString("userName").equals(d.getString("userName"))) {
					found = true;
					break;
				}
			}
			
			if(!found) {
				retVal.add(new UserDTO(d.getString("userName"),d.getString("name"),d.getString("lastName")));
			}
			
		}
		
		return retVal;
	}
	
	
	public String deleteFriend(String loggedUserName, String toDelete) {
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", loggedUserName)).first();
		Document friendDoc = userCollection.find(eq("userName", toDelete)).first();
		
		if(!userDoc.containsKey("myFriendsList") || !friendDoc.containsKey("myFriendsList")) {
			return null;		
		}
		
		BasicDBObject userMatch = new BasicDBObject("userName", loggedUserName);
		BasicDBObject deleteFriend = new BasicDBObject("myFriendsList", new BasicDBObject("userName", toDelete));
		userCollection.updateOne(userMatch,new BasicDBObject("$pull", deleteFriend));
		
		BasicDBObject friendMatch = new BasicDBObject("userName", toDelete);
		BasicDBObject deleteUser = new BasicDBObject("myFriendsList", new BasicDBObject("userName", loggedUserName));
		userCollection.updateOne(friendMatch,new BasicDBObject("$pull", deleteUser));
			
		return toDelete;
	}
	
	public String addFriend(String loggedUserName, String toAdd) {
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", loggedUserName)).first();
		Document friendDoc = userCollection.find(eq("userName", toAdd)).first();
		
		if(userDoc==null || friendDoc==null) {
			return null;
		}

		BasicDBObject userMatch = new BasicDBObject("userName", loggedUserName);
		BasicDBObject friendToAdd = new BasicDBObject();
		friendToAdd.put("userName", friendDoc.get("userName"));
		friendToAdd.put("name",friendDoc.get("name"));
		friendToAdd.put("lastName", friendDoc.get("lastName"));
		userCollection.updateOne(userMatch,new BasicDBObject("$push", new BasicDBObject("myFriendsList", friendToAdd)));
		
		BasicDBObject friendMatch = new BasicDBObject("userName", toAdd);
		BasicDBObject userToAdd = new BasicDBObject();
		userToAdd.put("userName", userDoc.get("userName"));
		userToAdd.put("name",userDoc.get("name"));
		userToAdd.put("lastName", userDoc.get("lastName"));
		
		userCollection.updateOne(friendMatch,new BasicDBObject("$push", new BasicDBObject("myFriendsList", userToAdd)));
		
		return toAdd;
	}
	
	public ArrayList<UserDTO> searchFriends(String loggedUserName, String userName, String name, String lastName){
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", loggedUserName)).first();
		
		ArrayList<UserDTO> retVal = new ArrayList<UserDTO>();
		
		if(!userDoc.containsKey("myFriendsList")) {
			return retVal;		
		}
		
		
		boolean searchUName = true;
		boolean searchName = true;
		boolean searchLName = true;
		
		List<Document> list = (List<Document>) userDoc.get("myFriendsList");
		for(Document doc : list) {
			
			String lCaseUname = doc.getString("userName").toLowerCase();
			String lCaseName = doc.getString("name").toLowerCase();
			String lCaseLname = doc.getString("lastName").toLowerCase();		
			
			if(!userName.isEmpty()) {
				searchUName = lCaseUname.equals(userName.toLowerCase()) || lCaseUname.startsWith(userName.toLowerCase());
			}
			
			if(!name.isEmpty()) {
				searchName = lCaseName.equals(name.toLowerCase()) || lCaseName.startsWith(name.toLowerCase());
			}
			
			if(!lastName.isEmpty()) {
				searchLName = lCaseLname.equals(lastName.toLowerCase()) || lCaseLname.startsWith(lastName.toLowerCase());
			}
			
			if(searchUName && searchName && searchLName) {
				UserDTO userDTO = new UserDTO(doc.getString("userName"), doc.getString("name"), doc.getString("lastName"));
				retVal.add(userDTO);
			}

		}
			
		return retVal;
	}
	
	public ArrayList<UserDTO> searchNonFriends(String loggedUserName, String userName, String name, String lastName){
		
		MongoCollection<Document> userCollection = dbConnectionProvider.getDatabase().getCollection("Users");
		Document userDoc = userCollection.find(eq("userName", loggedUserName)).first();
		FindIterable<Document> users = userCollection.find();
		
		ArrayList<UserDTO> retVal = new ArrayList<UserDTO>();
		
		if(!userDoc.containsKey("myFriendsList")) {
			
			for(Document d : users) {
				if(!d.getString("userName").equals(loggedUserName)) {
					retVal.add(new UserDTO(d.getString("userName"),d.getString("name"),d.getString("lastName")));
				}		
			}
			return retVal;
		}
		
		List<Document> list = (List<Document>) userDoc.get("myFriendsList");
		
		ArrayList<UserDTO> tempList = new ArrayList<UserDTO>();
		
		for(Document d : users) {
			
			boolean found = false;
			
			if(d.getString("userName").equals(loggedUserName))
				continue;
			
			for(Document doc : list) {
				if(doc.getString("userName").equals(d.getString("userName"))) {
					found = true;
					break;
				}
			}
			
			if(!found) {
				tempList.add(new UserDTO(d.getString("userName"),d.getString("name"),d.getString("lastName")));
			}
			
		}
		
		boolean searchUName = true;
		boolean searchName = true;
		boolean searchLName = true;
		
		for(UserDTO u : tempList) {
			
			String lCaseUname = u.getUserName().toLowerCase();
			String lCaseName = u.getName().toLowerCase();
			String lCaseLname = u.getLastName().toLowerCase();		
			
			if(!userName.isEmpty()) {
				searchUName = lCaseUname.equals(userName.toLowerCase()) || lCaseUname.startsWith(userName.toLowerCase());
			}
			
			if(!name.isEmpty()) {
				searchName = lCaseName.equals(name.toLowerCase()) || lCaseName.startsWith(name.toLowerCase());
			}
			
			if(!lastName.isEmpty()) {
				searchLName = lCaseLname.equals(lastName.toLowerCase()) || lCaseLname.startsWith(lastName.toLowerCase());
			}
			
			if(searchUName && searchName && searchLName) {
				retVal.add(u);
			}
			
		}		
		
		return retVal;
	}
	
	public List<GroupDTO> getGroups(String userName) {
		List<GroupDTO> ret = new ArrayList<GroupDTO>();
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(this.GROUP_COLLECTION_NAME);
		
		FindIterable<Document> isParent = groups.find(eq("parentUserId", userName));
		for(Document d : isParent) {
			GroupDTO grp = createGroupFromDocument(d);
			ret.add(grp);
		}
		
		FindIterable<Document> filteredGroups = groups.find(in("groupMemberList", userName));
		for(Document d : filteredGroups) {
			GroupDTO grp = createGroupFromDocument(d);
			ret.add(grp);
		}
		
		return ret;
	}
	
	public GroupDTO createGroup(GroupDTO g) {
		GroupDTO ret = null;
		int grpId = getNextId(this.GROUP_COLLECTION_NAME);
		boolean success = false;
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(this.GROUP_COLLECTION_NAME); 
		Document newGroup = null;
		
		try {
			newGroup = new Document("_id", grpId)
					.append("groupName", g.getGroupName())
					.append("parentUserId", g.getParentUserId())
					.append("groupMemberList", g.getGroupMembersList());

			groups.insertOne(newGroup);
			success=true;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(success) {
				ret = createGroupFromDocument(newGroup);
			}
		}
		
		return ret;
	}
	
	public boolean deleteGroup(int groupId) {
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(GROUP_COLLECTION_NAME);
		DeleteResult result = groups.deleteOne(eq("_id", groupId));
		boolean succ = false;
		
		if(result != null) {
			succ = result.getDeletedCount() > 0;
		}
		
		return succ;
	}
	
	public int getNextId(String collectionName) {
		MongoCollection<Document> counters = dbConnectionProvider.getDatabase().getCollection("Counters");
		BasicDBObject find = new BasicDBObject();
		int ret = 0;
		
		find.put("_id", collectionName);
		BasicDBObject update = new BasicDBObject();
		update.put("$inc", new BasicDBObject("counter", 1));
		
		Document count = counters.findOneAndUpdate(find, update);
		ret = count.getInteger("counter");
		
		return ret;
	}
	
	public GroupDTO leaveGroup(GroupLeaveDTO leaveDto) {
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(GROUP_COLLECTION_NAME);
		Document group = groups.find(eq("_id", leaveDto.getGroupId())).first();
		GroupDTO ret = null;
		
		if(group != null) {
			ret = createGroupFromDocument(group);
			List<String> newArr = ret.getGroupMembersList().stream().filter(u ->{
									System.out.println(!u.equals(leaveDto.getLeaverUsername()));
									return !u.equals(leaveDto.getLeaverUsername());
								}).map(u -> u).collect(Collectors.toList());
			ret.setGroupMembersList(newArr);
			Document newDoc = new Document("_id", ret.getGroupId())
					.append("groupName", ret.getGroupName())
					.append("parentUserId", ret.getParentUserId())
					.append("groupMemberList", ret.getGroupMembersList());
			
			UpdateResult lol = groups.replaceOne(eq("_id", leaveDto.getGroupId()), newDoc);
			System.out.println(lol.getModifiedCount());
			System.out.println(lol.getMatchedCount());
		}
		
		return ret;
	}
	
	public GroupDTO addUserToGroup(String userToAdd, int groupId) {
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(GROUP_COLLECTION_NAME);
		Document group = groups.find(eq("_id", groupId)).first();
		GroupDTO ret = null;
		
		if(group != null) {
			BasicDBObject addToGrp = new BasicDBObject("groupMemberList", new BasicDBObject("userName", userToAdd));
			groups.updateOne(group, new BasicDBObject("$push", addToGrp));
			Document updated = groups.find(eq("_id", groupId)).first();
			
			ret = createGroupFromDocument(updated);
		}
		
		return ret;
	}
	
	public GroupDTO getOne(int groupId) {
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(GROUP_COLLECTION_NAME);
		Document group = groups.find(eq("_id", groupId)).first();
		GroupDTO ret = null;
		
		ret = createGroupFromDocument(group);
		
		return ret;
	}
	
	public GroupDTO createGroupFromDocument(Document group) {
		GroupDTO ret = null;
		
		if(group != null) {
			ret = new GroupDTO();
			ret.setGroupId(group.getInteger("_id"));
			ArrayList<String> objArr = (ArrayList<String>) group.get("groupMemberList");
			ArrayList<String> users = new ArrayList<String>();
			
			for(Object o : objArr) {
				users.add(o.toString());
			}
			
			ret.setGroupMembersList(users);
			ret.setGroupName(group.getString("groupName"));
			ret.setParentUserId(group.getString("parentUserId"));
		}
		
		return ret;
	}

	public List<GroupDTO> getAddedInto(String username) {
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(GROUP_COLLECTION_NAME);
		FindIterable<Document> filteredGroups = groups.find(in("groupMemberList", username));
		List<GroupDTO> list = new ArrayList<GroupDTO>();
		
		for(Document d : filteredGroups) {
			GroupDTO grp = createGroupFromDocument(d);
			list.add(grp);
		}
		
		return list;
	}

	public List<GroupDTO> getCreatedGroups(String username) {
		MongoCollection<Document> groups = dbConnectionProvider.getDatabase().getCollection(GROUP_COLLECTION_NAME);
		FindIterable<Document> filteredGroups = groups.find(eq("parentUserId", username));
		List<GroupDTO> list = new ArrayList<GroupDTO>();
		
		for(Document d : filteredGroups) {
			GroupDTO grp = createGroupFromDocument(d);
			list.add(grp);
		}
		
		return list;
	}
}
