package beans;

import java.util.ArrayList;


public class User {

	//UID
	private String userName;
	private String password;
	private String name;
	private String lastName;
	private Host currentHost;
	private ArrayList<User> myFriendsList;
	
	
	public User(String userName, String password, String name, String lastName, Host currentHost) {
		super();
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.currentHost = currentHost;
	}

	public User () {
		userName = "";
		password = "";
		name = "";
		lastName = "";
		currentHost = null;
		myFriendsList = new ArrayList<User>();
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

	public Host getCurrentHost() {
		return currentHost;
	}

	public void setCurrentHost(Host currentHost) {
		this.currentHost = currentHost;
	}


	public ArrayList<User> getMyFriendsList() {
		return myFriendsList;
	}

	public void setMyFriendsList(ArrayList<User> myFriendsList) {
		this.myFriendsList = myFriendsList;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", name=" + name + ", lastName=" + lastName
				+ "]";
	}	

}
