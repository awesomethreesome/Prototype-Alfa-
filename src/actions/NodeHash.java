package actions;
import java.util.*;
/**
 * 
 * @author dell
 * description:
 * 1.provide unique hash code for each node
 * 2.life cycle: always running on the server
 * 3.hash code whose length is 5 consists of number 0-9 and character a-z   
 */
public class NodeHash {
	/////constructor
	NodeHash(){
		for (int i=0; i<length; i++){
			nextHash[i] = '0';
		}
		bitIndex = 0;
	}
	
	public String generateHash(){
		String hash = "", temp = "";
		for (int i =0; i<length; i++){
			temp = "";
			temp = String.format("%c", nextHash[i]);
			hash += temp;
		}
		updateHash();
		return hash;
	}
	
	public void clear(){
		for (int i=0; i<length; i++){
			nextHash[i] = '0';
		}
		bitIndex = 0;
	}
	
	/////private methods
	private void updateHash(){//48-57  97-122
		try{
			int i=0;
			for (i=0; i<length; i++) {
				if ( digitIncrement(i) == false ){
					break;
				}
			}
			if ( i == length ){//overflow
				System.out.println("Hash overflow");
			}
				
		}catch (Exception e){
			System.out.println("Fail generating hashcode");
		}
	}
	
	private boolean digitIncrement( int i) throws Exception{//if needs to carry digit, return true
		if ( i <= length){
			return false;
		}
		if ( (nextHash[i]<57 && nextHash[i]>=48) || (nextHash[i]<122 && nextHash[i]>=97) ){
			nextHash[i] += 1;
			return false;
		}
		else if ( nextHash[i] == 57 ){
			nextHash[i] = 97;
			return false;
		}
		else if ( nextHash[i] == 122 ){
			nextHash[i] = '0';
			return true;
		}
		else{
			Exception e = new Exception();
			throw e; 
		} 
	}
	
	/////private variable
	private char[] nextHash;
	private static int length = 5;
	private int bitIndex;
}
