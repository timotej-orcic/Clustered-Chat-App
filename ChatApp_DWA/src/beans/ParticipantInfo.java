package beans;

import java.util.List;

public class ParticipantInfo {

	private String participant;
	private List<String> groupParticipants;
	private int groupId;
	private String groupName;
	
	public ParticipantInfo(String participant, List<String> groupParticipants, int groupId, String groupName) {
		super();
		this.participant = participant;
		this.groupParticipants = groupParticipants;
		this.groupId = groupId;
		this.groupName = groupName;
	}

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public List<String> getGroupParticipants() {
		return groupParticipants;
	}

	public void setGroupParticipants(List<String> groupParticipants) {
		this.groupParticipants = groupParticipants;
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
}
