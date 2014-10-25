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
    
    <title>R-R detail page</title>
    
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
    <h1 align = "CENTER"> Detail Information of Chosen Node</h1> <br>
    
    <table border = 1, align = "CENTER" >
    	<!-- the head of the table -->
    	<tr>
    		<td> Name </td>
    		<td>  </td>
    		<td> AuthorID </td>
    		<td> Author </td>
    		<td> Publisher </td>
    		<td> PublishDate </td>
    		<td> Price </td>
    	</tr>
    	<!-- solid info -->
    	<tr>
    		<td> <s:property value = "currentBook.ISBN" /> </td>
    		<td> <s:property value = "currentBook.Title" /> </td>
    		<td> <s:property value = "currentBook.AuthorID" /> </td>
    		<td> <s:property value = "authorSR.Name" /> </td>
    		<td> <s:property value = "currentBook.Publisher" /> </td>
    		<td> <s:property value = "currentBook.PublishDate" /> </td>
    		<td> <s:property value = "currentBook.Price" /> </td>
    	</tr>
    </table>   
     
    <s:form action = "BACK_TO_START" >
    	<s:submit value = "Backward" />
    </s:form>
    
  </body>
</html>
