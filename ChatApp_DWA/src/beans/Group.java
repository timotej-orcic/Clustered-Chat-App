package beans;

import java.util.ArrayList;
import java.util.List;

public class Group {

	//UID
	private int groupId;
	private String groupName;
	private String parentUserId;
	private List<User> groupMembersList;
	
	public Group() {
		groupId = 0;
		groupName = "";
		parentUserId = "";
		groupMembersList = new ArrayList<User>();
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	public List<User> getGroupMembersList() {
		return groupMembersList;
	}

	public void setGroupMembersList(ArrayList<User> groupMembersList) {
		this.groupMembersList = groupMembersList;
	}		
}
