package packages.beans;

import java.util.ArrayList;
import java.util.Date;

public class Message {

	private int messageId;
	private String senderId;
	private ArrayList<String> receiverIdsList;
	private Date sendingTime;
	private String content;
	
	public Message() {
		//Konstruktor mora biti prazan zbog resta
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public ArrayList<String> getReceiverIdsList() {
		return receiverIdsList;
	}

	public void setReceiverIdsList(ArrayList<String> receiverIdsList) {
		this.receiverIdsList = receiverIdsList;
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
