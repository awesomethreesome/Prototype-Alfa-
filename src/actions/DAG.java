package actions;
import java.util.*;
public class DAG {
	public DAG(){
		src = new String();
		info = new ArrayList<String>();
		dst = new ArrayList<String>();
	}
	
	public DAG(DAG dag){
		src = dag.src;
		dst = new ArrayList<String>(dag.dst);
	}
	
	public String src;
	public ArrayList<String> info;
	public ArrayList<String> dst;
}
