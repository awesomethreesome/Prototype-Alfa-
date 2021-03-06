package actions;

import java.sql.*;
import java.util.*;

/**
 * 
 * @author Ruogu.Gao
 *
 */

public class DBManager {
	//////constructor
	public DBManager () {
		connectDataBase();
	}
	

	/////public methods
	
    public final ResultSet selectUser( String name, String password ) {
        try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_USER_SQL);
			ps.setString(1, name);
            ps.setString(2, password);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select user.");
			return null;
		}
		return result;
	}
    
    public final ResultSet selectUserName( String name ){
    	try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_USER_NAME_SQL);
			ps.setString(1, name);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select user name.");
			return null;
		}
		return result;
    }
	 
    public boolean insertUser( String name, String password, String rootlist/*info*/ ){
    	try{
			PreparedStatement ps = dbConnection.prepareStatement(INSERT_USER_SQL);
			ps.setString(1, "");
			ps.setString(2, name);
            ps.setString(3, password);
            ps.setString(4, rootlist);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert user.");
			return false;
		}
		return true;
    }
    
	public final ResultSet selectRootNodebyUserID( String userID ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_ROOTNODE_SQL);
			ps.setString(1, userID);
            ps.setString(2, null);//root node has no father 
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select root.");
			return null;
		}
		return result;
    }

	public final ResultSet selectNodebyHash( String hash) {
        try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_NODE_BY_HASH_SQL);
			ps.setString(1, hash);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select by hash.");
			return null;
		}
		return result;
    }
	
    public final ResultSet selectNodebyName( String name, boolean ambiguity ) {
        try{
        	String SQLStatement = ambiguity?SELECT_NODE_BY_NAME_AMIBIGUITY_SQL:SELECT_NODE_BY_NAME_SQL;
        	String inqueryParameter = ambiguity?(AMBIGUITY_TERM + name + AMBIGUITY_TERM):name;
			PreparedStatement ps = dbConnection.prepareStatement(SQLStatement);
			ps.setString(1, inqueryParameter);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select by name.");
			return null;
		}
		return result;
    }
    
    public final ResultSet selectNodebyIns( String ins, boolean ambiguity ){
        try{
        	String SQLStatement = ambiguity?SELECT_NODE_BY_INS_AMIBIGUITY_SQL:SELECT_NODE_BY_INS_SQL;
        	String inqueryParameter = ambiguity?(AMBIGUITY_TERM + ins + AMBIGUITY_TERM):ins;
			PreparedStatement ps = dbConnection.prepareStatement(SQLStatement);
			ps.setString(1, inqueryParameter);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select by ins.");
			return null;
		}
		return result;
    }

    public final ResultSet selectNodebyPro( String pro, boolean ambiguity ){
        try{
        	String SQLStatement = ambiguity?SELECT_NODE_BY_PRO_AMIBIGUITY_SQL:SELECT_NODE_BY_PRO_SQL;
        	String inqueryParameter = ambiguity?(AMBIGUITY_TERM + pro + AMBIGUITY_TERM):pro;
			PreparedStatement ps = dbConnection.prepareStatement(SQLStatement);
			ps.setString(1, inqueryParameter);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select by pro.");
			return null;
		}
		return result;

    }

    public final ResultSet selectNodebyFatherKey(String fKey){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_NODE_BY_FKEY_SQL);
			ps.setString(1, fKey);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select by father.");
			return null;
		}
		return result;
    }

    public boolean updateNode( String upFather, String upSon, String chosenKey, String upName, String upBirthdate, String upPro, String upIns, String upLink, String upBio){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_INFO_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, upName);
            ps.setString(2, upBirthdate);
            ps.setString(3, upPro);
            ps.setString(4, upIns);
            ps.setString(5, upLink);
            ps.setString(6, upBio);
            ps.setString(7, upFather);
            ps.setString(8, upSon);
            ps.setString(9, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update.");
			return false;
		}
		return true;
    }
    
    public boolean updateNodeFather( String chosenKey, String father ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_FATHER_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, father);
            ps.setString(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update father.");
			return false;
		}
		return true;
    }
    
    public boolean updateNodeBio( String chosenKey, String Bio ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_BIO_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, Bio);
            ps.setString(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update bio.");
			return false;
		}
		return true;
    }

    public boolean updateNodeGender( String chosenKey, String gender ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_FATHER_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, gender);
            ps.setString(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update gender.");
			return false;
		}
		return true;
    }

    
    public boolean updateNodeSon( String chosenKey, String son ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_SON_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, son);
            ps.setString(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update son.");
			return false;
		}
		return true;
    }

    public boolean deleteNode( String key ){
        try{
        	
			PreparedStatement ps = dbConnection.prepareStatement(DELECT_NODE_BY_KEY_SQL);
			//delect from node where nodekey = ?;
			ps.setString(1, key);
			ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to delete book.");
			return false;
		}
		return true;
    }    

    public boolean insertNode( String key, String userID, String father, String son, String upName, 
    		                   String upGender, String upBirthdate, String upPro, String upIns, 
    		                   String upLink, String upBio ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(INSERT_NODE_SQL);
			//insert into node (nodekey, userid, father, nodename, age, profession, institution, link) values(?,?,?,?,?,?,?,?);
			ps.setString(1, key);
            ps.setString(2, userID);
            ps.setString(3, father);
            ps.setString(4, son);
            ps.setString(5, upName);
            ps.setString(6, upGender);
            ps.setString(7, upBirthdate);
            ps.setString(8, upPro);
            ps.setString(9, upIns);
            ps.setString(10, upLink);
            ps.setString(11, upBio);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert node.");
			return false;
		}
		return true;
    }

	/////advanced methods
	public boolean createNodeTable() {
		try{
			connectionCheck();
			statement.executeUpdate(CREATE_NODETABLE_SQL);
			
		}catch ( Exception e ){
			System.out.println("ERROR: fail to create book table.");
			return false;
		}
		return true;
	}
	
	public boolean createUserTable() {
		try {
			connectionCheck();
			statement.executeUpdate(CREATE_USERTABLE_SQL);
			
		}catch ( Exception e ){
			System.out.println("ERROR: fail to create author table.");
			return false;
		}
		return true;
	}
	
	/////private methods
	private void connectDataBase() {
		try {
			Class.forName(driver);
			dbConnection = DriverManager.getConnection(dbURL, userName, passWord);
		}catch ( Exception e ){
			System.out.println("ERROR: failed to connect database ");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private void createDataBase() {
		try{
			statement = dbConnection.createStatement();//createDB;
			statement.executeUpdate(createDB);
			statement.executeUpdate(useDB);
			
		}catch ( Exception e ){
			System.out.println("ERROR: fail to creat database.");
			e.printStackTrace();
		}
	}
	
	/////protected methods
	protected void connectionCheck() throws Exception {
		if ( dbConnection == null ){
			Exception e = new Exception("connection with database has not yet established.");
			throw e;
		}
	}

	/////public variables
	
	
    public static String SELECT_USER_SQL = "select * from user where username = ? and upassword = ?;";
    public static String SELECT_USER_NAME_SQL = "select * from user where username = ?;";
    public static String INSERT_USER_SQL = "insert into user values( ?, ?, ?, ? );";
    public static String SELECT_ROOTNODE_SQL = "select * from node where userid = ? and father = ?;";
    public static String SELECT_NODE_BY_HASH_SQL = "select * from node where nodekey = ?;";
    public static String SELECT_NODE_BY_NAME_SQL = "select * from node where nodename = ?;";
    public static String SELECT_NODE_BY_INS_SQL = "select * from node where institution = ?;";
    public static String SELECT_NODE_BY_PRO_SQL = "select * from node where profession = ?;";
    public static String SELECT_NODE_BY_NAME_AMIBIGUITY_SQL = "select * from node where nodename like ?;";
    public static String SELECT_NODE_BY_INS_AMIBIGUITY_SQL = "select * from node where institution like ?;";
    public static String SELECT_NODE_BY_PRO_AMIBIGUITY_SQL = "select * from node where profession like ?;";
    public static String SELECT_NODE_BY_FKEY_SQL = "select * from node where father = ?";
    public static String UPDATE_NODE_INFO_SQL = "update node set nodename = ?, birthdate = ?, profession = ?, institution = ?, link = ?, bio = ?, father = ?, son = ? where nodekey = ?;";
    public static String UPDATE_NODE_FATHER_SQL = "update node set father = ? where nodekey = ?;";
    public static String UPDATE_NODE_GENDER_SQL = "update node set gender = ? where nodekey = ?;";
    public static String UPDATE_NODE_BIO_SQL = "update node set bio = ? where nodekey = ?;";
    public static String UPDATE_NODE_SON_SQL = "update node set son = ? where nodekey = ?;";
    public static String DELECT_NODE_BY_KEY_SQL = "delete from node where nodekey = ?;";
    public static String INSERT_NODE_SQL = "insert into node (nodekey, userid, father, son, nodename, gender, birthdate, profession, institution, link, bio) values(?,?,?,?,?,?,?,?,?,?,?);";
    public static String AMBIGUITY_TERM = "%";
    
	public static String CREATE_NODETABLE_SQL = "create table node(" +
			" nodekey varchar(6) not null primary key , " +
			" userid varchar(30) not null," +
			" father varchar(100) ," +
			" son varchar(100) ," +
			" nodename varchar(40)," +
			" gender varchar(6)," +
			" birthdate varchar(50)," +
			" profession varchar(50)," +
            " institution varchar(50)," +
            " link varchar(200)," + 
            " bio varchar(1000)" +
			");";
	public static String CREATE_USERTABLE_SQL = "create table user(" +
			"userid varchar(30) not null, " +
			"username varchar(30) not null, " +
			"upassword varchar(30), " +
			"rootlist varchar(100) " +
			");";

	/////private variables
	
	private final String userName = "root";
	private final String passWord = "vorstellung";
	private final String dbName = "RandRwiki";
	private final String dbURL = "jdbc:mysql://localhost:3306/" + dbName;
	
	
	/*
	//SAE
	private final String userName = "m2l20j5xyk";
	private final String passWord = "5ji00512yz0z4llhzwwijhkhwiwmjkyhj3hl1h21";
	private final String dbName = "app_researchrelation";
	private final String dbURL = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/" + dbName;
	*/
	private final String NodeTable = "node";
	private final String userTable = "user";
	private final String driver = "com.mysql.jdbc.Driver"; 
	private final String createDB = "create database " + dbName;
	private final String useDB = "use database " + dbName;
	
	protected Connection dbConnection = null;
	protected Statement statement = null;
	protected ResultSet result = null;
	
}
