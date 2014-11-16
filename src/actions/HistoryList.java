package actions;
import java.util.*;
import java.sql.*;
/**
 * 
 * @author dell
 * Description:
 * 1. store each modification made by users and it's coresponding EditBuffer 
 */
public class HistoryList {
	/////constructor
	HistoryList() {
		historyChain = new ArrayList<EditBuffer>();
		modificationRecord = new ArrayList<String>();
		originRecord = new ArrayList<String>();
	}
	
	/////public methods
	public void setOrigin( EditBuffer oriBuffer ){
		originRecord.clear();
		for ( int i=0; i<oriBuffer.size(); i++ ){
			originRecord.add( oriBuffer.get(i).getKey() );
		}
	}
	
	public void pushBack( EditBuffer currentBuffer, String modification ){
		EditBuffer temp = null;
		temp = new EditBuffer(currentBuffer);
		historyChain.add(temp);
		modificationRecord.add(modification);
	}
	
	public EditBuffer popTop(){
		int index = historyChain.size() - 1 ;
		modificationRecord.remove(index);
		return historyChain.remove(index);
	}
	
	public EditBuffer pop(int index){
		if ( index >= historyChain.size() )
			return null;
		for ( int i = historyChain.size()-1; i>index;i-- ){
			historyChain.remove(i);
			modificationRecord.remove(i);
		}
		modificationRecord.remove(index);
		return historyChain.remove(index);
	}
	
	public void clear(){
		historyChain = new ArrayList<EditBuffer>();
		modificationRecord = new ArrayList<String>();
		originRecord = new ArrayList<String>();
	}
	
	public int size(){
		return historyChain.size();
	}
	/////private methods
	
	
	/////public constant;(modification type)
	String UPDATE_INFO = "";
	String DELETE£ßNODE = "";
	String INSERT_NODE = "";
	String UPDATE_RELATION = "";
	
	/////private variables
	List<EditBuffer> historyChain; 
	List<String> modificationRecord;
	List<String> originRecord;
	
}
