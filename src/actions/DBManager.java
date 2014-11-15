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
			System.out.println("ERROR: fail to insert book.");
			return null;
		}
		return result;
	}
	 
	public final ResultSet selectRootNodebyUserID( String userID ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_ROOTNODE_SQL);
			ps.setString(1, userID);
            ps.setInt(2, -1);//root node has no father 
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert book.");
			return null;
		}
		return result;
    }

    public final ResultSet selectNodebyName( String name, boolean ambiguity ) {
        try{
        	String SQLStatement = ambiguity?SELECT_NODE_BY_INS_AMIBIGUITY_SQL:SELECT_NODE_BY_INS_SQL;
        	String inqueryParameter = ambiguity?(name + AMBIGUITY_TERM):name;
			PreparedStatement ps = dbConnection.prepareStatement(SQLStatement);
			ps.setString(1, inqueryParameter);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert book.");
			return null;
		}
		return result;
    }
    
    public final ResultSet selectNodebyIns( String ins, boolean ambiguity ){
        try{
        	String SQLStatement = ambiguity?SELECT_NODE_BY_INS_AMIBIGUITY_SQL:SELECT_NODE_BY_INS_SQL;
        	String inqueryParameter = ambiguity?(ins + AMBIGUITY_TERM):ins;
			PreparedStatement ps = dbConnection.prepareStatement(SQLStatement);
			ps.setString(1, inqueryParameter);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert book.");
			return null;
		}
		return result;
    }

    public final ResultSet selectNodebyPro( String pro, boolean ambiguity ){
        try{
        	String SQLStatement = ambiguity?SELECT_NODE_BY_PRO_AMIBIGUITY_SQL:SELECT_NODE_BY_PRO_SQL;
        	String inqueryParameter = ambiguity?(pro + AMBIGUITY_TERM):pro;
			PreparedStatement ps = dbConnection.prepareStatement(SQLStatement);
			ps.setString(1, inqueryParameter);
			
            result = ps.executeQuery();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert book.");
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
			System.out.println("ERROR: fail to insert book.");
			return null;
		}
		return result;
    }

    public boolean updateNode( int chosenKey, String upName, String upBirthdate, String upPro, String upIns, String upLink ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_INFO_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, upName);
            ps.setString(2, upBirthdate);
            ps.setString(3, upPro);
            ps.setString(4, upIns);
            ps.setString(5, upLink);
            ps.setInt(6, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update.");
			return false;
		}
		return true;
    }
    
    public boolean updateNodeFather( int chosenKey, String father ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_FATHER_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, father);
            ps.setInt(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update.");
			return false;
		}
		return true;
    }
    
    public boolean updateNodeBio( int chosenKey, String Bio ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_BIO_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, Bio);
            ps.setInt(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update.");
			return false;
		}
		return true;
    }

    public boolean updateNodeGender( int chosenKey, String gender ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_FATHER_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, gender);
            ps.setInt(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update.");
			return false;
		}
		return true;
    }

    
    public boolean updateNodeSon( int chosenKey, String son ){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_NODE_SON_SQL);
			// update node set nodename = ?, age = ?, profession = ?, institution = ?, link = ? where key = ?;
            ps.setString(1, son);
            ps.setInt(2, chosenKey);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update.");
			return false;
		}
		return true;
    }

    public boolean deleteNode( int key ){
        try{
        	
			PreparedStatement ps = dbConnection.prepareStatement(DELECT_NODE_BY_KEY_SQL);
			//delect from node where nodekey = ?;
			ps.setInt(1, key);
			ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert book.");
			return false;
		}
		return true;
    }    

    public boolean insertNode( int key, String userID, String father, String son, String upName, String upGender, String upBirthdate, String upPro, String upIns, String upLink, String upBio){
        try{
			PreparedStatement ps = dbConnection.prepareStatement(INSERT_NODE_SQL);
			//insert into node (nodekey, userid, father, nodename, age, profession, institution, link) values(?,?,?,?,?,?,?,?);
			ps.setInt(1, key);
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
			System.out.println("ERROR: fail to insert book.");
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
	
	private void connectionCheck() throws Exception {
		if ( dbConnection == null ){
			Exception e = new Exception("connection with database has not yet established.");
			throw e;
		}
	}

	/////public variables
	

    public static String SELECT_USER_SQL = "select * from user where userid = ? and upassword = ?;";
    public static String SELECT_ROOTNODE_SQL = "select * from node where userid = ? and father = ?;";
    public static String SELECT_NODE_BY_NAME_SQL = "select * from node where nodename = ?;";
    public static String SELECT_NODE_BY_INS_SQL = "select * from node where institution = ?;";
    public static String SELECT_NODE_BY_PRO_SQL = "select * from node where profession = ?;";
    public static String SELECT_NODE_BY_NAME_AMIBIGUITY_SQL = "select * from node where nodename like ?;";
    public static String SELECT_NODE_BY_INS_AMIBIGUITY_SQL = "select * from node where institution like ?;";
    public static String SELECT_NODE_BY_PRO_AMIBIGUITY_SQL = "select * from node where profession like ?;";
    public static String SELECT_NODE_BY_FKEY_SQL = "select * from node where father = ?";
    public static String UPDATE_NODE_INFO_SQL = "update node set nodename = ?, birthdate = ?, profession = ?, institution = ?, link = ? where nodekey = ?;";
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
            " link varchar(200)" + 
            " bio varchar" +
			");";
	public static String CREATE_USERTABLE_SQL = "create table user(" +
			"userid varchar(30) not null primary key, " +
			"userName varchar(30) not null, " +
			"upassword varchar(30), " +
			"rootlist varchar(100) " +
			");";

	/////private variables
	
	private final String userName = "root";
	private final String passWord = "vorstellung";
	private final String dbName = "RandRwiki";
	private final String dbURL = "jdbc:mysql://localhost:3306/" + dbName;
	private final String NodeTable = "node";
	private final String userTable = "user";
	private final String driver = "com.mysql.jdbc.Driver"; 
	private final String createDB = "create database " + dbName;
	private final String useDB = "use database " + dbName;
	
	private Connection dbConnection = null;
	private Statement statement = null;
	private ResultSet result = null;
	
}
