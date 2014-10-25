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
			System.out.println("when login :" + authorized);
			return "AUTHORIZED";
		}
		authorized = false;
		System.out.println("when login :" + authorized);
		return "UN_AUTHORIZED";
	}
	
	
	
	
	/////public variables
	
	public static int INT_INVALID = -1;
	public static double DOUBLE_INVALID = -1.0;
	/////private variables
	private DBManager dataBase = new DBManager();//
	private final String passWord = "vorstellung";
	private final String userName = "wille"; 
	private static boolean authorized = false;
	private static int currentIndex = INT_INVALID;//the index of chosen book in bookSR
	
	private static List<BookRecord> bookSR = new ArrayList<BookRecord>();//book search results
	private static AuthorRecord authorSR = new AuthorRecord();//author search results
	
	private static String currentISBN = null;//the ISBN of chosen book in bookSR
	private static BookRecord currentBook = null;//current chosen book
	private static String isUpAuthor = "No";//only has 2 possible values{Yes, No} 
	
	private static String queryInput, queryType;//queryType has 3 possible values{Name, Institution, Profession, }
	private static String inputPassword, inputUserName;
	
	private static String upISBN, upTitle, upPub, upPubDate, upName, upCountry;//used for update and add
	private static int upAuthorID, upAge;
	private static double upPrice;
	
	//private static boolean deleteRecord;
	
	//private static int cnt=0; 
}
