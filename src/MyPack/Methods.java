package MyPack;
import java.util.*;
public class Methods {
	public static TreeNode findNode(ArrayList<TreeNode> nodelist, String nodeid) {
		for(int i=0; i<nodelist.size(); i++)
			if (nodelist.get(i).getNodeid().equals(nodeid))
				return nodelist.get(i);
		return null;
	}
	public static boolean isParent(ArrayList<TreeNode> nodelist, String id, String pid) {
		while(Integer.parseInt(id)>0) {
			if(id.equals(pid)) return true;
			id = findNode(nodelist, id).getPid();
		}
		return false;
	}
	public static int dispatchNextID(ArrayList<TreeNode> nodelist) {
		int ret = 0;
		for(int i=0; i<nodelist.size(); i++)
			if(Integer.parseInt(nodelist.get(i).getNodeid())>ret)
				ret = Integer.parseInt(nodelist.get(i).getNodeid());
		return ret+1;
	}
	public static void deleteSubtree(ArrayList<TreeNode> nodelist, String id) {
		for(int i=0; i<nodelist.size(); i++) {
			if(isParent(nodelist, nodelist.get(i).getNodeid(), id)) {
				nodelist.remove(i--);
			}
		}
	}
}
