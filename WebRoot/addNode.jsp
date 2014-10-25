<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title> R-R Add New Node page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <h1 align = "CENTER" > Add Node </h1> <br>
    <h2> New Node Info* </h2>
    <s:form align = "CENTER" action = "ADD_QUERY" >
    	<s:textfield name = "upName" label = "New Name"/>
    	<s:textfield name = "upAge" label = "New Age"/>
    	<s:textfield name = "upPro" label = "New Profession"/>
    	<s:textfield name = "upIns" label = "New Institution"/>
    	<s:textfield name = "upLink" label = "New Link"/>
    	<s:submit value = "Add"/>
    </s:form> 
    <h5 align = "CENTER" > *Fill every blank to avoid error. </h5>
    
    <s:form action = "BACK_TO_USERTREES" >
    	<s:submit value = "Backward" />
    </s:form>
  </body>
</html>
