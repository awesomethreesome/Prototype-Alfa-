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
	public boolean link(String teacher, String student) throws SQLException {
		if ( !authorityCheck() )
			return false;
		NodeRecord teacherSource = getSource( teacher );
		NodeRecord studentSource = getSource( student );
		if ( teacherSource == null || studentSource == null){//fail to find one or both node 
			return false;
		}
		
		NodeRecord teacherRecord = new NodeRecord( teacherSource );
		NodeRecord studentRecord = new NodeRecord( studentSource );
		//set new relation link
		String newSon = teacherRecord.getSon() + student;
		String newFather = studentRecord.getFather() + teacher; 
		teacherRecord.setSon(newSon);
		studentRecord.setFather(newFather);
		
		String modification = "link" +teacherRecord.getName()+ "as" +studentRecord.getName()+ "'s teacher";
		EditBuffer newBuffer = generateBuffer(teacherRecord, studentRecord);
		history.pushBack(newBuffer, modification);
		return true;
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	public boolean sever(String teacher, String student) throws SQLException {
		if ( !authorityCheck() )
			return false;
		NodeRecord teacherSource = getSource( teacher );
		NodeRecord studentSource = getSource( student );
		if ( teacherSource == null || studentSource == null){//fail to find one or both node 
			return false;
		}
		
		NodeRecord teacherRecord = new NodeRecord( teacherSource );
		NodeRecord studentRecord = new NodeRecord( studentSource );
		//set new relation link
		String newSon = teacherRecord.getSon().replaceAll(student, "");
		String newFather = studentRecord.getFather().replaceAll(teacher, ""); 
		teacherRecord.setSon(newSon);
		studentRecord.setFather(newFather);
		
		String modification = "sever " +teacherRecord.getName()+ "and" +studentRecord.getName();
		EditBuffer newBuffer = generateBuffer( teacherRecord, studentRecord );
		history.pushBack(newBuffer, modification);
		//currentBuffer.updateRecord(teacher, teacherRecord);
		//currentBuffer.updateRecord(student, studentRecord);
		return true;
	}
	
	//returns the hash of newly-added
	//refresh historyList
	public String add( String name, String gender, String birthDate, String son, String father, 
			           String pro, String ins, String link, String bio ) {
		if ( !authorityCheck() )//check authority
			return "";	
		//if there's no delete history and has record in DB, abandon operation
		int historyIndex = searchHistorybyName(name);
		ResultSet tempSet= searchDBbyName(name);
		if ( historyIndex == INT_INVALID && tempSet != null ){//no record in history but in DB
			return "FAILURE";
		}
		else if (historyIndex != INT_INVALID && tempSet != null){//in DB in history but not been deleted || not in DB, but was not deleted in history  
			String modRecord = history.ModRecord(historyIndex);
			if ( modRecord.contains("delete") == false)
				return "FAILURE";
		}
		
		NodeRecord temp = new NodeRecord( hashGenerator.generateHash(),currentUser.getUserID(), son, father, 
										  name, gender,birthDate, pro, ins, link, bio );
		String modification = "Add node: " + temp.getName();
		EditBuffer newBuffer = generateBuffer(temp, null);
		history.pushBack(newBuffer, modification);
		return temp.getKey();
	}
	
	public boolean delete( String Hash ) throws SQLException {
		if ( !authorityCheck() )
			return false;
		//if there's no add history and has no record in DB, abandon operation
		int historyIndex = searchHistory(Hash);
		ResultSet tempSet= searchDB(Hash);
		if ( historyIndex == INT_INVALID && tempSet == null ){//has no record in history or DB
			return false;
		}
		else if (historyIndex != INT_INVALID ){//in DB in history but has been deleted ||not in DB, but was deleted in history  
			String modRecord = history.ModRecord(historyIndex);
			if ( modRecord.contains("delete") == true)
				return false;
		}
		
		NodeRecord source = getSource(Hash);
		String modification = "delete node: " + source.getName();
		EditBuffer newBuffer = generateBuffer( new NodeRecord(source), null );
		history.pushBack(newBuffer, modification);
		return true;
	}
	
	//returns null when not found
	//refresh neighborLists and directedWeb
	public CharDesc get(String hash) {////////////////////////////////////////////////////////////////////////////////////////////////////////
		int index = searchCurrentBuffer(hash);
		if ( index == INT_INVALID )
			return null;
		NodeRecord centralNode = loadCurrentBuffer(hash);
		history.clear();///////////////////////////////////////////////need an initial push?
		transcribeCurrentBuffer();
		return new CharDesc(centralNode);
	}
	
	//refresh historyList
	public void edit(CharDesc target) throws SQLException {
		if ( !authorityCheck() )
			return;
		
		NodeRecord target0 = getSource( target.hash );
		if ( target0 == null )//not found
			return;
		String modification = "edit node: " + target0.getName();
		EditBuffer newBuffer = generateBuffer(target0, null);
		history.pushBack(newBuffer, modification);
		
	}
	
	public void back2History(String back) {
		int index = Integer.parseInt(back);
		history.pop(index);
	}
	
	//save changes to database
	public void syncDB() {////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
			temp = new ListItem();
			
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
	
	private NodeRecord transcribeSingleRecord( ResultSet sr) throws SQLException{
		NodeRecord temp = new NodeRecord();
		while ( sr.next() ){
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
		}
		sr.beforeFirst();
		return temp;
	}
	
	private ResultSet searchDB( String hash ){
		ResultSet temp = null;
		temp = dataBase.selectNodebyHash(hash);
		return temp;
	}
	
	private ResultSet searchDBbyName( String name ){
		ResultSet temp = null;
		temp = dataBase.selectNodebyName(name, false);
		return temp;
	}
	
	private int searchHistory( String hash ){
		for ( int i=history.size()-1; i>=0; i++ ){//search the history in the reversed direction to get latest changes
			for ( int j=0; j<history.get(i).size();j++ ){
				if ( history.get(i).get(j).getKey() == hash ){
					return i;
				}
			}
		}
		return INT_INVALID;
	}
	
	private int searchHistorybyName( String name ){
		for ( int i=history.size()-1; i>=0; i++ ){//search the history in the reversed direction to get latest changes
			for ( int j=0; j<history.get(i).size();j++ ){
				if ( history.get(i).get(j).getName() == name ){
					return i;
				}
			}
		}
		return INT_INVALID;
	}
	
	//for edit, delete, link and sever operation
	private NodeRecord getSource( String hash ) throws SQLException{
		int index = searchHistory( hash );
		ResultSet temp = null;
		NodeRecord temp2 = null;
		if ( index != INT_INVALID ){//recently modified in buffer
			for (int i=0; i<history.get(index).size(); i++){
				temp2 = history.get(index).get(i);
				if (temp2.getKey() == hash){
					break;
				}
			}
			return new NodeRecord(temp2);
		}
		else{ 
			temp = searchDB(hash);
			if ( temp == null ){//not in buffer or DB
				return null;
			}
			temp2 = transcribeSingleRecord(temp);
			return new NodeRecord(temp2);
		}
	}
	
	//if there is only one node changed, set  node2 = null
	private EditBuffer generateBuffer( NodeRecord node1, NodeRecord node2 ){
		EditBuffer newBuffer = new EditBuffer();
		newBuffer.addRecord(node1);
		if ( node2 != null )
			newBuffer.addRecord(node2);
		return new EditBuffer(newBuffer);
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
	 * 1.load network into currentBuffer and neighborListx from DB 
	 * 2.the net work consists of nodes who distance 3 unit from the central node
	 * 3.return the central node   
	 */
	private NodeRecord loadCurrentBufferNeighborList(  String hash  ){////////////////////////////////////
		
		
	}
	
	/**
	 * 
	 * @param target
	 * description:
	 * 1.update directedWeb with neighborListx
	 * 2.return DAGList
	 */
	private ArrayList<DAG> transcribeDAGList(){///////////////////////////////////////////////////
		
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
