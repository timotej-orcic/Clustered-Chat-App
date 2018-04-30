package packages.servise;

import javax.ejb.Singleton;
import javax.inject.Inject;

import org.bson.Document;
import org.bson.json.JsonParseException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Sorts;

import java.io.IOException;
import java.util.Arrays;
import org.bson.Document;

import packages.application.App;
import packages.beans.Host;
import packages.beans.User;
import packages.database.DatabaseConnectionProvider;
import packages.modelView.UserRegistrationInfo;

	@Singleton
	public class Service {

	@Inject
	private DatabaseConnectionProvider dbConnectionProvider;
	private ObjectMapper objMapper = new ObjectMapper();
	
	public void userLogin() {
		
	}
	
	public boolean userRegister(UserRegistrationInfo userInfo) throws JsonParseException, JsonMappingException, IOException {
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
	
	
}
