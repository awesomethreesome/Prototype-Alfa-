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
	//blank constructor
	EditBuffer(){
		
	}
	//copy constructor
	EditBuffer( EditBuffer Net ){
		NodeRecord temp = null;
		currentNet = new ArrayList<NodeRecord>();
		for ( int i = 0; i < Net.getCurrentNet().size(); i++ ){
			temp = new NodeRecord(Net.getCurrentNet().get(i));
			currentNet.add(temp);
		}
	}

	//private variables
	private List<NodeRecord> currentNet;
	
	//setter and getters 
	public List<NodeRecord> getCurrentNet() {
		return currentNet;
	}
	public void setCurrentNet(List<NodeRecord> currentNet) {
		this.currentNet = currentNet;
	}

}
