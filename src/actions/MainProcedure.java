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
		if ( "Institution".equals( queryType ) ) {
			temp = dataBase.selectNodebyIns(queryInput, true);//search with ambiguity
		}
		else if ( "Profession".equals(queryType) ) {
			temp = dataBase.selectNodebyPro(queryInput, true);
		}
		else {//default option is search by name
			System.out.println("going in");
			temp = dataBase.selectNodebyName(queryInput, true);
		}
		//security
		if (temp == null){
			System.out.println("no result match");
			return searchList;
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
	
	public String getLinkInfo( String source, String destiny ) throws SQLException{
		//scan the history
		NodeRecord srcNode = getSource( source );
		ArrayList<String> srcDescendant = sprawl( srcNode, false );
		String period = "";
		for ( int i=0; i<srcDescendant.size(); i++ ){
			if ( srcDescendant.get(i).equals(destiny) ){
				period = transcribeLinkInfo( dataBase.selectLink(source, destiny) );
				break;
			}
		}
		return period;
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	// two inputs are all hash code
	public boolean link(String teacher, String student, String period) throws SQLException {
		if ( !authorityCheck() )
			return false;
		ArrayList<NodeRecord> ModifiedNodes = changeRelation( teacher, student, true );
		String modification = "link" +ModifiedNodes.get(0).getName()+ "as" +ModifiedNodes.get(1).getName()+ "'s teacher during " + period;
		
		EditBuffer newBuffer = generateBuffer( ModifiedNodes );
		history.pushBack(newBuffer, modification);
		return true;
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	public boolean sever(String teacher, String student) throws SQLException {
		if ( !authorityCheck() )
			return false;
		ArrayList<NodeRecord> ModifiedNodes = changeRelation( teacher, student, false );
		String modification = "sever " +ModifiedNodes.get(0).getName()+ "and" +ModifiedNodes.get(1).getName();
		
		EditBuffer newBuffer = generateBuffer( ModifiedNodes );
		history.pushBack(newBuffer, modification);
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
		
		ArrayList<NodeRecord> ModifiedNodes = new ArrayList<NodeRecord>();
		ModifiedNodes.add( new NodeRecord(temp) );
		EditBuffer newBuffer = generateBuffer( ModifiedNodes );
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
	
		ArrayList<NodeRecord> ModifiedNodes = new ArrayList<NodeRecord>();	
		//delete all related links
		ArrayList<String> ascendant = sprawl( source, true );
		ArrayList<String> descendant = sprawl( source, false );
		ArrayList<NodeRecord> temp = new ArrayList<NodeRecord>(); 
		for ( int i=0; i<ascendant.size(); i++ ){
			temp = changeRelation( ascendant.get(i), Hash, false );
			ModifiedNodes.add( new NodeRecord(temp.get(0)) );//push the teachers of 'Hash' into ModifiedNodes
		}
		for ( int j=0; j<descendant.size(); j++ ){
			temp = changeRelation( Hash, descendant.get(j), false);
			ModifiedNodes.add( new NodeRecord(temp.get(1)) );//push the students of 'Hash' into ModifiedNodes
		}
		//generate buffer
		ModifiedNodes.add( new NodeRecord(source) );
		EditBuffer newBuffer = generateBuffer( ModifiedNodes );
		history.pushBack(newBuffer, modification);
		return true;
	}
	
	//returns null when not found
	//refresh neighborLists and directedWeb
	public CharDesc get(String hash) throws SQLException {
		//int index = searchCurrentBuffer(hash);
		/*
		if ( index == INT_INVALID )
			return null;
		*/
		neighborList1.clear();
		neighborList2.clear();
		neighborList3.clear();
		directedWeb.clear();
		System.out.println("get hash:" + hash);
		NodeRecord centralNode = loadCurrentBufferNeighborList(hash);
		history.clear();///////////////////////////////////////////////need an initial push?
		//security
		if ( currentBuffer == null )
			currentBuffer = new EditBuffer();
		//security
		if ( centralNode == null ){//fatal error
			CharDesc errorCharDesc = new CharDesc();
			errorCharDesc.hash = "fatal error: target node does not exist";
			return errorCharDesc;
		}
		transcribeDAGList( centralNode );
		
		//synchronize DAG with editBuffer
		
		CharDesc tempCharDesc = new CharDesc(centralNode);
		
		System.out.println("at the end of get: ");
		System.out.println("CharDesc: " + tempCharDesc.hash);
		System.out.println("CharDesc: " + tempCharDesc.name);
		
		System.out.println(neighborList1.size());
		System.out.println(neighborList2.size());
		System.out.println(neighborList3.size());
		System.out.println(directedWeb.size());
		return tempCharDesc;
	}
	
	//refresh historyList
	public void edit(CharDesc target) throws SQLException {
		if ( !authorityCheck() )
			return;
		
		NodeRecord origin = getSource( target.hash );
		if ( origin == null )//not found or has already been deleted 
			return;
		NodeRecord updater = new NodeRecord( target );
		
		NodeRecord target0 = edit_auxilary( updater, origin );
		String modification = "edit node: " + target0.getName();
		
		ArrayList<NodeRecord> ModifiedNodes = new ArrayList<NodeRecord>();
		ModifiedNodes.add(target0);
		EditBuffer newBuffer = generateBuffer( ModifiedNodes );
		history.pushBack(newBuffer, modification);
		
	}
	
	public void back2History(String back) {
		int index = Integer.parseInt(back);
		history.pop(index);
	}
	
	//save changes to database
	public void syncDB() throws SQLException {
		//update all changes stored in history to DB
		for (int i=0; i<history.size(); i++){//from the earliest to latest 
			for ( int j=0; j<history.get(i).size(); j++){
				String operation = history.ModRecord(i);
				NodeRecord updater = new NodeRecord(history.get(i).get(j));
				updateDB( updater, operation );
			}
		}
		updateLinkTable();
		//clear history
		history.clear();
	}
	
	public boolean findPath( String srcHash, String desHash ){
		
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
			
			temp.setBack(sr.getString( 1 ));
			temp.setFront(sr.getString( 5 ));
			
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
	
	private String transcribeLinkInfo( ResultSet sr ) throws SQLException {
		String Info = "";
		while( sr.next() ){
			Info = sr.getString(3);
		}
		sr.beforeFirst();
		return Info;
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
		for ( int i=history.size()-1; i>=0; i-- ){//search the history in the reversed direction to get latest changes
			for ( int j=history.get(i).size()-1; j>=0; j-- ){
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
	//get newest record from history or DB
	private NodeRecord getSource( String hash ) throws SQLException{
		int index = searchHistory( hash );
		ResultSet temp = null;
		NodeRecord temp2 = null;
		if ( index != INT_INVALID ){//recently modified in buffer
			if ( history.ModRecord(index).contains("delete") ){//the wanted node has been deleted 
				return null;
			}
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
	
	//accept an list of modified nodes
	private EditBuffer generateBuffer( ArrayList<NodeRecord> ModifiedNodeList){
		EditBuffer newBuffer = new EditBuffer();
		for ( int i=0; i<ModifiedNodeList.size(); i++ ){
			newBuffer.addRecord( new NodeRecord( ModifiedNodeList.get(i) ) );
		}
		return new EditBuffer(newBuffer);
	}
	
	
	private void updateDB( NodeRecord updater, String operation ) throws SQLException{
		if ( operation.contains("add") ){
			dataBase.insertNode(updater.getKey(), updater.getUserID(), updater.getFather(), updater.getSon(), updater.getName(), 
								updater.getGender(), updater.getBirthDate(), updater.getProfession(), 
								updater.getInstitution(), updater.getLink(), updater.getBio());
		}
		else if ( operation.contains("delete") ){
			dataBase.deleteNode(updater.getKey());
		}
		else if( operation.contains("sever") || operation.contains("link") || operation.contains("edit")){
			dataBase.updateNode(updater.getKey(), updater.getName(), updater.getBirthDate(), 
								updater.getProfession(), updater.getInstitution(), updater.getLink(), updater.getBio());
		}
		else
			return;
	}
	
	private void updateLinkTable(){////////////////////////////////////////////////////////////////////
		NodeRecord teacher = new NodeRecord(), student = new NodeRecord();
		for (int i=0; i<history.size(); i++){
			for (int j=0; j<history.get(i).size(); j++ ){
				if ( history.ModRecord(i).contains("delete")){
					
				}
				else if ( history.ModRecord(i).contains("sever") ){
					teacher = history.get(i).get(0);//potential hazard
					student = history.get(i).get(1);
					dataBase.deleteLink(teacher.getKey(), student.getKey());
				}
				else if ( history.ModRecord(i).contains("link") ){
					teacher = history.get(i).get(0);//potential hazard
					student = history.get(i).get(1);
					String period = new String();
					int index = history.ModRecord(i).indexOf("'s teacher during ");
					int startIndex = index+"'s teacher during ".length(); 
					period = history.ModRecord(i).substring( startIndex );
					dataBase.insertLink( teacher.getKey(), student.getKey(), period );
				}
			}
		}
		
	}
	
	//update those variables that are not null in updatedRecord to temp
	private NodeRecord edit_auxilary( NodeRecord updatedRecord,  NodeRecord origin ){
		NodeRecord temp = new NodeRecord( origin );
		
		temp.setBio( (updatedRecord.getBio()==null?temp.getBio():updatedRecord.getBio() ) );
		temp.setBirthDate( (updatedRecord.getBirthDate()==null?temp.getBirthDate():updatedRecord.getBirthDate() ) );
		temp.setFather( (updatedRecord.getFather()==null?temp.getFather():updatedRecord.getFather() ) );
		temp.setSon( (updatedRecord.getSon()==null?temp.getSon():updatedRecord.getSon() ) );
		temp.setName( (updatedRecord.getName()==null?temp.getName():updatedRecord.getName() ) );
		temp.setGender( (updatedRecord.getGender()==null?temp.getGender():updatedRecord.getGender() ) );
		temp.setProfession( (updatedRecord.getProfession()==null?temp.getProfession():updatedRecord.getProfession() ) );
		temp.setInstitution( (updatedRecord.getInstitution()==null?temp.getInstitution():updatedRecord.getInstitution() ) );
		temp.setLink( (updatedRecord.getLink()==null?temp.getLink():updatedRecord.getLink() ) );
		
		return new NodeRecord(temp); 
	}
	
	
	private boolean authorityCheck(){
		return authorized;
	}
	
	private int searchCurrentBuffer( String hash ){
		for ( int i=0; i<currentBuffer.size(); i++ ){
			if ( currentBuffer.get(i).getKey().equals(hash) ){
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
	 * @throws SQLException 
	 */
	private NodeRecord loadCurrentBufferNeighborList(  String hash  ) throws SQLException{
		System.out.println("in the loadCurrentBufferNeighborList : hash is " + hash);
		
		NodeRecord centralNode = getSource( hash );
		if (centralNode == null){//fatal error, node does not exist
			System.out.println("fatal error in loadCurrentBufferNeighborList, node does not exist");
			return null;
		}
		ArrayList<NodeRecord> queue = new ArrayList<NodeRecord>();
		ArrayList<String> ascendant = new ArrayList<String>(), descendant = new ArrayList<String>();
		
		queue.add(centralNode);
		currentBuffer.clear();
		currentBuffer.addRecord(new NodeRecord(centralNode));
		nextQueue.clear();
		for ( int i=0; i<3; i++ ){//distance at most 3
			for ( int j=0; j<queue.size(); j++ ){
				ascendant = sprawl( queue.get(j), true );
				descendant = sprawl( queue.get(j), false );
				
				fetchNewest( ascendant, i );
				fetchNewest( descendant, i );//currentBuffer and NeighborList-i and nextQueue shall yet be updated
				
			}
			queue.clear();
			queue = new ArrayList<NodeRecord>(nextQueue);//reference here?we want it to transfer by value
			nextQueue.clear();
		}
		OrganizeNeighborList3();
		return new NodeRecord( centralNode );
	}
	
	/**
	 * description:
	 * 1. check if nodes in input duplicate with that in currentBuffer
	 * 2. push non-duplicate node to currentBuffer and NeighborListx and nextQueue
	 * 3. nodes which has been deleted shall not push into 'nextqueue'
	 * 4. 
	 * @throws SQLException 
	 */
	private void fetchNewest( ArrayList<String> input, int x ) throws SQLException{
		System.out.println("in fetchNewest");
		for ( int k=0; k<input.size();k++ ){
			if ( searchCurrentBuffer( input.get(k) ) == INT_INVALID){//non duplicate
				System.out.println("times: " + k);
	
				NodeRecord tempRecord = getSource( input.get(k) );
				if ( tempRecord == null ){//deleted or not exist
					System.out.println("'getSource()' failure in fetchNewest() ");
					continue;
				}
				appendtoNeighborList( tempRecord, x );//set neighborListx
				currentBuffer.addRecord(tempRecord);//push to currentBuffer
				NodeRecord tempRecord1 = new NodeRecord(tempRecord);
				nextQueue.add(tempRecord1);//push to nextQueue
			}
		}	
	}
	
	
	private void appendtoNeighborList( NodeRecord newcomer, int i ){
		ListItem temp = new ListItem(newcomer.getName(), newcomer.getKey());
		if ( i == 0 ){
			neighborList1.add(temp);
		}
		else if (i == 1){
			neighborList2.add(temp);
		}
		else if (i == 2){
			neighborList3.add(temp);
		}
		else
			return;
	}
	
	//add "goto" term to neighborList3 to meet the requirement of UI 
	private void OrganizeNeighborList3(){
		for(int i=0; i<neighborList1.size(); i++){
			ListItem temp = new ListItem( "goto: "+neighborList1.get(i).getFront(), neighborList1.get(i).getBack() );
			neighborList3.add(temp);
		}
		for(int j=0; j<neighborList2.size(); j++){
			ListItem temp1 = new ListItem( "goto: "+neighborList2.get(j).getFront(), neighborList2.get(j).getBack() );
			neighborList3.add(temp1);
		}
	}
	
	/**
	 * 
	 * @param center
	 * @param direction
	 * @return
	 * description:
	 * 1. find all the ascendant or the descendant( direction = true, ascendant)
	 * 2. return in the form of ArrayList<String>, which is the list of hash code
	 * 3. notice that the hash code length 5
	 */
	private ArrayList<String> sprawl( NodeRecord center, boolean direction){
		System.out.println("into the sprawl");
		System.out.println("center hash: " + center.getKey());
		System.out.println("center father: " + center.getFather());
		System.out.println("center son: " + center.getSon());
		ArrayList<String> result = new ArrayList<String>();
		result.clear();
		String hashList = new String();
		String temp = null;
		if ( !direction ){//descendant
			hashList = center.getSon();
		}
		else {
			hashList = center.getFather();
		}
		System.out.println("in sprawl: hashList: " + hashList);
		//security
		if ( hashList == null ){
			return new ArrayList<String>(result);
		}
		while ( /*"".equals(hashList) == false*/hashList.length() >=5 ){
			System.out.println("in sprawl while 1: hashList: " + hashList);
			temp = hashList.substring(0, 5);
			result.add(temp);
			temp =null;
			hashList = hashList.substring(5);
			System.out.println("in sprawl while 2: hashList: " + hashList);
			System.out.println("in sprawl while 2: hashList.length: " + hashList.length());
		}
		
		return new ArrayList<String>(result);
	}
	
	/**
	 * 
	 * @param target
	 * description:
	 * 1.update directedWeb with neighborListx
	 * 2.return DAGList
	 */
	private ArrayList<DAG> transcribeDAGList( NodeRecord center ){
		ArrayList<DAG> tempDirectedWeb = new ArrayList<DAG>();
		ArrayList<String> descendant = new ArrayList<String>();
		int index = 0;
		System.out.println("in transcribeDAGList, currentBuffer status: " + currentBuffer);
		for ( int i=0; i<currentBuffer.size(); i++ ){
			descendant = sprawl( currentBuffer.get(i), false );
			DAG temp = new DAG();
			temp.src = currentBuffer.get(i).getName();
			for ( int j=0; j<descendant.size(); j++ ){
				index = searchCurrentBuffer( descendant.get(j) );
				if (index == INT_INVALID) 
					continue;
				String tempName = new String(currentBuffer.get(index).getName());
				temp.dst.add( tempName );
			}
			if ( currentBuffer.get(i).getKey() != center.getKey() ){
				temp.dst.add("goto: " + currentBuffer.get(i).getName());
			}
			tempDirectedWeb.add(temp);
		}
		System.out.println("directedWeb status: size: " + directedWeb.size());
		directedWeb = new ArrayList<DAG>(tempDirectedWeb);//DAG is transfered by reference
		return new ArrayList<DAG>(directedWeb);
	}
	/**
	 * 
	 * @param teacher
	 * @param student
	 * @param linkFlag
	 * @return
	 * description:
	 * 1. operate link or sever on given nodes, linkFlag = true stands for link
	 * 2. returns a ArrayList<NodeRecord>, if is called by link() or sever(), first entry of the ArrayList is newTeacher
	 * @throws SQLException 
	 */
	private ArrayList<NodeRecord> changeRelation( String teacher, String student, boolean linkFlag ) throws SQLException{
		NodeRecord teacherSource = getSource( teacher );
		NodeRecord studentSource = getSource( student );
		if ( teacherSource == null || studentSource == null){//fail to find one or both node
			System.out.println("error: in changeRelation(): fail to locate targeting node(s)");
			return null;
		}
		
		NodeRecord teacherRecord = new NodeRecord( teacherSource );
		NodeRecord studentRecord = new NodeRecord( studentSource );
		//set new relation link
		if ( !linkFlag ){
			String newSon = teacherRecord.getSon().replaceAll(student, "");
			String newFather = studentRecord.getFather().replaceAll(teacher, ""); 
			teacherRecord.setSon(newSon);
			studentRecord.setFather(newFather);
		}
		else {
			String newSon = teacherRecord.getSon() + student;
			String newFather = studentRecord.getFather() + teacher; 
			teacherRecord.setSon(newSon);
			studentRecord.setFather(newFather);
		}
		
		ArrayList<NodeRecord> ModifiedNodeList = new ArrayList<NodeRecord>();
		ModifiedNodeList.add( new NodeRecord(teacherRecord) );
		ModifiedNodeList.add( new NodeRecord(studentRecord) );
		return ModifiedNodeList;
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
	public ArrayList<ListItem> historyList = new ArrayList<ListItem>();
	public ArrayList<ListItem> neighborList1 = new ArrayList<ListItem>();
	public ArrayList<ListItem> neighborList2 = new ArrayList<ListItem>();
	public ArrayList<ListItem> neighborList3 = new ArrayList<ListItem>();
	public ArrayList<DAG> directedWeb = new ArrayList<DAG>();
	
	public String debugInfo = new String();
	public NodeHash hashGenerator = null;
	
	/////private variables
	private LinkDB dataBase = new LinkDB();//
	private final String passWord = "vorstellung";
	private final String userName = "wille"; 
	private static String inputPassword = "vorstellung", inputUserName = "wille";
	
	private UserRecord currentUser = new UserRecord() ;//current user
	private EditBuffer currentBuffer = new EditBuffer();
	private HistoryList history = new HistoryList();
	
	
	private ArrayList<NodeRecord> nextQueue = new ArrayList<NodeRecord>();
	
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
