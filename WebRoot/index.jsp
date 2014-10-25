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
    
    <title>Book Management System starting page</title>
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
    <h1 align = "CENTER">Welcome <s:property value = "userName"></s:property> </h1> <br>
    
    <s:form align = "CENTER" action = "SEARCH_QUERY">
    	<s:textfield name = "queryInput" align = "CENTER" />
    	<s:radio name = "queryType" label = "Search Option*" list = "{'Name', 'Institution', 'Profession', 'N\A'}"/>
    	<s:submit value = "Search" align = "CENTER"/>
    </s:form>
     
  	<s:form action = "MANAGE_QUERY" align = "CENTER">
  		<s:submit value = "My Trees"  />
  	</s:form>
     
     
    <h6 align = "CENTER" > Edition 1.0</h6> <br>
    
  </body>
</html>
