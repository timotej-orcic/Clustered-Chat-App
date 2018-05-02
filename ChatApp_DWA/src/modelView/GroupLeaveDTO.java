package modelView;

public class GroupLeaveDTO {
	private String kickedBy;
	private int groupId;
	private String leaverUsername;
	
	public String getKickedBy() {
		return kickedBy;
	}
	public void setKickedBy(String kickedBy) {
		this.kickedBy = kickedBy;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getLeaverUsername() {
		return leaverUsername;
	}
	public void setLeaverUsername(String leaverUsername) {
		this.leaverUsername = leaverUsername;
	}
	
	
}
