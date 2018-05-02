package packages.modelView;

import java.util.ArrayList;

import packages.beans.User;

public class GroupDTO {
	private int groupId;
	private String groupName;
	private String parentUserId;
	private ArrayList<String> groupMembersList;
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
	public ArrayList<String> getGroupMembersList() {
		return groupMembersList;
	}
	public void setGroupMembersList(ArrayList<String> groupMembersList) {
		this.groupMembersList = groupMembersList;
	}
	
	
}
