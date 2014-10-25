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
    
    <title>BMS result page</title>
    
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
    <h1 align = "CENTER">Search Result :</h1> <br>
    <!-- we need a table here to display our result -->
    <table border = 1, align = "CENTER" >
    	<!-- the head of the table -->
    	<tr>
    		<td> Title </td>
    		<td> Author </td>
    		<td> Publisher </td>
    		<td> Option </td>
    	</tr>
    <s:iterator value = "bookSR" id = "i" status = "struts" >
    	<!-- the solid content -->
    	<tr>
    		<td><s:url id="detailURL" action="SHOW_DETAIL">
    				<s:param name = "currentISBN" value = "#i.ISBN" /> 
    			</s:url>
				<s:a href="%{detailURL}">
					<s:property value = "#i.Title" />
				</s:a> </td>
    		<td><s:property value = "#i.AuthorID" /> </td>
    		<td><s:property value = "#i.Publisher"/> </td>
    		<td><s:url id="editURL" action="EDIT_RECORD">
    				<s:param name = "currentISBN" value = "#i.ISBN" /> 
    			</s:url>
				<s:a href="%{editURL}">
					Edit 
				</s:a> </td>
    	</tr>
    </s:iterator>

    </table>
    
    <s:form action = "BACK_TO_START" >
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
