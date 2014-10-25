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
	/////public methods 
	
	public String logInCheck(){
		if ( inputPassword.equals(passWord) && inputUserName.equals(userName)){
			authorized = true;
			//search the user info 
			ResultSet temp = dataBase.selectUser( userName ,  inputPassword );
			if ( temp == null ) {
				authorized = false;
				return "UN_AUTHORIZED";
			}
			transcribeUser( temp );//current user info has be loaded into currentUser 
			return "AUTHORIZED";
		}
		authorized = false;
		return "UN_AUTHORIZED";
	}
	
	public String manageQuery(){
		ResultSet  temp = null; 
		temp = dataBase.selectRootNodebyUserID( currentUser.getUserID() );
		if ( temp == null ) {
			return "ERROR";
		}
		transcribeNode( temp );//all roots has been loaded into currentSons
		currentNode.setFather(ROOT_STAGE);//indicating that it on the root stage
		return "QUERY_DONE";
	}
	
	public String searchQuery() throws SQLException{
		//queryType has 3 possible values{Name, Institution, Profession, }
		ResultSet temp = null, temp_ = null;
		if ( queryType.equals("Name")  ) {
			temp = dataBase.selectNodebyName(queryInput);		
		}
		else if ( queryType.equals("Institution") ) {
			temp = dataBase.selectNodebyIns(queryInput);
		}
		else if ( queryType.equals("Profession") ) {
			temp = dataBase.selectNodebyPro(queryInput);
		}
		else {//default option is search by book title
			temp = dataBase.selectBookbyName(queryInput);
		}
		transcribeNode( temp );//now all selected book have been stored in currentSons, which is a list of NodeRecord
		
		return "QUERY_DONE";
	}
	
	
	public String ascendQuery() {///////////////////////////////////////////////////////////////////////////
		
		return "ASCEND_DONE";
	}
	
	public String detailQueryinSearch() {
		int index = findIndexofChosenNode();
		detailBuffer = new NodeRecord(currentSons.get(index));
		return "QUERY_APPROVED"; 
	}
	
	public String descendQuery() {
		int index = findIndexofChosenNode();
		//set the chosen node as new father
		currentNode = new NodeRecord(currentSons.get(index));		
		//search sons of currentNode	
		ResultSet temp = dataBase.selectNodebyFatherKey( currentNode.getKey() );
		transcribeNode( temp );//now the new sons has been loaded into currentSons
		return "DESCEND_DONE";
	}
	
	
	
	
	public String blankSwitch() {
		return "SWITCH_APPROVED";
	}
	
	/////private methods
	private void transcribeNode( ResultSet sr ) throws SQLException {//////////////////////////////////////////
		
	}
	
	private void transcribeUser( ResultSet sr ) throws SQLException{////////////////////////////////////////
		
	}
	
	private int findIndexofChosenNode(){
		//find the chosen node in currentSons
		int i = 0;
		for (i = 0; i < currentSons.size(); i++){
			if ( currentSons.get(i).getKey() == chosenKey ){
				break;
			}
		}
		return i;
	}
	/////public variables
	

	public static int INT_INVALID = -1;
	public static double DOUBLE_INVALID = -1.0;
	public static String ROOT_STAGE = "end of the line";
	public static String SEARCHING_STAGE = "on searching";
	/////private variables
	private DBManager dataBase = new DBManager();//
	private final String passWord = "vorstellung";
	private final String userName = "wille"; 
	private static boolean authorized = false;
	private static int chosenKey = INT_INVALID;//the key of chosen node in currentSons
	
	private static List<NodeRecord> currentSons = new ArrayList<NodeRecord>();//all sons of currentNode
	private static NodeRecord currentNode = new NodeRecord();//current chosen node
	private static UserRecord currentUser = new UserRecord() ;//current user
	private static NodeRecord detailBuffer = new NodeRecord();//store the node whose detail is to be displayed
	
	private static String queryInput, queryType;//queryType has 3 possible values{Name, Institution, Profession, }
	private static String inputPassword, inputUserName;
	
	
	
	/////all setters and getters
	public static int getChosenKey() {
		return chosenKey;
	}

	public static void setChosenKey(int chosenKey) {
		MainProcedure.chosenKey = chosenKey;
	}

	public static List<NodeRecord> getCurrentSons() {
		return currentSons;
	}

	public static void setCurrentSons(List<NodeRecord> currentSons) {
		MainProcedure.currentSons = currentSons;
	}

	public static NodeRecord getCurrentNode() {
		return currentNode;
	}

	public static void setCurrentNode(NodeRecord currentNode) {
		MainProcedure.currentNode = currentNode;
	}

	public static UserRecord getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(UserRecord currentUser) {
		MainProcedure.currentUser = currentUser;
	}

	public static String getQueryInput() {
		return queryInput;
	}

	public static void setQueryInput(String queryInput) {
		MainProcedure.queryInput = queryInput;
	}

	public static String getQueryType() {
		return queryType;
	}

	public static void setQueryType(String queryType) {
		MainProcedure.queryType = queryType;
	}

	public static String getInputPassword() {
		return inputPassword;
	}

	public static void setInputPassword(String inputPassword) {
		MainProcedure.inputPassword = inputPassword;
	}

	public static String getInputUserName() {
		return inputUserName;
	}

	public static void setInputUserName(String inputUserName) {
		MainProcedure.inputUserName = inputUserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
