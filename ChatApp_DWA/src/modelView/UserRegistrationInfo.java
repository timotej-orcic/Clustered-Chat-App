package modelView;

import beans.Host;

public class UserRegistrationInfo {
	private String userName;
	private String password;
	private String name;
	private String lastName;
	private Host host;
	
	public UserRegistrationInfo() {
		super();
	}
	public UserRegistrationInfo(String userName, String password, String name, String lastName, Host host) {
		super();
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.host = host;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Host getHost() {
		return host;
	}
	public void setHost(Host host) {
		this.host = host;
	}
	
}
