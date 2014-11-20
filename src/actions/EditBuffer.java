package actions;
import java.util.*;
/**
 * 
 * @author dell
 * Description:
 * 1. holds the current on-editing network
 * 2. fundamentally an arrayList of UserRecord
 * 
 */
public class EditBuffer {
	/////blank constructor
	public EditBuffer(){
		currentNet = new ArrayList<NodeRecord>();
	}
	/////copy constructor
	EditBuffer( EditBuffer Net ){
		NodeRecord temp = null;
		currentNet = new ArrayList<NodeRecord>();
		for ( int i = 0; i < Net.getCurrentNet().size(); i++ ){
			temp = new NodeRecord(Net.getCurrentNet().get(i));
			currentNet.add(temp);
		}
	}
	
	/////public methods
	
	public void initialize( ArrayList<NodeRecord> iniBuffer ){
		NodeRecord temp = null;
		currentNet.clear();
		for ( int i=0; i<iniBuffer.size(); i++ ){
			temp = iniBuffer.get(i);
			currentNet.add(temp);
			temp = null;
		}
	}
	
	public void addRecord( NodeRecord newRecord ){
		NodeRecord newRecord0 = new NodeRecord(newRecord);
		currentNet.add(newRecord0);
	}
	
	public void deleteRecord( String hash ){
		for ( int i=0; i<currentNet.size(); i++ ){
			if ( currentNet.get(i).getKey() == hash ){
				currentNet.remove(i);
			}
		}
	}
	
	public void updateRecord( String hash, NodeRecord updatedRecord ){
		NodeRecord temp = null;
		for ( int i=0; i<currentNet.size(); i++ ){
			if ( currentNet.get(i).getKey() == hash ){
				temp = currentNet.get(i);
				//new information in the updateRecord is non-null, whiles the others are.
				temp.setBio( (updatedRecord.getBio()==null?temp.getBio():updatedRecord.getBio() ) );
				temp.setBirthDate( (updatedRecord.getBirthDate()==null?temp.getBirthDate():updatedRecord.getBirthDate() ) );
				temp.setFather( (updatedRecord.getFather()==null?temp.getFather():updatedRecord.getFather() ) );
				temp.setSon( (updatedRecord.getSon()==null?temp.getSon():updatedRecord.getSon() ) );
				temp.setName( (updatedRecord.getName()==null?temp.getName():updatedRecord.getName() ) );
				temp.setGender( (updatedRecord.getGender()==null?temp.getGender():updatedRecord.getGender() ) );
				temp.setProfession( (updatedRecord.getProfession()==null?temp.getProfession():updatedRecord.getProfession() ) );
				temp.setInstitution( (updatedRecord.getInstitution()==null?temp.getInstitution():updatedRecord.getInstitution() ) );
				temp.setLink( (updatedRecord.getLink()==null?temp.getLink():updatedRecord.getLink() ) );
				
				currentNet.set(i, temp);
			}
		}
	}
	
	public int size(){
		return currentNet.size();
	}
	
	public NodeRecord get( int i ){
		return currentNet.get(i);
	}
	
	public void clear(){
		currentNet = new ArrayList<NodeRecord>();
	}
	
	/////private variables
	private ArrayList<NodeRecord> currentNet = new ArrayList<NodeRecord>();
	
	/////setter and getters 
	public ArrayList<NodeRecord> getCurrentNet() {
		return currentNet;
	}
	public void setCurrentNet(ArrayList<NodeRecord> currentNet) {
		this.currentNet = currentNet;
	}

}
