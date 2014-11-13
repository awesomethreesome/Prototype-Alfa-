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
    
    <title>R-R error page</title>
    
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
    <h1 align = "CENTER">This is an BMS error page. </h1><br>
    <h3 align = "CENTER">
    	If you are watching this page, that means an error* occurred during the process,please push the button below to return to starting page.
    </h3>
    <h4>
    	*Most likely, the error is caused by one or more of those operations listed below: <br>
    	 1. attempting to insert or import a record which causes failure in database. <br>
    	 2. attempting to conduct operations(such as delete and edit) to a record which does not existed. <br> 
    </h4>
    <s:form align = "CENTER" action = "ERROR_RECOVER">
    	<s:submit value = "Restart" />
    </s:form>
    
  </body>
</html>
