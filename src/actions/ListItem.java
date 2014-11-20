package actions;
import java.util.*;

public class ListItem {
	/////constructor
	public ListItem(){
		front = new String();
		back =  new String();
	}
	
	public ListItem(String front, String back) {
		this.front = front;
		this.back = back;
	}
	/////private variables
	public String front;
	public String back;
	
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
