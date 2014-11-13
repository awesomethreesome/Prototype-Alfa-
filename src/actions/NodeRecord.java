package actions;

public class NodeRecord {
	/////constructor
	NodeRecord(){
		
	}
		
	public NodeRecord(NodeRecord nodeRecord) {
		// TODO Auto-generated constructor stub
		key = nodeRecord.getKey();
		userID = new String(nodeRecord.getUserID());
		son = new String(nodeRecord.getSon());
		name = new String(nodeRecord.getName());
		age = nodeRecord.getAge();
		profession = new String(nodeRecord.getProfession());
		institution = new String(nodeRecord.getInstitution());
		link = new String(nodeRecord.getLink());
	}

	public void clear(){
		key = STRING_INVALID;
		userID = STRING_INVALID;
		father = STRING_INVALID;
		son = STRING_INVALID;
		name = STRING_INVALID;
		age = STRING_INVALID;
		profession = STRING_INVALID;
		institution = STRING_INVALID;
		link = STRING_INVALID;
	}
	
	public static int INT_INVALID = -1;
	public static String STRING_INVALID = "";
	
	private String key;
	private String userID;
	private String father;
	private String son;
	private String name;
	private String age;
	private String profession;
	private String institution;
	private String link;//this suppose to be an valid URL
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getSon() {
		return son;
	}

	public void setSon(String son) {
		this.son = son;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

}
