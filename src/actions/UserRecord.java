package actions;

public class UserRecord {
	/////constructor
	UserRecord(){
		
	}
	
	
	//clean
	public void clear(){
		userID = STRING_INVALID;
		userName = STRING_INVALID;;
		password = STRING_INVALID;
		rootList = STRING_INVALID;
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
	
	public static String STRING_INVALID = "end of the line";
	
	private String userID;
	private String userName;
	private String password ;
	private String rootList;
}
