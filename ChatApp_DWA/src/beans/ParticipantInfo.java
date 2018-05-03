package beans;

public class ParticipantInfo {

	private String participantId;
	private boolean isGroup;
	
	public ParticipantInfo(String pId, boolean isGr) {
		this.participantId = pId;
		this.isGroup = isGr;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}		
}
