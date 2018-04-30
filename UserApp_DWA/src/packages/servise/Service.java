package packages.servise;

import javax.ejb.Singleton;
import javax.inject.Inject;

import packages.database.DatabaseConnectionProvider;

	@Singleton
	public class Service {

	@Inject
	private DatabaseConnectionProvider dbConnectionProvider;
	
	public void userLogin() {
		
	}
	
	public void userRegister() {
		
	}
	
}
