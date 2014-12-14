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
	public boolean synced = true;
	public static void main( String[] args ) {
		MainProcedure test_case = new MainProcedure();
	}
	//empty constructor
	public MainProcedure(){
		//System.out.println("create an instance");
	}
	/////public methods 
	public void logInCheck( String inputUserName, String inputPassword ) {//name password
		try{
			System.out.println(inputPassword + " " + inputUserName);
			
			ResultSet temp = dataBase.selectUser( inputUserName ,  inputPassword );
			if ( temp.next() == false ) {
				authorized = false;
				return;
			}
			temp.beforeFirst();
			transcribeUser( temp );//current user info has be loaded into currentUser
			authorized = true;
			System.out.println("currentUser info: "+currentUser.getUserID() + " "+ currentUser.getRootList());
		}catch( Exception e ){
			System.out.println("error in logInCheck: exception: " + e.getMessage() +" cause: "+ e.getCause());
			
		}
	}
	
	public void logOut(){
		authorized = false;
	}
	
	public boolean signIn( String info, String name, String password ) {
		try{
			//check duplication
			ResultSet temp = dataBase.selectUserName(name);
			if ( temp.next() == true ){
				return false;
			}
			//insert user
			return dataBase.insertUser(name, password, info);
		}catch( Exception e ){
			System.out.println("error in signIn: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
		}
		
	}
	
	
	
	
	public ArrayList<ListItem> search( String queryInput, String queryType ) {
		try{
			//queryType has 3 possible values{Name, Institution, Profession, }
			ResultSet temp = null;
			if ( "Institution".equals( queryType ) ) {
				temp = dataBase.selectNodebyIns(queryInput, true);//search with ambiguity
			}
			else if ( "Profession".equals(queryType) ) {
				temp = dataBase.selectNodebyPro(queryInput, true);
			}
			else if ( !queryInput.contains("+") ){//default option is search by name
				System.out.println("going in");
				temp = dataBase.selectNodebyName(queryInput, true);
			}
			else {//search relation by name
				findPathbyName( queryInput );
				String srcName = new String();
				int i=0; 
				for ( i=0; i<queryInput.length(); i++){
					if ( queryInput.charAt(i) == '+' ){
						break;
					}
				}
				srcName = queryInput.substring(0, i);
				NodeRecord tempRecord = getSource( getSourceHashbyName(srcName) );
				if ( tempRecord != null ){
					searchList.add( new ListItem( tempRecord.getName(), tempRecord.getKey() ) );
				}
				return searchList;
			}
			//security
			if (temp == null){
				System.out.println("no result match");
				return searchList;
			}
			transcribeSearchResult( temp );//now all possible node have been stored in searchList, which is a list of ListItem
			//synchronize with buffer(front- name; back- hash)
			for (int j=0; j<searchList.size(); j++){
				for ( int i=history.size()-1; i>=0; i-- ){
					if ( history.ModRecord(i).contains( searchList.get(j).front ) ){
						if ( history.ModRecord(i).contains( "delete" ) ){
							searchList.remove(j);
						}
						break;
					}
				}
			}
			NodeRecord tempRecord = getSource( getSourceHashbyName(queryInput) );
			
			if ( tempRecord != null ){
				searchList.add( new ListItem( tempRecord.getName(), tempRecord.getKey() ) );
			}
			
			return searchList;
		}catch( Exception e ){
			System.out.println("error in search: exception: "+ e.getMessage());
			return null;
		}
	}
	
	public boolean findPathbyName( String input ){
		try{
			if ( input==null ){
				return false;
			}
			
			//get src and des name
			String srcName = new String();
			String desName = new String();
			int i=0; 
			for ( i=0; i<input.length(); i++){
				if ( input.charAt(i) == '+' ){
					break;
				}
			}
			srcName = input.substring(0, i);
			desName = input.substring(i+1, input.length());
			String srcHash = getSourceHashbyName(srcName);
			String desHash = getSourceHashbyName(desName);
			if ( srcHash==null || desHash==null ){
				return false;
			}
			
			return findPath( srcHash, desHash);
		}catch( Exception e ){
			System.out.println("error in findPathbyName:");
			return false;
		}
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
	
	//from DB
	public String getLinkInfo( String source, String destiny ) {
		try {
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
		}catch( Exception e ){
			System.out.println("error in getLinkInfo: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return null;
		}
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	// two inputs are all hash code
	public boolean link(String teacher, String student, String period) {
		try{
			if ( !authorityCheck() )
				return false;
			ArrayList<NodeRecord> ModifiedNodes = changeRelation( teacher, student, true );
			String modification = "link" +ModifiedNodes.get(0).getName()+ "as" +ModifiedNodes.get(1).getName()+ "-s teacher during " + period;
			
			EditBuffer newBuffer = generateBuffer( ModifiedNodes );
			history.pushBack(newBuffer, modification);
			return true;
		}catch( Exception e ){
			System.out.println("error in Link: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
		}
	}
	
	public boolean link2( String teaName, String stuName, String period ) {
		try{
			if ( !authorityCheck() )
				return false;
			//get hash
			String teaHash = new String(), stuHash = new String();
			teaHash = getSourceHashbyName( teaName );
			stuHash = getSourceHashbyName( stuName );
			
			return link( teaHash, stuHash, period );
		}catch( Exception e ){
			System.out.println("error in Link2: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
		}
	}
	
	
	public boolean sever2(String teaName, String stuName ) {
		try{
			if ( !authorityCheck() )
				return false;
			//get hash
			String teaHash = new String(), stuHash = new String();
			teaHash = getSourceHashbyName( teaName );
			stuHash = getSourceHashbyName( stuName );
			
			return sever( teaHash, stuHash );
		}catch( Exception e ){
			System.out.println("error in sever2: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
		}
	}
	
	//returns true when success, false otherwise.
	//refresh historyList
	public boolean sever(String teacher, String student) {
		try{
			if ( !authorityCheck() )
				return false;
			ArrayList<NodeRecord> ModifiedNodes = changeRelation( teacher, student, false );
			String modification = "sever " +ModifiedNodes.get(0).getName()+ "and" +ModifiedNodes.get(1).getName();
			
			EditBuffer newBuffer = generateBuffer( ModifiedNodes );
			history.pushBack(newBuffer, modification);
			return true;
		}catch( Exception e ){
			System.out.println("error in sever: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
		}
	}
	
	//returns the hash of newly-added
	//refresh historyList
	public String add( CharDesc newInput )  {
		try {
			if ( !authorityCheck() )//check authority
				return null;	
			//if there's no delete history and has record in DB, abandon operation
			int historyIndex = searchHistorybyName( newInput.name );
			ResultSet tempSet= searchDBbyName( newInput.name );
			if ( historyIndex == INT_INVALID && tempSet.next() == true ){//no record in history but in DB
				return null;
			}
			else if (historyIndex != INT_INVALID/* && tempSet.next() == true*/){//in DB in history but not been deleted || not in DB, but was not deleted in history  
				String modRecord = history.ModRecord(historyIndex);
				if ( modRecord.contains("delete") == false)
					return null;
			}
			
			String newHash = hashGenerator.generateHash();
			NodeRecord temp = new NodeRecord( newHash, currentUser.getUserID(), ""/*son*/, ""/*father*/, 
											  newInput.name, newInput.gender,newInput.birthDate, newInput.profession, newInput.institution, newInput.link, newInput.bio );
			String modification = "add node: " + temp.getName();
			
			ArrayList<NodeRecord> ModifiedNodes = new ArrayList<NodeRecord>();
			ModifiedNodes.add( new NodeRecord(temp) );
			EditBuffer newBuffer = generateBuffer( ModifiedNodes );
			history.pushBack(newBuffer, modification);
			return temp.getKey();
		}catch( Exception e ){
			System.out.println("error in add(): exception");
			return "";
		}
	}
	
	public boolean delete( String Hash ) {
		try{
			if ( !authorityCheck() )
				return false;
			//if there's no add history and has no record in DB, abandon operation
			int historyIndex = searchHistory(Hash);
			ResultSet tempSet= searchDB(Hash);
			if ( historyIndex == INT_INVALID && tempSet.next() == false ){//has no record in history or DB
				return false;
			}
			else if (historyIndex != INT_INVALID ){//in DB in history but has been deleted ||not in DB, but was deleted in history  
				String modRecord = history.ModRecord(historyIndex);
				if ( modRecord.contains("delete") == true)
					return false;
			}
			tempSet.beforeFirst();
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
		}catch( Exception e ){
			System.out.println("error in delete: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
		}
		
	}
	
	//returns null when not found
	//refresh neighborLists and directedWeb
	public CharDesc get(String hash) {
		try{
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
			//history.clear();///////////////////////////////////////////////need an initial push? 
			//security
			if ( currentBuffer == null )
				currentBuffer = new EditBuffer();
			//security
			if ( centralNode == null ){//fatal error
				System.out.println("fatal error: target node does not exist");
				return null;
			}
			transcribeDAGList( centralNode,true );
			//getDAGLinkInfo();
			
			CharDesc tempCharDesc = new CharDesc(centralNode);
			
			System.out.println("at the end of get: ");
			System.out.println("CharDesc: " + tempCharDesc.hash);
			System.out.println("CharDesc: " + tempCharDesc.name);
			System.out.println(neighborList1.size());
			System.out.println(neighborList2.size());
			System.out.println(neighborList3.size());
			System.out.println(directedWeb.size());
			return tempCharDesc;
		}catch( Exception e ){
			System.out.println("error in get: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return null;
		}
	}
	
	//refresh historyList
	public void edit(CharDesc target) {
		try{
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
			System.out.println(modification + "size of history: " + history.size());
		}catch( Exception e ){
			System.out.println("error in edit: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return ;
		}
	}
	
	public void back2History(String back) {
		int index = Integer.parseInt(back);
		history.pop(index);
	}
	
	//save changes to database
	public void syncDB() {
		try{
			//update all changes stored in history to DB
			for (int i=0; i<history.size(); i++){//from the earliest to latest 
				for ( int j=0; j<history.get(i).size(); j++){
					String operation = history.ModRecord(i);
					NodeRecord updater = new NodeRecord(history.get(i).get(j));
					if ( !operation.contains("delete") || j == history.get(i).size()-1 ){
						updateDB( updater, operation );
					}
					//delete needs to update all related nods and delete the target node(which is at the last of it's EditBuffer)
					else { 
						updateDB( updater, "sever" );
					}
				}
			}
			updateLinkTable();
			//clear history
			history.clear();
		}catch( Exception e ){
			System.out.println("fatal error in syncDB");
		}
	}
	
	public boolean findPath( String srcHash, String desHash ) {
		try{
			ArrayList<String> visited = new ArrayList<String>();
			ArrayList<String> descendant = new ArrayList<String>();
			ArrayList<String> queue = new ArrayList<String>(), nextQueue_ = new ArrayList<String>();
			NodeRecord temp = new NodeRecord();
			boolean found = false;
			//BP algorithm
			queue.add(srcHash);
			visited.add(srcHash);
			while ( !queue.isEmpty() ){
				for  ( int i=0; i<queue.size(); i++ ){
					temp = getSource( queue.get(i) );
					//security
					if ( temp == null ){
						System.out.println("error in finPath: fail to getSource of " + queue.get(i));
						continue;
					}
					descendant = sprawl( temp, false );
					//check desHash
					found = descendant.contains(desHash);
					if ( found ){
						visited.add(desHash);
						break;
					}
					//check visited and add to nectQueue 
					for ( int k=0; k<descendant.size(); k++){
						if ( !visited.contains( descendant.get(k) ) ){
							nextQueue_.add( descendant.get(k) );
							visited.add( descendant.get(k) );
						}
					}
					
				}
				if ( found ){
					break;
				}
				queue.clear();
				queue = new ArrayList<String>( nextQueue_ );
				nextQueue_.clear();
			}
			//back trace and form neighborListx and directedDAG
			if ( found ){
				tracePath( srcHash, desHash, visited );
				fitPathtoNeighborListDAG(srcHash, desHash);
				return true;
			}
			return false;
		}catch( Exception e ){
			System.out.println("error in findPath: exception: " + e.getMessage() +" cause: "+ e.getCause());
			return false;
			
		}
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
				String tempHash = history.get(i).get(j).getKey(); 
				if ( hash.equals( tempHash ) ){
					return i;
				}
			}
		}
		return INT_INVALID;
	}
	
	private int searchHistorybyName( String name ){
		for ( int i=history.size()-1; i>=0; i-- ){//search the history in the reversed direction to get latest changes
			for ( int j=history.get(i).size()-1; j>=0; j-- ){
				if ( name.equals( history.get(i).get(j).getName() ) ){
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
				if (temp2.getKey().equals( hash )){
					break;
				}
			}
			return new NodeRecord(temp2);
		}
		else{ 
			temp = searchDB(hash);
			if ( temp.next() == false ){//not in buffer or DB
				return null;
			}
			temp.beforeFirst();
			temp2 = transcribeSingleRecord(temp);
			return new NodeRecord(temp2);
		}
	}
	//get the hash code of given name 
	private String getSourceHashbyName( String sourceName ) throws SQLException{
		int index = searchHistorybyName( sourceName );
		ResultSet temp = null;
		NodeRecord temp2 = null;
		if ( index != INT_INVALID ){//recently modified in buffer
			if ( history.ModRecord(index).contains("delete") ){//the wanted node has been deleted 
				return null;
			}
			for (int i=0; i<history.get(index).size(); i++){
				temp2 = history.get(index).get(i);
				if (temp2.getName() == sourceName){
					break;
				}
			}
			return temp2.getKey();
		}
		else{ 
			temp = searchDBbyName( sourceName );
			if ( temp.next() == false ){//not in buffer or DB
				return null;
			}
			temp.beforeFirst();
			temp2 = transcribeSingleRecord(temp);
			return temp2.getKey();
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
			dataBase.updateNode( updater.getFather(), updater.getSon(), updater.getKey(), updater.getName(), updater.getBirthDate(), 
								updater.getProfession(), updater.getInstitution(), updater.getLink(), updater.getBio());
		}
		else
			return;
	}
	
	private void updateLinkTable() throws SQLException{////////////////////////////////////////////////////////////////////
		NodeRecord teacher = new NodeRecord(), student = new NodeRecord();
		for (int i=0; i<history.size(); i++){
			for (int j=0; j<history.get(i).size(); j++ ){
				if ( history.ModRecord(i).contains("delete")){
					int sourceIndex = history.get(i).size()-1;
					teacher = history.get(i).get( sourceIndex );//potential hazard
					dataBase.deleteLink(teacher.getKey(), true);
					
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
					int index = history.ModRecord(i).indexOf("-s teacher during ");//linkDarth MooreasGeneral Grievous-s teacher during 123
					int startIndex = index+"-s teacher during ".length(); 
					period = history.ModRecord(i).substring( startIndex );
					if ( dataBase.selectLink( teacher.getKey(), student.getKey() ).next() == false ){
						dataBase.insertLink( teacher.getKey(), student.getKey(), period );
					}
					else {
						dataBase.updateLink( teacher.getKey(), student.getKey(), period );
					}
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
	@SuppressWarnings("unused")
	private ArrayList<String> sprawl( NodeRecord center, boolean direction){
		System.out.println("into the sprawl");
		System.out.println("center hash: " + center.getKey());
		System.out.println("center father: " + center.getFather());
		System.out.println("center son: " + center.getSon());
		ArrayList<String> result = new ArrayList<String>();
		result.clear();
		String hashList = new String();
		String temp = null;
		if ( center == null ){//security
			System.out.println("fatal error in sprawl: center == null");
			return result;
		}
		else if ( !direction ){//descendant
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
	 * 3. flag = true indicates that it's called by 'get(...)', otherwise 'tracePath(...)'
	 * @throws SQLException 
	 */
	private ArrayList<DAG> transcribeDAGList( NodeRecord center, boolean flag/*added in 12.13.2014*/ ) throws SQLException{
		ArrayList<DAG> tempDirectedWeb = new ArrayList<DAG>();
		ArrayList<String> descendant = new ArrayList<String>();
		int index = 0;
		String linkInfo = new String();
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
				//added in 12.13.2014
				linkInfo = getLinkInfo(  currentBuffer.get(i).getKey(), currentBuffer.get(index).getKey());
				if ( linkInfo != null && !linkInfo.equals("") ){
					temp.info.add(linkInfo);
				}
				else {//look the history///////////////////////////////117
					String srcName =  temp.src;
					String desName =  tempName;
					String period = "";
					int index_aux, startIndex;  
					String mod = new String();
					for ( int k=history.size()-1; k>=0; k-- ){
						if ( history.ModRecord(k).contains(srcName) && history.ModRecord(k).contains(desName) ){
							if ( history.ModRecord(k).contains("link") ){ 
								index_aux = history.ModRecord(k).indexOf("-s teacher during ");//linkDarth MooreasGeneral Grievous-s teacher during 123
								startIndex = index_aux+"-s teacher during ".length();
								period = history.ModRecord(k).substring( startIndex );
							}
							break;
						}
					}
					temp.info.add(period);
				}
				//added in 12.13.2014
			}
			if ( currentBuffer.get(i).getKey() != center.getKey() || flag == false ){
				temp.dst.add("goto: " + currentBuffer.get(i).getName());
				temp.info.add("");
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
			/*
			String newSon = teacherRecord.getSon().replaceAll(student, "");
			String newFather = studentRecord.getFather().replaceAll(teacher, "");
			*/
			ArrayList<String> son = sprawl( new NodeRecord( teacherRecord ), false );
			ArrayList<String> father = sprawl( new NodeRecord( studentRecord ), true );
			String newSon = teacherRecord.getSon();
			String newFather = studentRecord.getFather();
			if ( son.contains(student) ){
				son.remove(student);
				newSon = "";
				for (int i=0; i<son.size(); i++){
					newSon = newSon + son.get(i);
				}
			}
			if ( father.contains(teacher) ){
				father.remove(teacher);
				newFather = "";
				for ( int i=0; i<father.size(); i++ ){
					newFather = newFather + father.get(i);
				}
			}
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
	
	
	/**
	 * 
	 * @param src
	 * @param des
	 * @param visited
	 * description:
	 * 1. trace the path and set neighborListx and directedDAG
	 * @throws SQLException 
	 * 
	 */
	private void tracePath( String src, String des, ArrayList<String> visited ) throws SQLException{
		//DP
		path.clear();
		found = false;
		tracePath_aux( src, des, visited );
	}
	
	/**
	 * 
	 * @param current
	 * @param des
	 * @param visited
	 * description:
	 * 1.auxiliary method for tracePath, operating recursive searching among visited nodes
	 * 2.path stored in stack 'path'
	 * @throws SQLException 
	 */
	private void tracePath_aux( String current, String des, ArrayList<String> visited ) throws SQLException{
		if ( found ){
			return;
		}
		else if ( current.equals( des ) ){
			found = true;
			path.push(current);
			return;
		}
		path.push(current);
		
		//get descendant
		ArrayList<String> descendant = new ArrayList<String>();
		descendant = sprawl( getSource(current), false );
		//recursive search
		for ( int i=0; i<descendant.size(); i++ ){
			if ( visited.contains(descendant.get(i)) == true ){
				tracePath_aux( descendant.get(i), des, visited);
			}
		}
		
		if (!found){
			path.pop();
		}
	}
	
	/**
	 * description:
	 * 1. if path exist(found = true), fit nodes in the path into neighborListx and directedWeb
	 * @throws SQLException 
	 */
	private void fitPathtoNeighborListDAG( String src, String des ) throws SQLException{
		if ( found ){
			neighborList1.clear();
			neighborList2.clear();
			neighborList3.clear();
			directedWeb.clear();
			
			//store the path into currentBuffer to fit the interface of 'transcribeDAGList(...)'
			currentBuffer.clear();
			NodeRecord temp = new NodeRecord();
			while ( !path.empty() ){
				temp = getSource( path.peek() );
				currentBuffer.addRecord( new NodeRecord( temp ) );
				path.pop();
			}
			
			//set neighborListx: all(except src and des) shall be added into neighborList2
			temp = getSource( src );
			appendtoNeighborList(temp, 0);
			temp = getSource( des );
			appendtoNeighborList(temp, 0);
			for ( int i=0; i<currentBuffer.size(); i++ ){
				temp = currentBuffer.get(i);
				if ( !temp.getKey().equals(src) && !temp.getKey().equals(des) ){
					appendtoNeighborList( new NodeRecord(temp), 1 );
				}
			}   
			
			//set directedDAG
			transcribeDAGList( new NodeRecord(), false );
		}
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
	public HistoryList history = new HistoryList();
	
	//auxiliary private variables for methods 
	private ArrayList<NodeRecord> nextQueue = new ArrayList<NodeRecord>();
	private boolean found;
	private Stack<String> path = new Stack<String>();
	
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
