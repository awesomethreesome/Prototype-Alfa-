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
    
    <title> BMS record edit page</title>
    
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
    <h1 align = "CENTER" > Edit Record </h1> <br>
    <h2> Update Record* </h2>
    <s:form align = "CENTER" action = "UPDATE_BOOK" >
    	<s:textfield name =  "upISBN" label = "Target ISBN"/>
    	<!--<s:textfield name = "upTitle" label = "Title"/> -->
    	<s:textfield name = "upAuthorID" label = "AuthorID"/>
    	<s:textfield name = "upPub" label = "Publisher"/>
    	<s:textfield name = "upPubDate" label = "Publish Date"/>
    	<s:textfield name = "upPrice" label = "Price"/>
    	<!-- 
    	<s:radio name = "isUpAuthor" label = "Insert Author Info*" list = "{'Yes', 'No'}" />
    	<s:textfield name = "upName" label = "AuthorName"/>
    	<s:textfield name = "upAge" label = "Age"/>
    	<s:textfield name = "upCountry" label = "Country"/>
    	 -->
    	<s:submit />
    </s:form> 
    <h5 align = "CENTER" > *Fill every blank to avoid error. </h5>
    <s:form action = "DELETE_BOOK" >
    	<s:submit value = "Delete Record " />
    </s:form>
    
    <s:form action = "BACK_TO_SHOWRESULT" >
    	<s:submit value = "Backward" />
    </s:form>
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
