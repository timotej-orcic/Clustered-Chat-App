package packages.beans;

import java.util.ArrayList;

import javax.persistence.Id;

public class User {

	//UID
	private String userName;
	private String password;
	private String name;
	private String lastName;
	private Host currentHost;
	private ArrayList<User> friendRequestsList;
	private ArrayList<User> myFriendsList;
	
	public User () {
		//Konstruktor mora biti prazan zbog resta
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

	public ArrayList<User> getFriendRequestsList() {
		return friendRequestsList;
	}

	public void setFriendRequestsList(ArrayList<User> friendRequestsList) {
		this.friendRequestsList = friendRequestsList;
	}

	public ArrayList<User> getMyFriendsList() {
		return myFriendsList;
	}

	public void setMyFriendsList(ArrayList<User> myFriendsList) {
		this.myFriendsList = myFriendsList;
	}	
}
