<%@ page language="java" import="java.util.*" import="MyPack.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String node_id = request.getParameter("node_id");
String opid = request.getParameter("opid");
if(opid==null) opid="1";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="StyleSheet" href="dtree.css" type="text/css" />
    <script type="text/javascript" src="dtree.js"></script>
  </head>
  <body>
  	<jsp:useBean id="recordsegment" class="MyPack.HistorySegment" scope="session"/>
  	<script type="text/javascript">
  	tree = new dTree('tree');
  	<%
  	ArrayList<TreeNode> nodelist = recordsegment.getTree(opid);
  	String name = (String)request.getParameter("name");
    String info = (String)request.getParameter("info");
    String pid = (String)request.getParameter("pid");
    String cmd = (String)request.getParameter("submit");
    if(cmd!=null) {
    if(cmd.equals("ok")){
    try{
    Integer.parseInt(pid);
    }
    catch(Exception e) {
    pid="0";
    }
    if(Integer.parseInt(pid)==-1 || ( Methods.findNode(nodelist, pid)!=null && !Methods.isParent(nodelist, pid, node_id))) {
    Methods.findNode(nodelist, node_id).setPid(pid);
    Methods.findNode(nodelist, node_id).setName(name);
    Methods.findNode(nodelist, node_id).setFloatinfo(info);
    }
    }
    else if(cmd.equals("add student")){
    nodelist.add(new TreeNode(Methods.dispatchNextID(nodelist), Integer.parseInt(node_id), name, info));
    }
    else if(cmd.equals("delete")){
    String nodeidc = Methods.findNode(nodelist, node_id).getPid();
    Methods.deleteSubtree(nodelist, node_id);
    node_id = nodeidc;
    }
    opid=String.valueOf(Integer.parseInt(opid)+1);
    recordsegment.setTree(nodelist, opid, node_id, cmd);
    }
  	for(int i=0; i<nodelist.size(); i++) {
  	%>
	tree.add("<%=nodelist.get(i).nodeid%>","<%=nodelist.get(i).pid%>","<%=nodelist.get(i).name%>","<%="tree.jsp?node_id="+nodelist.get(i).nodeid+"&opid="+opid%>","<%=nodelist.get(i).floatinfo%>");
	<%
	}
	%>
	document.write(tree);
	</script>
    <%if (node_id!=null) {%>
    Node Number: <%=Methods.findNode(nodelist, node_id).nodeid%> <br>
  	<form action=<%="tree.jsp?node_id="+node_id+"&opid="+opid%> method=post name=form>
  	Parent Node Number: <input type="text" style="width:300px" value="<%=Methods.findNode(nodelist, node_id).pid%>" name="pid"/> <br>
    Name: <input type="text" style="width:300px" value="<%=Methods.findNode(nodelist, node_id).name%>" name="name"/> <br>
    Float Info: <input type="text" style="width:300px" value="<%=Methods.findNode(nodelist, node_id).floatinfo%>" name="info"/> <br>
    <input type="submit" value="ok" name="submit">
    <input type="submit" value="add student" name="submit">
    <input type="submit" value="delete" name="submit">
    </form>
    <br>History Records:<br>
    <jsp:include page="record.jsp"></jsp:include>
    <%
    }
    %>
  </body>
</html>
