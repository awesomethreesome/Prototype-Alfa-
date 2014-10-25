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
		//createDataBase();
		//createAuthorTable();
		//createBookTable();
	}
	

	/////public methods
	public boolean createBookTable() {
		try{
			connectionCheck();
			statement.executeUpdate(CREATE_BOOKTABLE);
			
			
		}catch ( Exception e ){
			System.out.println("ERROR: fail to create book table.");
			return false;
		}
		return true;
	}
	
	public boolean createAuthorTable() {
		try {
			connectionCheck();
			statement.executeUpdate(CREATE_AUTHORTABLE);
			
		}catch ( Exception e ){
			System.out.println("ERROR: fail to create author table.");
			return false;
		}
		return true;
	}
	
	public boolean insertBook( String ISBN, String title, int auID, String pub, String pubDate, double price ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(INSERT_BOOK_SQL);
			ps.setString(1, ISBN);
			ps.setString(2, title);
			ps.setInt(3, auID);
			ps.setString(4, pub);
			ps.setString(5, pubDate);
			ps.setDouble(6, price);
			ps.executeUpdate();
			ps.close();
			
		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert book.");
			return false;
		}
		return true;
	}
	
	public boolean insertAuthor( int ID, String name, int age, String country ) {
		 try{
			 PreparedStatement ps = dbConnection.prepareStatement(INSERT_AUTHOR_SQL);
			 ps.setInt(1, ID);
			 ps.setString(2, name);
			 ps.setInt(3, age);
			 ps.setString(4, country);
			 ps.executeUpdate();
			 ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: fail to insert author.");
			 return false;
		 }
		 return true;
	}
	 
	public boolean deleteBookbyTitle( String title ) {
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(DELETE_BOOK_BY_TITLE_SQL);
			 
			 ps.setString(1, title);
			 
			 ps.executeUpdate();
			 ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: fail to delete book(by title)");
			 return false;
		 }
		 return true;
	}
	
	public boolean deleteBook( String isbn ) {
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(DELETE_BOOK_BY_ISBN_SQL);
			 
			 ps.setString(1, isbn);
			 
			 ps.executeUpdate();
			 ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: fail to delete book(by isbn)");
			 return false;
		 }
		 return true;
	}
	
	
	public boolean deleteAuthor( String name ) {
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(DELETE_AUTHOR_BY_NAME_SQL);
			 
			 ps.setString(1, name);
			 
			 ps.executeUpdate();
			 ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR:fail to delete author");
			 return false;
		 }
		 return true;
	}
	
	public boolean deleteAuthor(  int ID ) {
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(DELETE_AUTHOR_BY_ID_SQL);
			 
			 ps.setInt(1, ID);
			 
			 ps.executeUpdate();
			 ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR:fail to delete author by id");
			 return false;
		 }
		 return true;
	}
	
	public ResultSet selectBookbyISBN( String isbn ) /*throws Exception*/ {
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(SELECT_BOOK_BY_ISBN_SQL);
			 result = null;
			 ps.setString(1, isbn);
			 
			 result = ps.executeQuery();
			 //ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: in selectBookbyISBN");
			 //throw e;
		 }
		 return result;
	}
	
	
	public ResultSet selectBookbyAuthorID( int nameid ) /*throws Exception*/ {
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(SELECT_BOOK_BY_AUTHORID_SQL);
			 result = null;
			 ps.setInt(1, nameid);
			 
			 result = ps.executeQuery();
			 //ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: in selectBookbyAuthor");
			 //throw e;
		 }
		 return result;
	}
	
	public ResultSet selectBookbyTitle( String title ) /*throws Exception*/{
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(SELECT_BOOK_BY_TITLE_SQL);
			 result = null;
			 
			 ps.setString(1, title);
			 
			 result = ps.executeQuery();
			 //ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: in selectBookbyTitle.");
			 //return result; 
		 }
		 return result;
	}
	
	public ResultSet selectBookbyPub( String pub ) /*throws Exception*/{
		try{
			 PreparedStatement ps = dbConnection.prepareStatement(SELECT_BOOK_BY_PUBLISHER_SQL);
			 result = null;
			 
			 ps.setString(1, pub);
			 
			 result = ps.executeQuery();
			 //ps.close();
			 
		 }catch ( Exception e ) {
			 System.out.println("ERROR: in selectBookbyTitle.");
			 //return result; 
		 }
		 return result;
	}
	
	public ResultSet selectAuthor ( String name ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_AUTHOR_BY_NAME_SQL);
			result = null;
			ps.setString(1, name);
			
			result = ps.executeQuery();
			//ps.close();
			
			
		}catch ( Exception e ) {
			System.out.println("ERROR: in selectAutnor(by name)");
		}
		return result;
	}
	
	public ResultSet selectAuthor ( int ID ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_AUTHOR_BY_ID_SQL);
			result = null;
			ps.setInt(1, ID);
			
			result = ps.executeQuery();
			//ps.close();
			
			
		}catch ( Exception e ) {
			System.out.println("ERROR: in selectAutnor(by id)");
		}
		return result;
	}
	
	public boolean updateBook(String isbn, int authorid, String publisher, String publishDate, double price ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_BOOK_SQL_I);
			result = null;
			ps.setInt(1, authorid);
			ps.setString(2, publisher);
			ps.setString(3, publishDate);
			ps.setDouble(4, price);
			ps.setString(5, isbn);
			
			ps.executeUpdate();
			ps.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in updateBook");
			return false;
		}
		return true;
	}
	
	public boolean updateBookAuthor( int author, String ISBN ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_BOOK_AUTHOR_SQL);
			result = null;
			ps.setInt(1, author);
			ps.setString(2, ISBN);
			
			ps.executeUpdate();
			ps.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in ");
			return false;
		}
		return true;
	}
	
	
	public boolean updateBookPub ( String publisher, String isbn ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_BOOK_PUBLISHER_SQL);
			result = null;
			ps.setString(1, publisher);
			ps.setString(2, isbn);
			
			ps.executeUpdate();
			ps.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in ");
			return false;
		}
		return true;
	}
	
	public boolean updateBookPubData ( String publishDate, String isbn ) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_BOOK_PUBDATE_SQL);
			result = null;
			ps.setString(1, publishDate);
			ps.setString(2, isbn);
			
			ps.executeUpdate();
			ps.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in ");
			return false;
		}
		return true;
	}
	
	public boolean updateBookPrice ( double price, String isbn) {
		try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_BOOK_PRICE);
			result = null;
			ps.setDouble(1, price);
			ps.setString(2, isbn);
			
			ps.executeUpdate();
			ps.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in ");
			return false;
		}
		return true;
	}
	
	public ResultSet allBook() {
		try{
			Statement stmt = dbConnection.createStatement();
			result = null;
			
			result = stmt.executeQuery(SELECT_ALL_BOOK);
			//stmt.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in allbook");
		}
		return result;
	}
	
	public ResultSet allAuthor() {
		try{
			Statement stmt = dbConnection.createStatement();
			result = null;
			
			result = stmt.executeQuery(SELECT_ALL_AUTHOR);
			//stmt.close();
			
		}catch ( Exception e ){
			System.out.println("ERROR: in allauthor");
		}
		return result;
	}
	
	public String getAuthorTable() {
		return authorTable;
	}

	public String getBookTable() {
		return bookTable;
	}


	/////advanced methods
	//public creatTable( String tableInfo )
	
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
	
	public static String INSERT_BOOK_SQL = "insert into book values(?, ?, ?, ?, ?, ?);";
	public static String INSERT_AUTHOR_SQL = "insert into author values(?, ?, ?, ?);";
	public static String DELETE_BOOK_BY_TITLE_SQL = "delete from book where title = ?;";
	public static String DELETE_BOOK_BY_ISBN_SQL = "delete from book where isbn = ?;";
	public static String DELETE_AUTHOR_BY_ID_SQL = "delete from author where authorid = ?;";
	public static String DELETE_AUTHOR_BY_NAME_SQL = "delete from author where name = ?;";
	public static String SELECT_AUTHOR_BY_ID_SQL = "select * from author where authorid = ?;";
	public static String SELECT_AUTHOR_BY_NAME_SQL = "select * from author where name = ?;";
	public static String SELECT_BOOK_BY_TITLE_SQL = "select * from book where title = ?;";
	public static String SELECT_BOOK_BY_ISBN_SQL = "select * from book where isbn = ?;";
	public static String SELECT_BOOK_BY_AUTHORID_SQL = "select *  from book where authorid = ?;";
	public static String SELECT_BOOK_BY_PUBLISHER_SQL = "select * from book where publisher = ?;";
	//update the author, publisher, publish date and price at once£¬ by isbn
	public static String UPDATE_BOOK_SQL_I = "update book set " +
			"authorid = ?, " +
			"publisher = ?, " +
			"publishdate = ?, " +
			"price = ? where isbn = ?; ";
	//all update operation is indexed by book isbn
	public static String UPDATE_BOOK_AUTHOR_SQL = "update book set authorid = ? where isbn = ?;";
	public static String UPDATE_BOOK_PUBLISHER_SQL = "update book set publisher = ? where isbn = ?;";
	public static String UPDATE_BOOK_PUBDATE_SQL = "update book set publishdate = ? where isbn = ?;";
	public static String UPDATE_BOOK_PRICE = "update book set price = ? where isbn = ?;";
	public static String SELECT_ALL_BOOK = "select * from book;";
	public static String SELECT_ALL_AUTHOR = "select * from author;";
	public static String CREATE_BOOKTABLE = "create table Book(" +
			"ISBN varchar(15) not null primary key, " +
			"Title varchar(30) not null," +
			" AuthorID int unsigned not null," +
			" Publisher varchar(40)," +
			" PublishDate varchar(12)," +
			" Price double" +
			");";
	public static String CREATE_AUTHORTABLE = "create table Author(" +
			"AuthorID int unsigned not null primary key, " +
			"Name varchar(30) not null, " +
			"Age int unsigned, " +
			"Country varchar(20) " +
			");";

	/////private variables
	
	private final String userName = "root";
	//private final String userName = "5k43y10zxw";
	private final String passWord = "vorstellung";
	//private final String passWord = "ylj1hz0lk3kxmm1mllmmmj1l2wh10105z1zmhxmj";
	private final String dbName = "BookManager";
	private final String dbURL = "jdbc:mysql://localhost:3306/" + dbName;
	//private final String dbName = "app_bms18";
	//private final String dbURL = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/" + dbName;
	private final String bookTable = "Book";
	private final String authorTable = "Author";
	private final String driver = "com.mysql.jdbc.Driver"; 
	private final String createDB = "create database " + dbName;
	private final String useDB = "use database " + dbName;
	
	private Connection dbConnection = null;
	private Statement statement = null;
	private ResultSet result = null;
	
}
