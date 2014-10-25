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
    	<s:submit align = "CENTER"/>
     </s:form>
     
  
     
     
    <h6 align = "CENTER" > Edition 1.0</h6> <br>
    <!-- 
    <h6>  
    	test <br> 
    	1. <s:property value = "currentIndex" /> <br>
    	2. <s:property value = "currentISBN" /> <br>
    	3. <s:property value = "bookSR.get(currentIndex).Title" /> <br>
    	4. <s:property value = "bookSR.size()" /> <br>
    	5. <s:property value = "authorSR.Name" /> <br>
    	6. <s:property value = "authorized" /> <br>
    </h6>
     -->
  </body>
</html>
