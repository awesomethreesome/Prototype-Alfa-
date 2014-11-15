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
	
	public ArrayList<ListItem> searchQuery( String queryInput, String queryType ) throws SQLException{
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
		transcribeSearchResult( temp );//now all possible node have been stored in searchResultList, which is a list of ListItem
		return searchResultList;
	}
	
	public NodeRecord detailQuery( String Hash ) {
		NodeRecord temp = new NodeRecord();
		temp.setKey(STRING_INVALID);
		for (int i=0; i<currentBuffer.size(); i++ ){
			if ( currentBuffer.get(i).getKey() == Hash )
				break;
		}
		
		return temp;
	}
	
	public void updateQuery() {/////////////////////////////////////////////////////
		
	}
	
	public void deleteQuery( String Hash ) {
		
	}
	
	public void addQuery(  NodeRecord newNode ) {
		
	}
	
	public void fetch( String hash ){
		
	}
	
	/////private methods
	/**
	 * 
	 * @param sr
	 * @throws SQLException
	 * Description:
	 * 1.transcribe the search result into searchResultList(arraylist<listitem>)
	 */
	private void transcribeSearchResult( ResultSet sr ) throws SQLException {
		//ArrayList<NodeRecord> currentResult = new ArrayList<NodeRecord>();
		//NodeRecord temp = null;
		searchResultList.clear();
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
			searchResultList.add(temp);
			temp = null;
		}
		sr.beforeFirst();
		
		//currentBuffer.initialize(currentResult);
	}
	
	private void transcribeUser( ResultSet sr ) throws SQLException{////////////////////////////////////////
		currentUser.clear();
		while ( sr.next() ) {
			 currentUser.setUserID(sr.getString(1));
			 currentUser.setUserName(sr.getString(2));
			 currentUser.setPassword(sr.getString(3));
			 currentUser.setRootList(sr.getString(4));
		}
		sr.beforeFirst();
	}
	
	/////public variables
	
	public static int INT_INVALID = -1;
	public static double DOUBLE_INVALID = -1.0;
	public static String STRING_INVALID = "end of the line";
	public static int ROOT_STAGE = -1;
	public static String SEARCHING_STAGE = "on searching";
	/////private variables
	private DBManager dataBase = new DBManager();//
	private final String passWord = "vorstellung";
	private final String userName = "wille"; 
	private static boolean authorized = false;
	private static String inputPassword = "vorstellung", inputUserName = "wille";
	
	private UserRecord currentUser = new UserRecord() ;//current user
	private EditBuffer currentBuffer = new EditBuffer();
	private HistoryList history = new HistoryList();
	
	private ArrayList<ListItem> searchResultList = new ArrayList<ListItem>();
	
	
	
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
