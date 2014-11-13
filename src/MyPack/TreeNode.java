package MyPack;
public class TreeNode {
	public String nodeid = new String();
	public String pid = new String();
	public String floatinfo = new String();
	public String name = new String();
	public TreeNode(int id, int pid, String name, String float_info) {
		nodeid = String.valueOf(id);
		this.pid = String.valueOf(pid);
		floatinfo = new String(float_info);
		this.name = new String(name);
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getFloatinfo() {
		return floatinfo;
	}
	public void setFloatinfo(String floatinfo) {
		this.floatinfo = floatinfo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TreeNode copy() {
		return new TreeNode(Integer.parseInt(nodeid), Integer.parseInt(pid), name, floatinfo);
	}
}
