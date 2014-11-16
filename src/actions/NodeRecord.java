package actions;

public class NodeRecord {
	/////constructor
	public NodeRecord(){
		
	}
		
	public NodeRecord(NodeRecord nodeRecord) {
		// TODO Auto-generated constructor stub
		key = nodeRecord.getKey();
		userID = new String(nodeRecord.getUserID());
		son = new String(nodeRecord.getSon());
		setFather(nodeRecord.getFather());
		name = new String(nodeRecord.getName());
		gender = nodeRecord.getGender();
		setBirthDate(nodeRecord.getBirthDate());
		profession = new String(nodeRecord.getProfession());
		institution = new String(nodeRecord.getInstitution());
		link = new String(nodeRecord.getLink());
		Bio = nodeRecord.getBio();
	}
	
	public NodeRecord ( CharDesc cd){
		key = null;
		userID = null;
		son = null;
		setFather(null);
		name = new String(cd.name);
		gender = cd.gender;
		setBirthDate(cd.birthDate);
		profession = new String(cd.profession);
		institution = new String(cd.institution);
		link = new String(cd.link);
		Bio = cd.bio;
	}
	
	public NodeRecord( String hash, String id, String decs, String asc, String name, String gender, String birth, 
					   String pro, String ins, String link, String bio){
		setKey(hash);
		setUserID(id);
		setSon(decs);
		setFather(asc);
		setName(name);
		setGender(gender);
		setBirthDate(birth);
		setProfession(pro);
		setInstitution(ins);
		setLink(link);
		setBio(bio);
	}
	
	/////public methods
	public void clear(){
		key = STRING_INVALID;
		userID = STRING_INVALID;
		father = STRING_INVALID;
		son = STRING_INVALID;
		name = STRING_INVALID;
		setBirthDate(STRING_INVALID);
		profession = STRING_INVALID;
		institution = STRING_INVALID;
		link = STRING_INVALID;
	}
	
	public static int INT_INVALID = -1;
	public static String STRING_INVALID = null;
	
	private String key;
	private String userID;
	private String father;
	private String son;
	private String name;
	private String gender;
	private String birthDate;
	private String profession;
	private String institution;
	private String link;//this suppose to be an valid URL
	private String Bio;
	
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBio() {
		return Bio;
	}

	public void setBio(String bio) {
		Bio = bio;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

}
