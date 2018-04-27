package packages.application;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.mongodb.MongoClient;

@ApplicationPath("/userApp")
public class App extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		
		return null;
	}
}
