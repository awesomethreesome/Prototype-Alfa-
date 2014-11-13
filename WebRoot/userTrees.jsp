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
    
    <title> R-R User Repository page</title>
    
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
    <h1 align = "CENTER">Result :</h1> <br>
    <s:form action = "ADD_NODE" >
    	<s:submit value = "Add node in current level"/>
    </s:form>
    <!-- we need a table here to display our result -->
    <table border = 1, align = "CENTER" >
    	<!-- the head of the table -->
    	<tr>
    		<td> Name </td>
    		<td> Institution </td>
    		<td> Profession </td>
    		<td> Edit </td>
    		<td> Delete </td>
    		<td> Ascend</td>
    		<td> Descend </td>
    	</tr>
    <s:iterator value = "currentSons" id = "i" status = "struts" >
    	<!-- the solid content -->
    	<tr>
    		<td><s:url id="detailURL" action="DETAIL_QUERY">
    				<s:param name = "chosenKey" value = "#i.key" /> 
    			</s:url>
				<s:a href="%{detailURL}">
					<s:property value = "#i.Name" />
				</s:a> </td>
    		<td><s:property value = "#i.institution" /> </td>
    		<td><s:property value = "#i.profession"/> </td>
    		<td><s:url id="editURL" action="EDIT_QUERY">
    				<s:param name = "chosenKey" value = "#i.key" /> 
    			</s:url>
				<s:a href="%{editURL}">
					EDIT
				</s:a> </td>
			<td><s:url id="deleteURL" action="DELETE_QUERY">
    				<s:param name = "chosenKey" value = "#i.key" /> 
    			</s:url>
				<s:a href="%{deleteURL}">
					DELETE
				</s:a> </td>
			<td><s:url id="ascendURL" action="ASCEND_QUERY">
    				<s:param name = "chosenKey" value = "#i.key" />
    			</s:url>
				<s:a href="%{ascendURL}">
					ASCEND
				</s:a> </td>
    		<td><s:url id="descendURL" action="DESCEND_QUERY">
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
	
  </body>
</html>
