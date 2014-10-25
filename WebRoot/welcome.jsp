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
    
    <title> R-R log-in page</title>
    
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
   <h1 align = "CENTER">  Welcome to R-R System </h1> <br>
   
   <h3 align = "CENTER">User Login </h3>
     <s:form align = "CENTER" action = "USER_LOGIN" >
     	<s:textfield name = "inputUserName"  label = "UserName" align = "CENTER"/>
     	<s:textfield name = "inputPassword"  label = "Password" align = "CENTER"/>
     	<s:submit value = "Login" />
     </s:form>
  </body>
</html>
