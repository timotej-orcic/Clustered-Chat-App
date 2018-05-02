package packages.modelView;

import java.util.ArrayList;
import java.util.List;

import packages.beans.User;

public class GroupDTO {
	private int groupId;
	private String groupName;
	private String parentUserId;
	private List<String> groupMembersList;
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
	public List<String> getGroupMembersList() {
		return groupMembersList;
	}
	public void setGroupMembersList(List<String> groupMembersList) {
		this.groupMembersList = groupMembersList;
	}
	
	
}
