package MyPack;
import java.util.*;
public class HistorySegment {
	private ArrayList<ArrayList<TreeNode>> records;
	private ArrayList<String> operations;
	private ArrayList<String> ob_node_id;
	private int size = 1;
	public HistorySegment() {
		records = new ArrayList<ArrayList<TreeNode>>();
		operations = new ArrayList<String>();
		ob_node_id = new ArrayList<String>();
		ArrayList<TreeNode> init_tree = new ArrayList<TreeNode>();
		init_tree.add(new TreeNode(1, -1, "root", "Care to add something here?"));
		records.add(init_tree);
		operations.add("New tree.");
		ob_node_id.add("1");
	}
	public ArrayList<TreeNode> getTree(String operation_id) {
		System.out.println("size of segment is "+String.valueOf(records.size()));
		int opid = Integer.parseInt(operation_id) - 1;
		ArrayList<TreeNode> ret = new ArrayList<TreeNode>();
		for(int i=0; i<records.get(opid).size();i++)
			ret.add(records.get(opid).get(i).copy());
		return ret;
	}
	public void setTree(ArrayList<TreeNode> nodelist, String operation_id, String node_id, String optype) {
		String operation = "Unknown operation";
		if(optype.equals("add student"))
			operation = "Add student  to teacher "  + Methods.findNode(nodelist, node_id).getName();
		else if(optype.equals("ok"))
			operation = "Edit " + Methods.findNode(nodelist, node_id).getName();
		else if(optype.equals("delete"))
			operation = "Delete student(s) from teacher " + Methods.findNode(nodelist, node_id).getName();
		if(Integer.parseInt(operation_id)>records.size()) {
			records.add(nodelist);
			operations.add(operation);
			ob_node_id.add(node_id);
			size++;
		}
		else {
			int opid = Integer.parseInt(operation_id) - 1;
			records.set(opid, nodelist);
			operations.set(opid, operation);
			ob_node_id.set(opid, node_id);
			size = opid;
		}
	}
	public String getTable() {
		String tablecmd = new String();
		for(int i = 0; i < size; i++) {
			tablecmd =tablecmd + "<a href=\"tree.jsp?opid="+String.valueOf(i+1)+"&node_id="+ob_node_id.get(i)+"\">"+operations.get(i)+"</a><br> ";
		}
		return tablecmd;
	}
}
