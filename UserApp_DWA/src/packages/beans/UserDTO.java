package packages.beans;

public class UserDTO {

	private String userName;
	private String name;
	private String lastName;
	
	public UserDTO() {}

	public UserDTO(String userName, String name, String lastName) {
		super();
		this.userName = userName;
		this.name = name;
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

}
