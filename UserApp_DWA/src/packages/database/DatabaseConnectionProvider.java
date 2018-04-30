package packages.database;

import java.io.Serializable;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

@Singleton
@ApplicationScoped
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DatabaseConnectionProvider implements Serializable {

	private static final long serialVersionUID = 1L;
	private MongoClient mongoClient;
	
	@Lock(LockType.READ)
	public MongoDatabase getDatabase() {
		MongoDatabase database = mongoClient.getDatabase("ChatApp");
		return database;
	}
	
	@PostConstruct
	public void init() throws UnknownHostException {
		MongoClientURI uri = new MongoClientURI("mongodb+srv://riki:riki5bogotac.2@chatapp-rb95b.mongodb.net/test");
		mongoClient = new MongoClient(uri);
	}
}
