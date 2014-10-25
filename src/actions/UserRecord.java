package actions;

public class UserRecord {
	/////constructor
	UserRecord(){
		
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRootList() {
		return rootList;
	}
	public void setRootList(String rootList) {
		this.rootList = rootList;
	}
	
	private String userID;
	private String userName;
	private String password ;
	private String rootList;
}
