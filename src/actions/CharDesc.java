package actions;
import java.util.*;
public class CharDesc {
	public CharDesc(){
		
	}
	
	public CharDesc( NodeRecord nodeRecord ){
		hash = nodeRecord.getKey();
		name = nodeRecord.getName();
		gender = nodeRecord.getGender();
		birthDate = nodeRecord.getBirthDate();
		profession = nodeRecord.getProfession();
		institution = nodeRecord.getInstitution();
		link = nodeRecord.getLink();
		bio = nodeRecord.getBio();
	}	
	
	public String hash = "";
	public String name = "John 117";
	public String gender = "";
	public String birthYear = "";
	public String birthMonth = "";
	public String birthDate = "";
	public String profession = "";
	public String institution = "";
	public String link = "";//this suppose to be an valid URL
	public String bio = "";
}
