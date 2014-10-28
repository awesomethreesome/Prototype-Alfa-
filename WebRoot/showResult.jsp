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
    
    <title> R-R result page</title>
    
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
    		<td> Ascend </td>
    		<td> Name </td>
    		<td> Institution </td>
    		<td> Profession </td>
    		<td> Descend </td>
    	</tr>
    <s:iterator value = "currentSons" id = "i" status = "struts" >
    	<!-- the solid content -->
    	<tr>
    		<td><s:url id="ascendURL" action="ASCEND_QUERY">
    				<s:param name = "chosenKey" value = "#i.key" /> 
    			</s:url>
				<s:a href="%{ascendURL}">
					ASCEND
				</s:a> </td>
    		<td><s:url id="detailURL" action="DETAIL_QUERY_IN_SEARCHING">
    				<s:param name = "chosenKey" value = "#i.key" /> 
    			</s:url>
				<s:a href="%{detailURL}">
					<s:property value = "#i.Name" />
				</s:a> </td>
    		<td><s:property value = "#i.institution" /> </td>
    		<td><s:property value = "#i.profession"/> </td>
    		<td><s:url id="descendURL" action="DESCEND_QUERY_IN_SEARCHING">
    				<s:param name = "chosenKey" value = "#i.key" />
    			</s:url>
				<s:a href="%{descendURL}">
					DESCEND
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
