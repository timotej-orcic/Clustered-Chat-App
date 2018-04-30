package packages.servise;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import packages.beans.LoginData;
import packages.beans.User;
import packages.database.DatabaseConnectionProvider;

	@Singleton
	public class Service {

	@Inject
	private DatabaseConnectionProvider dbConnectionProvider;
	
	private HashMap<String,User> activeUsers;
	
	@PostConstruct
	public void init() {
		activeUsers = new HashMap<String,User>();
	}
	
	public String userLogin(LoginData logData) {
		
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
		
		return loggedUser.getUserName();
	}
	
	public void userRegister() {
		
	}
	
}
