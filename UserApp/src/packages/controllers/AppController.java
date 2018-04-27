package packages.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bouncycastle.crypto.generators.BCrypt;
import org.bson.Document;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

import packages.beans.User;
import packages.database.DatabaseConnectionProvider;
import packages.modelView.UserInfo;
import packages.utils.PasswordUtils;

@Stateless
@Path("/users")
public class AppController {
	
	@Inject
	private DatabaseConnectionProvider mongoClient;
	
	private BCrypt passwordEncrypter;
	
	@POST
	@Path("/register")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public User register(User u) {
		MongoCollection<Document> collection = mongoClient.getDatabase().getCollection("Users");
		Document doc = new Document("name", "Users")
						.append("username", u.getUserName())
						.append("name", u.getName())
						.append("password", encryptPW(u.getPassword()))
						.append("lastname", u.getLastName());
		
		return null;
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String login(UserInfo ui) {			
		MongoCollection<Document> collection = mongoClient.getDatabase().getCollection("Users");
		String token = null;
		//Napraviti query koji vraca usera sa nekim username, nauciti kako xD
		//Napraviti token od podatak Usera, ne UserInfo-a
		try {
			token = JWT.create().withClaim("username", ui.getUsername())
					.withIssuedAt(new Date())
					.withSubject(ui.getUsername())
					.sign(Algorithm.HMAC384("secret"));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JWTCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return token;
	}

	private String encryptPW(String password) {
		String salt = PasswordUtils.getSalt(50);
		return PasswordUtils.generateSecurePW(password, salt);
	}
	
	
}
