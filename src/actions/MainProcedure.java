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
	
	
	
	
	
	/////private methods
	private void transcribeNode( ResultSet sr ) throws SQLException {//////////////////////////////////////////
		
	}
	
	private void transcribeUser( ResultSet sr ) throws SQLException{////////////////////////////////////////
		
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
	private static int currentIndex = INT_INVALID;//the index of chosen node in currentSons
	
	private static List<NodeRecord> currentSons = new ArrayList<NodeRecord>();//all sons of currentNode
	private static NodeRecord currentNode = new NodeRecord();//current chosen node
	private static UserRecord currentUser = new UserRecord() ;//current user 
	
	private static String queryInput, queryType;//queryType has 3 possible values{Name, Institution, Profession, }
	private static String inputPassword, inputUserName;
	 
}
