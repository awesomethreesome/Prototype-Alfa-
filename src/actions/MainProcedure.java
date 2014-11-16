package actions;
/**
 * @author Ruogu.Gao
 */
import java.util.*;
import java.sql.*;

import com.opensymphony.xwork2.ActionSupport;

public class MainProcedure extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main( String[] args ) {
		MainProcedure test_case = new MainProcedure();
	}
	//empty constructor
	public MainProcedure(){
		//System.out.println("create an instance");
	}
	/////public methods 
	public void logInCheck( String inputUserName, String inputPassword ) throws SQLException{//name password 
		System.out.println(inputPassword + " " + inputUserName);
		
		ResultSet temp = dataBase.selectUser( inputUserName ,  inputPassword );
		if ( temp == null ) {
			authorized = false;
			return;
		}
		transcribeUser( temp );//current user info has be loaded into currentUser
		authorized = true;
		System.out.println("currentUser info: "+currentUser.getUserID() + " "+ currentUser.getRootList());
	}
	
	public void logOut(){
		authorized = false;
	}
	
	public ArrayList<ListItem> search( String queryInput, String queryType ) throws SQLException{
		//queryType has 3 possible values{Name, Institution, Profession, }
		ResultSet temp = null;
		if ( queryType.equals("Institution") ) {
			temp = dataBase.selectNodebyIns(queryInput, true);//search with ambiguity
		}
		else if ( queryType.equals("Profession") ) {
			temp = dataBase.selectNodebyPro(queryInput, true);
		}
		else {//default option is search by name
			temp = dataBase.selectNodebyName(queryInput, true);
		}
		transcribeSearchResult( temp );//now all possible node have been stored in searchList, which is a list of ListItem
		return searchList;
	}
	
	public NodeRecord detail( String Hash ) {
		NodeRecord temp = new NodeRecord();
		temp.setKey(STRING_INVALID);
		for (int i=0; i<currentBuffer.size(); i++ ){
			if ( currentBuffer.get(i).getKey() == Hash )
				break;
		}
		
		return temp;
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	// two inputs are all hash code
	public boolean link(String teacher, String student) {
		if ( !authorityCheck() )
			return false;
		int teacherIndex = searchCurrentBuffer(teacher), studentIndex = searchCurrentBuffer(student);
		if ( teacherIndex == INT_INVALID || studentIndex == INT_INVALID ){//not found
			return false;
		}
		NodeRecord teacherRecord = new NodeRecord( null, null, null, null, null, null, null, null, null, null, null );
		NodeRecord studentRecord = new NodeRecord( null, null, null, null, null, null, null, null, null, null, null );
		//set new relation link
		String newSon = currentBuffer.get(teacherIndex).getSon() + student;
		String newFather = currentBuffer.get(studentIndex).getFather() + teacher; 
		teacherRecord.setSon(newSon);
		studentRecord.setFather(newFather);
		String modification = "link" +teacherRecord.getName()+ "as" +studentRecord.getName()+ "'s teacher";
		history.pushBack(currentBuffer, modification);
		currentBuffer.updateRecord(teacher, teacherRecord);
		currentBuffer.updateRecord(student, studentRecord);
		return true;
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	public boolean sever(String teacher, String student) {
		if ( !authorityCheck() )
			return false;
		if ( !authorityCheck() )
			return false;
		int teacherIndex = searchCurrentBuffer(teacher), studentIndex = searchCurrentBuffer(student);
		if ( teacherIndex == INT_INVALID || studentIndex == INT_INVALID ){//not found
			return false;
		}
		NodeRecord teacherRecord = new NodeRecord( null, null, null, null, null, null, null, null, null, null, null );
		NodeRecord studentRecord = new NodeRecord( null, null, null, null, null, null, null, null, null, null, null );
		//set new relation link
		String newSon = currentBuffer.get(teacherIndex).getSon().replaceAll(student, "");
		String newFather = currentBuffer.get(studentIndex).getFather().replaceAll(teacher, ""); 
		teacherRecord.setSon(newSon);
		studentRecord.setFather(newFather);
		String modification = "sever the link between" +teacherRecord.getName()+ "and" +studentRecord.getName();
		history.pushBack(currentBuffer, modification);
		currentBuffer.updateRecord(teacher, teacherRecord);
		currentBuffer.updateRecord(student, studentRecord);
		return true;
	}
	
	//returns the hash of newly-added
	//refresh historyList
	public String add( String name, String gender, String birthDate, String son, String father, 
			           String pro, String ins, String link, String bio ) {
		if ( !authorityCheck() )
			return "";	
		NodeRecord temp = new NodeRecord( hashGenerator.generateHash(),currentUser.getUserID(), son, father, 
										  name, gender,birthDate, pro, ins, link, bio );
		String modification = "Add node: " + temp.getName();
		history.pushBack(currentBuffer, modification);
		currentBuffer.addRecord(temp);
		return temp.getKey();
	}
	
	public boolean delete( String Hash ) {
		if ( !authorityCheck() )
			return false;
		//search the node in currentBuffer
		int index = searchCurrentBuffer(Hash); 
		if (  index != INT_INVALID ){
			//record modification
			String modification = "delete node: " + currentBuffer.get(index).getName();
			history.pushBack(currentBuffer, modification);
			currentBuffer.deleteRecord(Hash);
			return true;
		}
		return false;
	}
	
	//returns null when not found
	//refresh neighborLists and directedWeb
	public CharDesc get(String hash) {/////////////////////////////没有就要先拉到缓冲区，于是缓冲区要加一个单纯加点的函数·1
		int index = searchCurrentBuffer(hash);
		if ( index == INT_INVALID )
			return null;
		NodeRecord centralNode = loadCurrentBuffer(hash);
		history.clear();///////////////////////////////////////////////need an initial push?
		transcribeCurrentBuffer();
		return new CharDesc(centralNode);
	}
	
	//refresh historyList
	public void edit(CharDesc target) {
		if ( !authorityCheck() )
			return;
		NodeRecord target0 = new NodeRecord( target );
		if ( INT_INVALID == searchCurrentBuffer(target0.getKey()) )//not found
			return;
		String modification = "edit node: " + target0.getName();
		history.pushBack(currentBuffer, modification);
		currentBuffer.updateRecord(target0.getKey(), target0);
	}
	
	public void back2History(String back) {
		int index = Integer.parseInt(back);
		currentBuffer = history.pop(index);
	}
	
	//save changes to database
	public void syncDB() {////////////////////////////////////////////////////////////////////
		//
		for (int i=0; i<currentBuffer.size(); i++){
			
		}
		
		//check whether there're delete operated
		for (int j=0; j<history){
			
		}
		
		
		//clear history
		history.clear();
	}
	
	/////private methods
	/**
	 * 
	 * @param sr
	 * @throws SQLException
	 * Description:
	 * 1.transcribe the search result into searchList(arraylist<listitem>)
	 */
	private void transcribeSearchResult( ResultSet sr ) throws SQLException {
		//ArrayList<NodeRecord> currentResult = new ArrayList<NodeRecord>();
		//NodeRecord temp = null;
		searchList.clear();
		ListItem temp = new ListItem();
		temp = null;
		while ( sr.next() ) {
			/*
			temp = new NodeRecord();
			temp.setKey(sr.getString(1));
			temp.setUserID(sr.getString(2));
			temp.setFather(sr.getString(3));
			temp.setSon(sr.getString(4));
			temp.setName(sr.getString(5));
			temp.setGender(sr.getString(6));
			temp.setBirthDate(sr.getString(7));
			temp.setProfession(sr.getString(8));
			temp.setInstitution(sr.getString(9));
			temp.setLink(sr.getString(10));
			temp.setBio(sr.getString(11));
			*/
			temp = new ListItem();
			
			//currentResult.add(temp);
			searchList.add(temp);
			temp = null;
		}
		sr.beforeFirst();
		
		//currentBuffer.initialize(currentResult);
	}
	
	private void transcribeUser( ResultSet sr ) throws SQLException{
		currentUser.clear();
		while ( sr.next() ) {
			 currentUser.setUserID(sr.getString(1));
			 currentUser.setUserName(sr.getString(2));
			 currentUser.setPassword(sr.getString(3));
			 currentUser.setRootList(sr.getString(4));
		}
		sr.beforeFirst();
	}
	
	private boolean authorityCheck(){
		return authorized;
	}
	
	private int searchCurrentBuffer( String hash ){
		for ( int i=0; i<currentBuffer.size(); i++ ){
			if ( currentBuffer.get(i).getKey() == hash ){
				return i;
			}
		}
		return INT_INVALID;
	}
	
	/**
	 * 
	 * @param target
	 * description:
	 * 1.load network into currentBuffer from DB 
	 * 2.the net work consists of nodes who distance 3 unit from the central node
	 * 3.return the central node   
	 */
	private NodeRecord loadCurrentBuffer(  String hash  ){////////////////////////////////////
		
		
	}
	
	/**
	 * 
	 * @param target
	 * update neighborListx and directedWeb with currentBuffer
	 */
	private void transcribeCurrentBuffer(){///////////////////////////////////////////////////
		
	}
	
	/////public constant
	public static int INT_INVALID = -1;
	public static double DOUBLE_INVALID = -1.0;
	public static String STRING_INVALID = "end of the line";
	public static int ROOT_STAGE = -1;
	public static String SEARCHING_STAGE = "on searching";
	
	/////public variable
	public boolean authorized = false;
	public ArrayList<ListItem> searchList = new ArrayList<ListItem>();
	public ArrayList<ListItem> historyList = new ArrayList<ListItem>();;
	public ArrayList<ListItem> neighborList1 = new ArrayList<ListItem>();;
	public ArrayList<ListItem> neighborList2 = new ArrayList<ListItem>();;
	public ArrayList<ListItem> neighborList3 = new ArrayList<ListItem>();;
	public ArrayList<DAG> directedWeb = new ArrayList<DAG>();;
	
	/////private variables
	private DBManager dataBase = new DBManager();//
	private final String passWord = "vorstellung";
	private final String userName = "wille"; 
	private static String inputPassword = "vorstellung", inputUserName = "wille";
	
	private UserRecord currentUser = new UserRecord() ;//current user
	private EditBuffer currentBuffer = new EditBuffer();
	private HistoryList history = new HistoryList();
	private static NodeHash hashGenerator = new NodeHash();
	
	
	/////all setters and getters	
	public long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserRecord getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(UserRecord currentUser) {
		this.currentUser = currentUser;
	}
	public String getInputPassword() {
		return inputPassword;
	}

	public void setInputPassword(String inputPassword) {
		MainProcedure.inputPassword = inputPassword;
	}

	public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		MainProcedure.inputUserName = inputUserName;
	}

}
