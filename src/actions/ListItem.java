package actions;
import java.util.*;

public class ListItem {
	/////constructor
	ListItem(){
		front = new String();
		back =  new String();
	}
	
	
	/////private variables
	String front;
	String back;
	
	/////setters and getters
	public String getFront() {
		return front;
	}
	public void setFront(String front) {
		this.front = front;
	}
	public String getBack() {
		return back;
	}
	public void setBack(String back) {
		this.back = back;
	}
}
