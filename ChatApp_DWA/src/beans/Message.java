package beans;

public class Message {
	
	private String messageType;
	private String content;
	private String loggedUserName;
	
	public Message() {}
	
	public Message(String mt, String ct, String lu) {
		this.messageType = mt;
		this.content = ct;
		this.loggedUserName = lu;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLoggedUserName() {
		return loggedUserName;
	}

	public void setLoggedUserName(String loggedUserName) {
		this.loggedUserName = loggedUserName;
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", content=" + content + ", loggedUserName=" + loggedUserName
				+ "]";
	}


}
