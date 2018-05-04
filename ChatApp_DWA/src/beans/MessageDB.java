package beans;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class MessageDB {

	private String senderId;
	private List<String> receiverUsers;
	private Date sendingTime;
	private String content;
	private String groupName;
	private int groupId;
	
	public MessageDB() {
		senderId = "";
		receiverUsers = new ArrayList<String>();
		sendingTime = null;
		content = "";
		groupName = null;
		groupId = -1;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public Date getSendingTime() {
		return sendingTime;
	}

	public void setSendingTime(Date sendingTime) {
		this.sendingTime = sendingTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getReceiverUsers() {
		return receiverUsers;
	}

	public void setReceiverUsers(List<String> receiverUsers) {
		this.receiverUsers = receiverUsers;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}	
}
