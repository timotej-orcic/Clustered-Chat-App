package beans;

import java.util.ArrayList;
import java.util.Date;

public class MessageDB {

	private String senderId;
	private String receiverUser;
	private Date sendingTime;
	private String content;
	
	public MessageDB() {
		senderId = "";
		receiverUser = "";
		sendingTime = null;
		content = "";
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}


	public String getReceiverUser() {
		return receiverUser;
	}

	public void setReceiverUser(String receiverUser) {
		this.receiverUser = receiverUser;
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
}
