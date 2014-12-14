package actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
/**
 * 
 * @author dell
 * description:
 * 1.monitor the data exchange with Link database
 */
public class LinkDB extends DBManager{
	public LinkDB(){
		super();
	}
	
	public ResultSet selectLink( String hash, boolean type ){//type = true, source
		try{
			String sqlQuery = new String();
			if ( type ){
				sqlQuery = SELECT_LINK_SOURCE_SQL;
			}
			else {
				sqlQuery = SELECT_LINK_DESTINY_SQL;
			}
			PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
			ps.setString(1, hash);
			
	        result = ps.executeQuery();
	
		}catch ( Exception e ) {
			System.out.println("ERROR: fail to select link.");
			return null;
		}
		return result;

	}
	
	public ResultSet selectLink( String sourceHash, String destinyHash){
		try{
			PreparedStatement ps = dbConnection.prepareStatement(SELECT_LINK_SQL);
			ps.setString(1, sourceHash);
			ps.setString(2, destinyHash);
			
	        result = ps.executeQuery();
	
		}catch ( Exception e ) {
			System.out.println("ERROR: fail in exact link select .");
			return null;
		}
		return result;

	}
	
	public boolean deleteLink( String hash, boolean type ){//type = true, source
		try{
			String sqlQuery = new String();
			if ( type ){
				sqlQuery = DELETE_LINK_SOURCE_SQL;
			}
			else {
				sqlQuery = DELETE_LINK_DESTINY_SQL;
			}
			PreparedStatement ps = dbConnection.prepareStatement(sqlQuery);
			ps.setString(1, hash);
			
	        ps.executeUpdate();
	
		}catch ( Exception e ) {
			System.out.println("ERROR: fail to delete link .");
			return false;
		}
		return true;

	}
	
	public boolean deleteLink( String sourceHash, String destinyHash ){
		try{
        	
			PreparedStatement ps = dbConnection.prepareStatement(DELETE_LINK_SQL);
			ps.setString(1, sourceHash);
			ps.setString(2, destinyHash);
			ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail in exact link delete.");
			return false;
		}
		return true;
	}
	
	public boolean insertLink( String sourceHash, String destinyHash, String period ){
		try{
			PreparedStatement ps = dbConnection.prepareStatement(INSERT_LINK_SQL);

			ps.setString(1, sourceHash);
            ps.setString(2, destinyHash);
            ps.setString(3, period);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to insert link.");
			return false;
		}
		return true;
	}
	
	public boolean updateLink( String src, String des, String period ){
		try{
			PreparedStatement ps = dbConnection.prepareStatement(UPDATE_LINK_SQL);
            ps.setString(2, src);
            ps.setString(3, des);
            ps.setString(1, period);
			
            ps.executeUpdate();

		}catch ( Exception e ) {
			System.out.println("ERROR: fail to update son.");
			return false;
		}
		return true;
	}
	
	public boolean createLinkTable() {
		try{
			super.connectionCheck();
			statement.executeUpdate(CREATE_LINKTABLE_SQL);
			
		}catch ( Exception e ){
			System.out.println("ERROR: fail to create link table.");
			return false;
		}
		return true;
	} 
	
	//public constants
	public static String SELECT_LINK_SQL = "select * from link where src = ? and destiny = ?;";
	public static String SELECT_LINK_SOURCE_SQL = "select * from link where src = ?;";
	public static String SELECT_LINK_DESTINY_SQL = "select * from link where destiny = ?;";
	public static String DELETE_LINK_SQL = "delete from link where src = ? and destiny = ?;";
	public static String DELETE_LINK_SOURCE_SQL = "delete from link where src = ?;";
	public static String DELETE_LINK_DESTINY_SQL = "delete from link where destiny = ?;";
	public static String INSERT_LINK_SQL = "insert into link values(?, ?, ?);";
	public static String UPDATE_LINK_SQL = "update node set period = ? where src = ? and destiny = ?;";
	public static String CREATE_LINKTABLE_SQL = "create table link(" +
			"src varchar(10) not null, " +
			"destiny varchar(10), " +
			"period varchar(22) " +//xxxx-xx-xx-xxxx-xx-xx
			");";
	
	//private variables
	private final String linkTable = "link";
}
