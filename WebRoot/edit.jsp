<%@ page language="java" import="java.util.*" import="actions.*" contentType="text/html; charset=UTF-8"
%>
    <jsp:useBean id="Generator" class="actions.NodeHash" scope="application"/>
    <jsp:useBean id="Model" class="actions.MainProcedure" scope="session"/>
	<%
		Model.hashGenerator = Generator;
		String submit = request.getParameter("submittype");
		String account = null;
		String password = null;
		String rememberStr = null;
		String searchQuery = null;
		boolean logedIn = Model.authorized;
		boolean rememberMe = false;
		boolean expandSearchList = false;
		boolean edit = false;
		boolean addFail =false;
		CharDesc charDesc = null;
		boolean linkSucess = false;
		boolean severSucess = false;
		String hash = request.getParameter("TargetHash");
		if(submit!=null && submit.equals("Back2History")) {
			Model.back2History(request.getParameter("HistoryHash"));
		}
		else if(submit!=null && submit.equals("LogIn")) {
			account = request.getParameter("Account");
			password = request.getParameter("Password");
			Model.logInCheck(account, password);
			if(Model.authorized == true) {
		logedIn = true;
		rememberStr = request.getParameter("RememberMe");
			}
		}
		else if(submit!=null && submit.equals("LogOut")) {
			logedIn = Model.authorized = false;
		}
		else if(submit!=null && submit.equals("Search")) {
			searchQuery = request.getParameter("Search");
			//throw new Exception(searchQuery);
			Model.search(searchQuery,null);
			expandSearchList = true;
		}
		else if(submit!=null && submit.equals("Edit")) {
			edit = true;
			charDesc = new CharDesc();
			charDesc.hash = hash;
			charDesc.name = request.getParameter("CharName");
			charDesc.gender = request.getParameter("gender");
			charDesc.birthDate = request.getParameter("BirthYear")+"-"+request.getParameter("BirthMonth")+"-"+request.getParameter("BirthDate");
			charDesc.institution = request.getParameter("Institution");
			charDesc.profession = request.getParameter("Profession");
			Model.edit(charDesc);
		}
		else if(submit!=null && submit.equals("Sync")) {
			Model.syncDB();
			Model.synced = true;
		}
		else if(submit!=null && submit.equals("New")) {
			edit = true;
			charDesc = new CharDesc();
			charDesc.name = request.getParameter("CharName");
			charDesc.birthDate = request.getParameter("BirthYear")+"-"+request.getParameter("BirthMonth")+"-"+request.getParameter("BirthDate");
			charDesc.institution = request.getParameter("Institution");
			charDesc.profession = request.getParameter("Profession");
			String addRst = Model.add(charDesc);
			if(addRst==null) {
				addFail = true;
				if(charDesc.birthDate == null) throw new Exception("invalid birthDate = null"); 
				if(charDesc.birthDate.length()!=10) throw new Exception("invalid birthDate="+charDesc.birthDate);
				charDesc.birthYear = charDesc.birthDate.substring(0, 4);
				charDesc.birthMonth = charDesc.birthDate.substring(5, 7);
				charDesc.birthDate = charDesc.birthDate.substring(8, 10);
			}
			else{
				hash = addRst;
				}
		}
		else if(submit!=null && submit.equals("DeleteChar")) {
			//throw new Exception(hash);
			Model.delete(hash);
		}
		else if(submit!=null && submit.equals("Link")) {
			linkSucess = Model.link2(request.getParameter("LinkSource"), request.getParameter("LinkTarget"), request.getParameter("LinkDate"));
		}
		else if(submit!=null && submit.equals("Sever")) {
			severSucess = Model.sever2(request.getParameter("LinkSource"), request.getParameter("LinkTarget"));
		}
		else if(submit!=null && submit.equals("LinkAddSon")) {
			linkSucess = Model.link(hash, request.getParameter("linkhash"), request.getParameter("LinkDate"));
		}
		else if(submit!=null && submit.equals("LinkAddFather")) {
		//throw new Exception(hash+";"+request.getParameter("linkhash")+";"+request.getParameter("LinkDate"));
			linkSucess = Model.link(request.getParameter("linkhash"), hash, request.getParameter("LinkDate"));
		}
		else if(submit!=null && submit.equals("SeverAsSon")) {
			severSucess = Model.sever(hash, request.getParameter("linkhash"));
		}
		else if(submit!=null && submit.equals("SeverAsFather")) {
			severSucess = Model.sever(request.getParameter("linkhash"), hash);
		}
		else if(submit!=null && submit.equals("Register")) {
			account = request.getParameter("Account");
			password = request.getParameter("Password");
			logedIn = Model.signIn("", account, password);
		}
		if(hash!=null) {
			charDesc = Model.get(hash);
			if(charDesc==null) hash=null;
			else{
			if(charDesc.birthDate == null) throw new Exception("invalid birthDate = null, char name="+charDesc.name+";hash="+charDesc.hash); 
			if(charDesc.birthDate.length()!=10) throw new Exception("invalid birthDate="+charDesc.birthDate+";char name="+charDesc.name+";hash="+charDesc.hash);
			charDesc.birthYear = charDesc.birthDate.substring(0, 4);
			charDesc.birthMonth = charDesc.birthDate.substring(5, 7);
			charDesc.birthDate = charDesc.birthDate.substring(8, 10);
			}
		}
		if(submit!=null && (submit.equals("Edit")||submit.equals("New")||submit.equals("DeleteChar")||submit.equals("Link")||submit.equals("Sever")||submit.equals("LinkAddSon")||submit.equals("LinkAddFather")||submit.equals("SeverAsSon")||submit.equals("SeverAsFather")))
		Model.synced = false;
	%>
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- <title>Edit</title> -->
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/bootswatch.css" rel="stylesheet">
	<!-- Functions stay here! -->
	<!--<script type="text/javascript" src="http://use.typekit.com/mxh7kqd.js"></script>-->
	<script language="javascript" type="text/javascript">
	var SaveBufferTimer = 0;
	function SaveBuffer() {
		//TODO : add function here
		if(SaveBufferTimer == 0) {
			document.getElementById("SaveBufferSync").innerText="Just a few sec to go...";
			$("#SaveBufferDiv").fadeIn(1000); 
			$("#SaveBufferSync").fadeIn(1000); 
		}
		HideSaveButton();
		document.getElementById("SaveBufferProgressBar").style.width=String(SaveBufferTimer)+"%";
		if(SaveBufferTimer==120) {
			$("#SaveBufferSync").fadeOut(1000); 
		}
		if(SaveBufferTimer==140) {
			document.getElementById("SaveBufferSync").innerText="Forming request now...";
			$("#SaveBufferSync").fadeIn(600);
		}
		if(SaveBufferTimer==160) {
			SaveBufferTimer = 0;
			$("#SaveBufferSync").fadeOut(1500);
			$("#SaveBufferDiv").fadeOut(1500);
			document.Targeting.submittype.value = "Sync";
			document.Targeting.submit();
		}
		SaveBufferTimer += 2;
		setTimeout("SaveBuffer();", 100);
	}
	function EditInfoOK() {
		document.EditInfo.gender.value = document.EditInfo.selectlist.value;
		if (!(CheckDate()&&CheckName())) return;
		document.EditInfo.submit();	
	}
	function CheckName() {
		 var regexp = /[a-zA-Z ]/;
		 if(!regexp.test(document.EditInfo.CharName.value)) {
			 ShowAlert("Invalid format of name. English letters only.");
			 }
		 return regexp.test(document.EditInfo.CharName.value);
	}
	function CheckDate() {
		if(parseInt(document.EditInfo.BirthYear.value)>=1900 && parseInt(document.EditInfo.BirthYear.value)<=2014) {
		document.EditInfo.BirthYear.value = "" + parseInt(document.EditInfo.BirthYear.value);
		if(parseInt(document.EditInfo.BirthMonth.value)>=1 && parseInt(document.EditInfo.BirthMonth.value)<=12) {
			if(parseInt(document.EditInfo.BirthMonth.value)<10)
			document.EditInfo.BirthMonth.value = "0" + parseInt(document.EditInfo.BirthMonth.value);
			else
				document.EditInfo.BirthMonth.value = "" + parseInt(document.EditInfo.BirthMonth.value);
			var date = 31;
			if(parseInt(document.EditInfo.BirthMonth.value) == 4 || parseInt(document.EditInfo.BirthMonth.value) == 6 ||
					parseInt(document.EditInfo.BirthMonth.value) == 9 ||parseInt(document.EditInfo.BirthMonth.value) == 12)
				date = 30;
			if(parseInt(document.EditInfo.BirthMonth.value)==2) {
				if((parseInt(document.EditInfo.BirthYear.value)%4)==0 && parseInt(document.EditInfo.BirthYear.value)!=1900)
					date = 29;
				else date = 28;
			}
			}
			if(parseInt(document.EditInfo.BirthDate.value)>=1 && parseInt(document.EditInfo.BirthDate.value)<=date) {
				if(parseInt(document.EditInfo.BirthDate.value)<10)
					document.EditInfo.BirthDate.value = "0" + parseInt(document.EditInfo.BirthDate.value);
				else
					document.EditInfo.BirthDate.value = "" + parseInt(document.EditInfo.BirthDate.value);
				return true;
			}
	}
	ShowAlert("Invalid format of date!");
	return false;
	}
	function AddNew() {
		document.EditInfo.gender.value = document.EditInfo.selectlist.value;
		if (!(CheckDate()&&CheckName())) return;
		document.EditInfo.submittype.value = "New";
		document.EditInfo.submit();	
	}
	function ShowSaveButton() {
		$("#SaveBufferButton").fadeIn(600); 
	}
	function HideSaveButton() {
		$("#SaveBufferButton").fadeOut(600); 
	}
	function FlashSaveButton() {
		ShowSaveButton();
		HideSaveButton();
		ShowSaveButton();
	}
	function DeleteThis(elemID) {
		var elem = document.getElementById(elemID);
		var parelm = elem.parentNode;
		parelm.removeChild(elem);
	}
	var lengthSectionA=0;
	var lengthSectionC=0;
	var lengthSectionB=0;
	var lengthSectionD=0;
	function Add2Storage(storageID, elemHash, elemName) {
		var elemID = storageID + '_' + elemHash;
		var storage;
		var sectionSpace;
		switch(storageID) {
		case 'ListSectionA':
			storage = 'storage section A.';
			sectionSpace = lengthSectionA;
			break;
		case 'ListSectionB':
			storage = 'storage section B.';
			sectionSpace = lengthSectionB;
			break;
		case 'ListSectionC':
			storage = 'storage section C.';
			sectionSpace = lengthSectionC;
			break;
		default:
			storage = 'storage section D.';
			sectionSpace = lengthSectionD;
		}
		if(document.getElementById(elemID) != null) {
			ShowAlert(elemName+' is already in ' + storage);
			return;
		}
		if(sectionSpace >= 10) {
			ShowAlert('There are already '+sectionSpace+' elements in '+storage+' A section may contain at most 10 elements.');
			return;
		}
		var storage = JSON.parse(getCookie("Ts"));
		if(storage==null)
			storage = new Array();
		var newItem = new Object();
		newItem.a = storageID.substring(storageID.length-1);
		newItem.b = elemHash;
		newItem.c = elemName;
		storage[storage.length] = newItem;
		var storageString = JSON.stringify(storage);
		if(storageString.length>2048) {
			ShowAlert('Storage space is full. Try to delete some items to free some space.');
			return;
		}
		setCookie('Ts', storageString, 1);
		Write2Storage(storageID, elemHash, elemName);
		switch(storageID) {
		case 'ListSectionA':
			lengthSectionA++;
			break;
		case 'ListSectionB':
			lengthSectionB++;
			break;
		case 'ListSectionC':
			lengthSectionC++;
			break;
		default:
			lengthSectionD++;
		}
	}
	function Write2Storage(storageID, elemHash, elemName) {
		var elemID = storageID + '_' + elemHash;
		var writeElem = '<a class="dropdown-toggle" data-toggle="dropdown" href="#">'+elemName+'<b class="caret"></b></a>';
		writeElem = writeElem + '<ul class="dropdown-menu"> <li><a href="javascript:AddAsSon('+ "'" + elemHash +"'" + ')">Add as student</a></li> <li><a href="javascript:AddAsFather('+ "'" + elemHash +"'" + ')">Add as teacher</a></li> <li><a href="javascript:DeleteAsSon('+"'" +  elemHash +"'" + ')">Delete as student</a></li> <li><a href="javascript:DeleteAsFather('+ "'" + elemHash +"'" + ')">Delete as teacher</a></li>';
		writeElem = writeElem + '<li class="divider"></li><li><a href="javascript:DeleteFromStorage(';
		writeElem = writeElem + "'" + elemID +"'" + ')">Remove</a></li></ul>';
		var storage = document.getElementById(storageID);
		var newelem = document.createElement("li");
		var lastChild = document.getElementById(storageID+'_Last');
		newelem.setAttribute("id", elemID);
		newelem.setAttribute("class", 'dropdown');
		newelem.innerHTML = writeElem;
		storage.insertBefore(newelem,lastChild);
	}
	function DeleteFromStorage(elemID) {
		var storageLetter = elemID.substring(11,12);
		var hash = elemID.substring(13);
		var storage = JSON.parse(getCookie("Ts"));
		var i=0;
		var found = 0;
		for(;i<storage.length;i++) {
			if(storage[i].a == storageLetter && storage[i].b == hash) {
				found=1;
				break;
			}
		}
		for(;i<storage.length-1;i++) {
			storage[i] = storage[i+1];
		}
		if(found)
			delete storage[storage.length-1];
		DeleteThis(elemID);
		var storageString = JSON.stringify(storage);
		if(storageString=="[null]") {
			setCookie('Ts', "", -1);
		}
		else
			setCookie('Ts', storageString, 1);
	}
	function DeleteList(storageID) {
		var storage = JSON.parse(getCookie("Ts"));
		if(storage==null) return;
		for(i=0;i<storage.length;i++) {
			if(storageID.substring(11)==storage[i].a)
			DeleteFromStorage(storageID+'_'+storage[i].b);
		}
	}
	var currpage = 1;
	var totalpages = <%=(Model.searchList.size()/10+1)%>;
	var allowshowsearchlist = 1;
	function Allowshowsearchlist() {
		allowshowsearchlist = 1;
	}
	function HideSearchList() {
		allowshowsearchlist = 0;
		$("#SearchListFrame").fadeOut(600);
		setTimeout("Allowshowsearchlist()",600)
	}
	function UnhideSearchList() {
		if(allowshowsearchlist)
			$("#SearchListFrame").fadeIn(600);
	}
	function FormTargeting(hash) {
		document.Targeting.TargetHash.value = hash;
		document.Targeting.submit();
	}
	function ShowSearchList() {
		UnhideSearchList();
		var elem = document.getElementById("SearchList");
		var elemwrite = '<li class="nav-header"  onclick="HideSearchList()">'+resultlist.length+' Result(s) in total. Click here to hide.</li>';
		for(var i=(currpage-1)*10; i<currpage*10; i++)
			if(i<resultlist.length)
				elemwrite = elemwrite + '<li><a href="javascript:FormTargeting('+"'"+resultlist[i].back+"'"+');">'+(i+1)+'.  '+resultlist[i].front+'</a></li>';
			else
				elemwrite = elemwrite + '<li><a href="javascript:void(0);"><br></a></li>';
		elemwrite = elemwrite + '<li class="divider"></li><div class="pagination"><ul>';
		if (totalpages<10) {
			for(var i=1; i<=totalpages; i++) {
				if(currpage == i)
					elemwrite = elemwrite + '<li class="active"><a href="javascript:{currpage='+i+';ShowSearchList();}">'+i+'</a></li>';
				else
					elemwrite = elemwrite + '<li><a href="javascript:{currpage='+i+';ShowSearchList();}">'+i+'</a></li>';		
			}
		}
		else {
			elemwrite = elemwrite + '<li><a href="javascript:{currpage=1;ShowSearchList();}">1</a></li>';
			if(currpage>2)
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+(currpage-2)+';ShowSearchList();}">&larr;</a></li>';
			else
				elemwrite = elemwrite + '<li><a href="javascript:{currpage=1;ShowSearchList();}">&larr;</a></li>';	
			elemwrite = elemwrite + '<li class="disabled"><a href="javascript:void(0);">...</a></li>';
			if(currpage == 1) {
				elemwrite = elemwrite + '<li class="active"><a href="javascript:{currpage=1;ShowSearchList();}">1</a></li>';
				elemwrite = elemwrite + '<li><a href="javascript:{currpage=2;ShowSearchList();}">2</a></li>';
				elemwrite = elemwrite + '<li><a href="javascript:{currpage=3;ShowSearchList();}">3</a></li>';
			}
			else if(currpage == totalpages) {
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+(currpage-2)+';ShowSearchList();}">'+(currpage-2)+'</a></li>';
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+(currpage-1)+';ShowSearchList();}">'+(currpage-1)+'</a></li>';
				elemwrite = elemwrite + '<li class="active"><a href="javascript:{currpage='+currpage+';ShowSearchList();}">'+currpage+'</a></li>';
			}
			else {
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+(currpage-1)+';ShowSearchList();}">'+(currpage-1)+'</a></li>';
				elemwrite = elemwrite + '<li class="active"><a href="javascript:{currpage='+currpage+';ShowSearchList();}">'+currpage+'</a></li>';
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+(currpage+1)+';ShowSearchList();}">'+(currpage+1)+'</a></li>';
			}
			elemwrite = elemwrite + '<li class="disabled"><a href="javascript:void(0);">...</a></li>';
			if(currpage<totalpages-1)
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+(currpage+2)+';ShowSearchList();}">&rarr;</a></li>';
			else
				elemwrite = elemwrite + '<li><a href="javascript:{currpage='+currpage+';ShowSearchList();}">&rarr;</a></li>';
			elemwrite = elemwrite + '<li><a href="javascript:{currpage='+totalpages+';ShowSearchList();}">'+totalpages+'</a></li>';
		}
		elem.innerHTML = elemwrite;
	}
	function HideNaviBar() {
		$("#nvbar").fadeIn(600);
	}
	function ShowNaviBar() {
		$("#nvbar").fadeOut(600); 
	}
	function FlashNaviBar() {
		setTimeout("HideNaviBar();",600);
		setTimeout("FlashNaviBar();",1200);
	}
	function ShowAlert(msg) {
		document.getElementById("AlertMsg").innerText=msg;
		UnHideAlert();
		HideAlert();
		UnHideAlert();
	}
	function HideAlert() {
		$("#Alert").fadeOut(600); 
	}
	function UnHideAlert() {
		$("#Alert").fadeIn(600); 
	}
	var historyList = new Array();
	<%
		for(int i=0;i<Model.history.size();i++) {
			%>
			listItem = new Object();
			listItem.front = '<%=Model.history.ModRecord(i)%>';
			listItem.back = '<%=i%>';
			historyList[<%=i%>] = listItem;
			<%
		}
	%>
	var resultlist = new Array();
	<%
		for(int i=0; expandSearchList &&  i<Model.searchList.size();i++) {
			%>
			listItem = new Object();
			listItem.front = '<%=Model.searchList.get(i).front%>';
			listItem.back = '<%=Model.searchList.get(i).back%>';
			resultlist[<%=i%>] = listItem;
			<%
		}
	%>
	function WriteHistoryList() {
		var elemwrite = "";
		for(var i=0;i<historyList.length; i++)
			elemwrite = elemwrite + '<li><a onclick="BackHistory('+"'"+historyList[i].back+"'"+')">'+historyList[i].front+'</a></li>';
		elemwrite = elemwrite + '<li class="divider"></li><li><a>'+historyList.length+' record(s) in total.</a></li>';
		document.getElementById("HistoryList").innerHTML=elemwrite;
	}
	function WriteStorage() {
		var storage = JSON.parse(getCookie("Ts"));
		if(storage!=null) {
			for(var i=0; i<storage.length; i++) {
				Write2Storage('ListSection'+storage[i].a, storage[i].b, storage[i].c);
				switch(storage[i].a) {
				case 'A':
					lengthSectionA++;
					break;
				case 'B':
					lengthSectionB++;
					break;
				case 'C':
					lengthSectionC++;
					break;
				default:
					lengthSectionD++;
					break;
				}
			}
		}
	}
	function getCookie(c_name){
		if (document.cookie.length>0){
			c_start=document.cookie.indexOf(c_name + "=");
			if (c_start!=-1){ 
				c_start=c_start + c_name.length+1;
				c_end=document.cookie.indexOf(";",c_start);
				if (c_end==-1) c_end=document.cookie.length;
				return unescape(document.cookie.substring(c_start,c_end));
			} 
		}
		return null;
	}
	function setCookie(c_name, value, expiredays){
		var exdate=new Date();
		exdate.setDate(exdate.getDate() + expiredays);
		document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
	}
	function ShowHidePassword() {
		if(document.getElementById("ShowPassword").checked == true) {
			document.LogIn.Password.type="text";
		}
		else {
			document.LogIn.Password.type="password";
		}
	}
	function BackHistory(hash) {
		document.Back2History.HistoryHash.value=hash;
		document.Back2History.submit();
	}
	function FormLogIn() {
		document.LogIn.RememberMe.value = "false";
		if(document.LogIn.RememberMe.checked)
			document.LogIn.RememberMe.value = "true";
		document.LogIn.submit();
	}
	function FormRegister() {
		document.LogIn.RememberMe.value = "false";
		if(document.LogIn.RememberMe.checked)
			document.LogIn.RememberMe.value = "true";
		document.LogIn.submittype.value = "Register";
		document.LogIn.submit();
	}
	function FormSearch() {
		document.Search.submit();
	}
	function LogOut() {
		document.Back2History.submittype.value = "LogOut";
		document.Back2History.submit();
	}
	function DeleteChar() {
		document.EditInfo.submittype.value ="DeleteChar";
		document.EditInfo.submit();
	}
	function CreateLink() {
		document.LinkSever.submittype.value = "Link";
		document.LinkSever.submit();
	}
	function DeleteLink() {
		document.LinkSever.submittype.value = "Sever";
		document.LinkSever.submit();
	}
	function AddAsSon(sonHash) {
		document.LinkSever.submittype.value = "LinkAddSon";
		document.LinkSever.linkhash.value = sonHash;
		document.LinkSever.submit();
	}
	function AddAsFather(fatherHash) {
		document.LinkSever.submittype.value = "LinkAddFather";
		document.LinkSever.linkhash.value = fatherHash;
		document.LinkSever.submit();
	}
	function DeleteAsSon(sonHash) {
		document.LinkSever.submittype.value = "SeverAsSon";
		document.LinkSever.linkhash.value = sonHash;
		document.LinkSever.submit();
	}
	function DeleteAsFather(fatherHash) {
		document.LinkSever.submittype.value = "SeverAsFather";
		document.LinkSever.linkhash.value = fatherHash;
		document.LinkSever.submit();
	}
	<%if(logedIn && rememberStr!=null && rememberStr.equals("true")) {%>
	setCookie('acn', '<%=account%>', 0);
	setCookie('psd', '<%=password%>', 0);
	<%}%>
	var psd = getCookie('psd');
	var acn = getCookie('acn');
	</script>
</head>

<body class="preview" id="top" data-spy="scroll" data-target=".subnav" data-offset="80" >
	 
 <div class="navbar navbar-fixed-top" id="nvbar"> <!-- Top Navigation Bar -->
   <div class="navbar-inner">
     <div class="container">
       <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </a>
       <a class="brand" href="http://down.admin5.com/">RelaNet</a>
       <div class="nav-collapse collapse" id="main-menu">
        <ul class="nav" id="main-menu-left">
          <li><a onclick="pageTracker._link(this.href); return false;" href="#">News</a></li>
          <li><a id="swatch-link" href="#">Forum</a></li>
          <%if(logedIn) {%>
          <li class="dropdown">
          <a class="dropdown-toggle" data-toggle="dropdown" href="#" onclick="WriteHistoryList()">History <b class="caret"></b></a>
          	<ul class="dropdown-menu" id="HistoryList">
            </ul>
          </li>
          <%} %>
          <li class="dropdown" id="SaveBufferButton" style="display:none">
            <a class="dropdown-toggle" data-toggle="dropdown" onclick="SaveBuffer()" href="javascript:void(0);" >Save </a>
          </li>
          <%if(logedIn) {%>
          <li><a  href="javascript:LogOut()">Log out</a></li>
          <%} %>
        </ul>
	   </div>
	   <div class="span12" id="SaveBufferDiv" style="display:none;">
		<div class="progress">
		<div class="bar" id="SaveBufferProgressBar" style="width: 0%;"></div>
		</div>
		<h4  class="alert-heading" id="SaveBufferSync" style="display:none;">Synchronizing DataBase...</h4>
	   </div>
	 </div>
     <div class="span12" id="Alert" style="display:none;">
        <div class="alert alert-block" style="background:transparent;">
          <a class="close" onclick="HideAlert()" style="color:white;">&times;</a>
          <h4 class="alert-heading" id="AlertMsg"></h4>
        </div>
    </div>
   </div>
   
 </div>
 
 <form action="" method="post" name="Back2History">
 <input type = "hidden" name = "HistoryHash" value="">
 <input type = "hidden" name= "submittype" value="Back2History">
 <%if(hash!=null) {%><input type = "hidden" name = "TargetHash" value="<%=hash %>"><%} %>
 </form>
 
 <form action="" method="post" name="Targeting">
 <input type = "hidden" name= "submittype" value="Targeting">
 <input type = "hidden" name = "TargetHash">
 </form>
 
 <div class="container">
 
 
   <div class="row">
   <%if(logedIn) {%>
   <form action="" method="post" name="LinkSever" class="form-horizontal well">
   
   <div class="span12">
   Source: <input type="text" class="input-large" value="<%if(hash!=null) {%><%=charDesc.name%><%} %>" name="LinkSource"> &emsp;
   Target: <input type="text" class="input-large" value="<%if(hash!=null) {%><%=charDesc.name%><%} %>" name="LinkTarget"> &emsp;
   Time period: <input type="text" class="input-large" placeholder="example:year/month/date-now" name="LinkDate">&emsp;
   <a class="btn btn-inverse" onclick="CreateLink()">Create Link</a>&emsp;
   <a class="btn btn-inverse" onclick="DeleteLink()">Delete Link</a>
   <input type="hidden" name="submittype" value="">
   <input type="hidden" name="TargetHash" value="<%=hash%>">
   <input type="hidden" name="linkhash" value="">
   
   </div>
   </form>
  
    <div class="span6">
	 <h3 id="tabs">Storage</h3>
      <ul class="nav nav-tabs">
        <li class="active"><a href="#sectionA" data-toggle="tab">Section A</a></li>
        <li><a href="#sectionB" data-toggle="tab">Section B</a></li>
        <li><a href="#sectionC" data-toggle="tab">Section C</a></li>
        <li><a href="#sectionD" data-toggle="tab">Section D</a></li>
      </ul>
      <div class="tabbable">
        <div class="tab-content">
          <div class="tab-pane active" id="sectionA">
            <div class="well" style="padding: 8px 0;">
        	<ul class="nav nav-list" id="ListSectionA">
          	<li class="nav-header">Section list A</li>
         	<li class="divider" id = "ListSectionA_Last"></li>
         	<li><a href="javascript:DeleteList('ListSectionA')">Delete List</a></li>
         	<%if(hash!=null) {%><li><a href="javascript:Add2Storage('ListSectionA', '<%=hash%>', '<%=charDesc.name%>')">Add to list A</a></li><%} %>
        	</ul>
      		</div>
          </div>
          <div class="tab-pane" id="sectionB">
            <div class="well" style="padding: 8px 0;">
        	<ul class="nav nav-list" id="ListSectionB">
        	<li class="nav-header">Section list B</li>
         	<li class="divider" id = "ListSectionB_Last"></li>
         	<li><a href="javascript:DeleteList('ListSectionB')">Delete List</a></li>
         	<%if(hash!=null) {%><li><a href="javascript:Add2Storage('ListSectionB', '<%=hash%>', '<%=charDesc.name%>')">Add to list B</a></li><%} %>
        	</ul>
      		</div>
          </div>
          <div class="tab-pane" id="sectionC">
            <div class="well" style="padding: 8px 0;">
        	<ul class="nav nav-list" id="ListSectionC">
          	<li class="nav-header">Section list C</li>
         	<li class="divider" id = "ListSectionC_Last"></li>
         	<li><a href="javascript:DeleteList('ListSectionC')">Delete List</a></li>
         	<%if(hash!=null) {%><li><a href="javascript:Add2Storage('ListSectionC', '<%=hash%>', '<%=charDesc.name%>')">Add to list C</a></li><%} %>
        	</ul>
      		</div>
          </div>
          <div class="tab-pane" id="sectionD">
            <div class="well" style="padding: 8px 0;">
        	<ul class="nav nav-list" id="ListSectionD">
          	<li class="nav-header">Section list D</li>
         	<li class="divider" id = "ListSectionD_Last"></li>
         	<li><a href="javascript:DeleteList('ListSectionD')">Delete List</a></li>
         	<%if(hash!=null) {%><li><a href="javascript:Add2Storage('ListSectionD', '<%=hash%>', '<%=charDesc.name%>')">Add to list D</a></li><%} %>
        	</ul>
      		</div>
          </div>
        </div>
      </div>
      </div>
      <div class="span6">
      <%}else {%>
      <div class="span12">
      <%} %>
        <h3>Search</h3>
        		<form class="well form-search" action="" method="post" name="Search">
        		<input type="text" class="input-medium search-query" name="Search" <%if(expandSearchList) {%>value="<%=searchQuery%>" <%} %>>
        		<input type="hidden" name="submittype" value="Search">
        		<%if(hash!=null) {%><input type = "hidden" name = "TargetHash" value="<%=hash %>"><%} %>
        		<button type="submit" class="btn" onclick="FormSearch()" <%if(expandSearchList) {%>onMouseOver="ShowSearchList()"<%} %>>Search</button>
     	<div class="well" id="SearchListFrame" style="display:none;" >
        <ul class="nav nav-list" id="SearchList">
        </ul>
        </div>
        		</form>
      </div>
    </div>
    
    <%if(hash != null) {%>
    <div class="page-header"><h2>Graph</h2>
    <canvas id="sitemap" width="600" height="900" class="" style="opacity: 1;"></canvas>
 	local relationship network is presented above.
    </div>
    <%} %>
    <br>
	<section id="forms">
	<div class="page-header"><h2>Texts</h2>
  	</div>
  	   
  	<div class="row">
    	<div class="span10 offset1">
    	<%if(!logedIn) {%>
			<form class="well form-search" action="" method="post" name="LogIn">
				<h4>Log in</h4>
        		<input type="text" class="input-large" placeholder="Account" name="Account">
          		<input type="password" class="input-large" placeholder="Password" name="Password">
          		<input type="hidden" name="submittype" value="LogIn">
       	 		<button class="btn" onclick="FormLogIn()">Log in</button>
       	 		<button class="btn" onclick="FormRegister()">Register</button>
       	 		<br>
       	 		<label class="checkbox" onclick="ShowHidePassword()">
                <input type="checkbox" id="ShowPassword">
              	show password
              	</label>
              	<label class="checkbox">
                <input type="checkbox" name="RememberMe">
              	remember me
              	</label>
              	<%if(hash!=null) {%><input type = "hidden" name = "TargetHash" value="<%=hash %>"><%} %>
       	 	</form>
       	 	<%} %>
       	 	<%if(hash != null) {%>
			<form class="form-horizontal well" method="post" name="EditInfo">
        	<fieldset>
          		<legend>Character Description</legend>
   				<div class="span12">
					
					<%if(!logedIn) {%>
					<label class="control-label offset1"><legend>Name</legend></label>
        			<div class="control-label offset1">
        			 <legend> <%=charDesc.name%>  (<%=charDesc.gender%>)</legend> 
            		</div>
            		</div>
            		<%}else{ %>
            		<label>Name:</label>
            		<input type="text" class="input-large" value="<%=charDesc.name%>" name="CharName">
            		<label>Gender:</label>
              			<select  name="selectlist">
                		<option>Male</option>
                		<option>Female</option>
              		</select>
              		</div>
            		<%} %>
            		<br><div class="span12">
            		<%if(!logedIn) {%>
            		<label class="control-label offset1"><legend>BirthDate</legend></label>
        			<div class="control-label offset1">
        			<legend> <%=charDesc.birthYear+"-"+charDesc.birthMonth+"-"+charDesc.birthDate%> </legend>
            		</div>
            		<%}else{%>
            		<label>BirthDate:</label>
            		<input type="text" class="input-large" value="<%=charDesc.birthYear%>" name="BirthYear">-
            		<input type="text" class="input-large" value="<%=charDesc.birthMonth%>" name="BirthMonth">-
            		<input type="text" class="input-large" value="<%=charDesc.birthDate%>" name="BirthDate">
            		<%} %>
            		</div>
            		<br><div class="span12">
            		<%if(!logedIn) {%>
            		<label class="control-label offset1"><legend>Profession</legend></label>
        			<div class="control-label offset1">
        			<legend> <%=charDesc.profession%> </legend>
            		</div>
            		<%}else{%>
            		<label>Profession:</label>
            		<input type="text" class="input-large" value="<%=charDesc.profession%>" name="Profession">
            		<%} %>
            		</div>
            		<br><div class="span12">
            		<%if(!logedIn) {%>
            		<label class="control-label offset1"><legend>Institution</legend></label>
        			<div class="control-label offset1">
        			<legend> <%=charDesc.institution%> </legend>
            		</div>
            		<%}else{%>
            		<label>Institution:</label>
            		<input type="text" class="input-large" value="<%=charDesc.profession%>" name="Institution">
            		<br>
            		<%}%>
        		</div>
        		<%if(logedIn) {%>
        		<div class="span8"><br></div>
        		<div class="span8"><a class="btn btn-inverse" onclick="EditInfoOK()">Save Changes</a>&emsp;
        		<a class="btn btn-inverse" onclick="AddNew()">Add as new</a> &emsp;
        		<a class="btn btn-inverse" onclick="DeleteChar()">Delete</a></div> &emsp;
        		<%} %>
        	</fieldset>
        	<input type="hidden" value="" name="gender">
        	<input type="hidden" value="<%=hash%>" name="TargetHash">
        	<input type="hidden" value="Edit" name="submittype">
        </form>
         <%} else if(logedIn){%>
			<form class="form-horizontal well" method="post" name="EditInfo">
        	<fieldset>
          		<legend>Character Description</legend>
   				<div class="span12">
            		<label>Name:</label>
            		<input type="text" class="input-large" value="" name="CharName">
            		<label>Gender:</label>
              			<select  name="selectlist">
                		<option>Male</option>
                		<option>Female</option>
              		</select>
              		</div>
            		<br><div class="span12">
            		<label>BirthDate:</label>
            		<input type="text" class="input-large" value="" name="BirthYear">-
            		<input type="text" class="input-large" value="" name="BirthMonth">-
            		<input type="text" class="input-large" value="" name="BirthDate">
            		</div>
            		<br><div class="span12">
            		<label>Profession:</label>
            		<input type="text" class="input-large" value="" name="Profession">
            		</div>
            		<br><div class="span12">
            		<label>Institution:</label>
            		<input type="text" class="input-large" value="" name="Institution">
            		<br>
        		</div>
        		
        		<div class="span8"><br></div>
        		<div class="span8">
        		<a class="btn btn-inverse" onclick="AddNew()">Add as new</a> &emsp;
        		</div>
        	</fieldset>
        	<input type="hidden" value="" name="gender">
        	<input type="hidden" value="Edit" name="submittype">
        </form>
         <%} %>
        </div>
       </div>	
	
</section>
 
Javabean created at : <%=Model.timing %>
 </div>
<!-- Don't change these sources! -->
<script src="js/jquery.min.js"></script>
<script src="js/json2.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootswatch.js"></script>
<script language="javascript" type="text/javascript">


<%if(submit!=null && submit.equals("Register") && !logedIn){%>
ShowAlert('User name <%=account%> has already been taken. Try another please.');
<%}%>
<%if(submit!=null && submit.equals("LogIn") && !logedIn){%>
ShowAlert('Incorrect username or password.');
<%}%>
<% if(submit!=null && submit.equals("Link") && !linkSucess) {%>
ShowAlert('Error: Can not create link from <%=request.getParameter("LinkSource")%> to <%=request.getParameter("LinkTarget")%>.\n Possibly because one or more names do not exist');
<%}%>
<% if(submit!=null && submit.equals("Sever") && !severSucess) {%>
ShowAlert('Error: Can not delete link from <%=request.getParameter("LinkSource")%> to <%=request.getParameter("LinkTarget")%>.\n Possibly because one or more names do not exist');
<%}%>
<% if(submit!=null && submit.equals("LinkAddSon") && !linkSucess) {%>
ShowAlert('Error: Can not create link from hash <%=hash%> to hash <%=request.getParameter("linkhash")%>.');
<%}%>
<% if(submit!=null && submit.equals("LinkAddFather") && !linkSucess) {%>
ShowAlert('Error: Can not create link from hash <%=request.getParameter("linkhash")%> to hash <%=hash%>.');
<%}%>
<% if(submit!=null && submit.equals("SeverAsSon") && !severSucess) {%>
ShowAlert('Error: Can not delete link from hash <%=hash%> to hash <%=request.getParameter("linkhash")%>.');
<%}%>
<% if(submit!=null && submit.equals("SeverAsFather") && !severSucess) {%>
ShowAlert('Error: Can not delete link from hash <%=request.getParameter("linkhash")%> to hash <%=hash%>.');
<%}%>
<%if(logedIn && hash!=null){%>
document.EditInfo.selectlist.value = '<%=charDesc.gender%>';
<%}%>
<%if (addFail){%>
	ShowAlert("Error: Duplicated name <%=charDesc.name%>. Try another please.");
<%}%>
<%if(expandSearchList) {%>
	ShowSearchList();
<%
}%>
<%if(!Model.synced) {%>
FlashSaveButton();
<%
}%>
if(psd!=null) document.LogIn.Password.value = psd;
if(acn!=null) document.LogIn.Account.value = acn;
<%if(logedIn){%>
WriteHistoryList();
WriteStorage();
<%}%>
</script>

<%if(hash != null && charDesc!=null) {
	
%>
<script src="site.js"></script>
  <script src="lib/arbor.js"></script>
  <script src="lib/arbor-tween.js"></script>
  <script src="lib/arbor-graphics.js"></script>
  <script>
//the arbor.js website
//
(function($){
 // var trace = function(msg){
 //   if (typeof(window)=='undefined' || !window.console) return
 //   var len = arguments.length, args = [];
 //   for (var i=0; i<len; i++) args.push("arguments["+i+"]")
 //   eval("console.log("+args.join(",")+")")
 // }  
 
 var Renderer = function(elt){
   var dom = $(elt)
   var canvas = dom.get(0)
   var ctx = canvas.getContext("2d");
   var gfx = arbor.Graphics(canvas)
   var sys = null

   var _vignette = null
   var selected = null,
       nearest = null,
       _mouseP = null;

   var intersect_line_line = function(p1, p2, p3, p4)
   {
     var denom = ((p4.y - p3.y)*(p2.x - p1.x) - (p4.x - p3.x)*(p2.y - p1.y));
     if (denom === 0) return false // lines are parallel
     var ua = ((p4.x - p3.x)*(p1.y - p3.y) - (p4.y - p3.y)*(p1.x - p3.x)) / denom;
     var ub = ((p2.x - p1.x)*(p1.y - p3.y) - (p2.y - p1.y)*(p1.x - p3.x)) / denom;

     if (ua < 0 || ua > 1 || ub < 0 || ub > 1)  return false;
     return arbor.Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
   }

   var intersect_line_box = function(p1, p2, boxTuple)
   {
     var p3 = {x:boxTuple[0], y:boxTuple[1]},
         w = boxTuple[2],
         h = boxTuple[3];
     
     var tl = {x: p3.x, y: p3.y};
     var tr = {x: p3.x + w, y: p3.y};
     var bl = {x: p3.x, y: p3.y + h};
     var br = {x: p3.x + w, y: p3.y + h};

     return intersect_line_line(p1, p2, tl, tr) ||
            intersect_line_line(p1, p2, tr, br) ||
            intersect_line_line(p1, p2, br, bl) ||
            intersect_line_line(p1, p2, bl, tl) ||
            false;
   }
   var that = {
     init:function(pSystem){
       sys = pSystem
       sys.screen({size:{width:dom.width(), height:dom.height()},
                   padding:[36,60,36,60]})

       $(window).resize(that.resize)
	   that.resize()
       that._initMouseHandling()
     },
     resize:function(){
       canvas.width = 1170
       canvas.height = 450
       sys.screen({size:{width:canvas.width, height:canvas.height}})
       _vignette = null
       that.redraw()
     },
     redraw:function(){
       var nodeBoxes = {}
       gfx.clear()
       sys.eachNode(function(node, pt){
           var w = Math.max(20, 20+gfx.textWidth(node.name) )
		   if (node.data.shape=='dot'){
               gfx.oval(pt.x-w/2, pt.y-w/2, w,w, {fill:ctx.fillStyle});
               nodeBoxes[node.name] = [pt.x-w/2, pt.y-w/2, w,w];
           }else{
               gfx.rect(pt.x-w/2, pt.y-14, w,30, 4, {fill:ctx.fillStyle});
               nodeBoxes[node.name] = [pt.x-w/2, pt.y-14, w, 30];
           }
         })
       sys.eachEdge(function(edge, p1, p2){
         if (edge.source.data.alpha * edge.target.data.alpha == 0) return
         gfx.line(p1, p2, {stroke:"#b2b19d", width:2, alpha:edge.target.data.alpha})
         if (true){
             ctx.save();
               // move to the head position of the edge we just drew
               var tail = intersect_line_box(p1, p2, nodeBoxes[edge.source.name]);
     		   var head = intersect_line_box(tail, p2, nodeBoxes[edge.target.name]);
               var wt = ctx.lineWidth;
               var arrowLength = 10 + wt;
               var arrowWidth = 6 + wt;
               ctx.fillStyle = ctx.strokeStyle;
               ctx.translate(head.x, head.y);
               ctx.rotate(Math.atan2(head.y - tail.y, head.x - tail.x));

               // delete some of the edge that's already there (so the point isn't hidden)
               ctx.clearRect(-arrowLength/2,-wt/2, arrowLength/2,wt);

               // draw the chevron
               ctx.beginPath();
               ctx.moveTo(-arrowLength, arrowWidth);
               ctx.lineTo(0, 0);
               ctx.lineTo(-arrowLength, -arrowWidth);
               ctx.lineTo(-arrowLength * 0.8, -0);
               ctx.closePath();
               ctx.fill();
             ctx.restore();
             if(edge.data!='[object Object]')
   	gfx.text(edge.data, (head.x + tail.x)/2, (head.y + tail.y)/2+7, {color:"black", align:"center", font:"Arial", size:8});
           }
       })
       sys.eachNode(function(node, pt){
           var w = Math.max(20, 20+gfx.textWidth(node.name))
           if (node.data.alpha===0) return
           if (node.data.shape=='dot'){
             gfx.oval(pt.x-w/2, pt.y-w/2, w, w, {fill:node.data.color, alpha:node.data.alpha})
             gfx.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
             gfx.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
           }else{
             gfx.rect(pt.x-w/2, pt.y-8, w, 20, 4, {fill:node.data.color, alpha:node.data.alpha})
             gfx.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
             gfx.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
           }
       })
       that._drawVignette()
     },
     
     _drawVignette:function(){
       var w = canvas.width
       var h = canvas.height
       var r = 3

       if (!_vignette){
         var top = ctx.createLinearGradient(0,0,0,3)
         top.addColorStop(0, "#c0c0c0")
         top.addColorStop(.7, "rgba(255,255,255,0)")

         var bot = ctx.createLinearGradient(0,h-3,0,h)
         bot.addColorStop(0, "rgba(255,255,255,0)")
         bot.addColorStop(1, "white")

         _vignette = {top:top, bot:bot}
       }
       
       // top
       ctx.fillStyle = _vignette.top
       ctx.fillRect(0,0, w,r)

       // bot
       ctx.fillStyle = _vignette.bot
       ctx.fillRect(0,h-r, w,r)
     },

     switchMode:function(e){
       if (e.mode=='hidden'){
         dom.stop(true).fadeTo(e.dt,0, function(){
           if (sys) sys.stop()
           $(this).hide()
         })
       }else if (e.mode=='visible'){
         dom.stop(true).css('opacity',0).show().fadeTo(e.dt,1,function(){
           that.resize()
         })
         if (sys) sys.start()
       }
     },
     
     switchSection:function(newSection){
       var parent = sys.getEdgesFrom(newSection)[0].source
       var children = $.map(sys.getEdgesFrom(newSection), function(edge){
         return edge.target
       })
       
       sys.eachNode(function(node) {
         if (node.data.shape=='dot') return // skip all but leafnodes
         var nowVisible = ($.inArray(node, children)>=0)
         var newAlpha = (nowVisible) ? 1 : 0
         var dt = (nowVisible) ? .5 : .5
         sys.tweenNode(node, dt, {alpha:newAlpha})

         if (newAlpha==1){
           node.p.x = parent.p.x + .05*Math.random() - .025
           node.p.y = parent.p.y + .05*Math.random() - .025
           node.tempMass = .001
         }
       })
     },
     
     
     _initMouseHandling:function(){
       // no-nonsense drag and drop (thanks springy.js)
       selected = null;
       nearest = null;
       var dragged = null;
       var oldmass = 1

       var _section = null

       var handler = {
         moved:function(e){
           var pos = $(canvas).offset();
           _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
           nearest = sys.nearest(_mouseP);

           if (!nearest.node) return false

           if (nearest.node.data.shape!='dot'){
             selected = (nearest.distance < 50) ? nearest : null
             if (selected){
                dom.addClass('linkable')
                window.status = selected.node.data.link.replace(/^\//,"http://"+window.location.host+"/").replace(/^#/,'')
             }
             else{
                dom.removeClass('linkable')
                window.status = ''
             }
           }else if ($.inArray(nearest.node.name, [//'Quigon Jinn','Count Dooku','Plo koon','Mace Windu','Cin Drallig','Anakin Skywalker'
			<%for(int i=0; i<Model.neighborList1.size(); i++) {%>
            	'<%=Model.neighborList1.get(i).front%>',
            <%}%>
            <%for(int i=0; i<Model.neighborList2.size(); i++) {%>
            	'<%=Model.neighborList2.get(i).front%>',
            <%}%>
            '<%=charDesc.name%>']) >=0){
             if (nearest.node.name!=_section){
               _section = nearest.node.name
               that.switchSection(_section)
             }
             dom.removeClass('linkable')
             window.status = ''
           }
           
           return false
         },
         clicked:function(e){
           var pos = $(canvas).offset();
           _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
           nearest = dragged = sys.nearest(_mouseP);
           
           if (nearest && selected && nearest.node===selected.node){
             var link = selected.node.data.link
             if (link.match(/^#/)){
                $(that).trigger({type:"navigate", path:link.substr(1)})
             }else{
                window.location = link
             }
             return false
           }
           
           
           if (dragged && dragged.node !== null) dragged.node.fixed = true

           $(canvas).unbind('mousemove', handler.moved);
           $(canvas).bind('mousemove', handler.dragged)
           $(window).bind('mouseup', handler.dropped)

           return false
         },
         dragged:function(e){
           var old_nearest = nearest && nearest.node._id
           var pos = $(canvas).offset();
           var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

           if (!nearest) return
           if (dragged !== null && dragged.node !== null){
             var p = sys.fromScreen(s)
             dragged.node.p = p
           }

           return false
         },

         dropped:function(e){
           if (dragged===null || dragged.node===undefined) return
           if (dragged.node !== null) dragged.node.fixed = false
           dragged.node.tempMass = 1000
           dragged = null;
           // selected = null
           $(canvas).unbind('mousemove', handler.dragged)
           $(window).unbind('mouseup', handler.dropped)
           $(canvas).bind('mousemove', handler.moved);
           _mouseP = null
           return false
         }


       }

       $(canvas).mousedown(handler.clicked);
       $(canvas).mousemove(handler.moved);

     }
   }
   
   return that
 }
 
 
 var Nav = function(elt){
   var dom = $(elt)

   var _path = null
   
   var that = {
     init:function(){
       $(window).bind('popstate',that.navigate)
       dom.find('> a').click(that.back)
       $('.more').one('click',that.more)
       
       $('#docs dl:not(.datastructure) dt').click(that.reveal)
       that.update()
       return that
     },
     more:function(e){
       $(this).removeAttr('href').addClass('less').html('&nbsp;').siblings().fadeIn()
       $(this).next('h2').find('a').one('click', that.less)
       
       return false
     },
     less:function(e){
       var more = $(this).closest('h2').prev('a')
       $(this).closest('h2').prev('a')
       .nextAll().fadeOut(function(){
         $(more).text('creation & use').removeClass('less').attr('href','#')
       })
       $(this).closest('h2').prev('a').one('click',that.more)
       
       return false
     },
     reveal:function(e){
       $(this).next('dd').fadeToggle('fast')
       return false
     },
     back:function(){
       _path = "/"
       if (window.history && window.history.pushState){
         window.history.pushState({path:_path}, "", _path);
       }
       that.update()
       return false
     },
     navigate:function(e){
       var oldpath = _path
       if (e.type=='navigate'){
         _path = e.path
         if (window.history && window.history.pushState){
            window.history.pushState({path:_path}, "", _path);
         }else{
           that.update()
         }
       }else if (e.type=='popstate'){
         var state = e.originalEvent.state || {}
         _path = state.path || window.location.pathname.replace(/^\//,'')
       }
       if (_path != oldpath) that.update()
     },
     update:function(){
       var dt = 'fast'
       if (_path===null){
         // this is the original page load. don't animate anything just jump
         // to the proper state
         _path = window.location.pathname.replace(/^\//,'')
         dt = 0
         dom.find('p').css('opacity',0).show().fadeTo('slow',1)
       }

       switch (_path){
         case '':
         case '/':
         dom.find('p').text('a graph visualization library using web workers and jQuery')
         dom.find('> a').removeClass('active').attr('href','#')

         $('#docs').fadeTo('fast',0, function(){
           $(this).hide()
           $(that).trigger({type:'mode', mode:'visible', dt:dt})
         })
         document.title = "arbor.js"
         break
         
         case 'introduction':
         case 'reference':
         $(that).trigger({type:'mode', mode:'hidden', dt:dt})
         dom.find('> p').text(_path)
         dom.find('> a').addClass('active').attr('href','#')
         $('#docs').stop(true).css({opacity:0}).show().delay(333).fadeTo('fast',1)
                   
         $('#docs').find(">div").hide()
         $('#docs').find('#'+_path).show()
         document.title = "arbor.js » " + _path
         break
       }
       
     }
   }
   return that
 }
 
 $(document).ready(function(){
   var CLR = {
     branch:"#b2b19d",
     code:"orange",
     doc:"#922E00",
     demo:"#a7af00"
   }

  


   var sys = arbor.ParticleSystem()
   sys.parameters({stiffness:900, repulsion:2000, gravity:true, dt:0.015})
<%for(int i=0; i<Model.neighborList1.size(); i++) {%>
   sys.addNode("<%=Model.neighborList1.get(i).front%>", {color:"grey", shape:"dot", alpha:1, mass:3})
   <%}%>
   <%for(int i=0; i<Model.neighborList2.size(); i++) {%>
   sys.addNode("<%=Model.neighborList2.get(i).front%>", {color:"grey", shape:"dot", alpha:1, mass:2})
   <%}%>
   <%for(int i=0; i<Model.neighborList3.size(); i++) {%>
   sys.addNode("<%=Model.neighborList3.get(i).front%>", {color:"grey", alpha:0, link:'javascript:FormTargeting("<%=Model.neighborList3.get(i).back%>")'})
   <%}%>
   sys.addNode("<%=charDesc.name%>", {color:"black", shape:"dot", alpha:1, mass:5})
   <%for(int i=0; i<Model.directedWeb.size(); i++) {%>
  	  <%for(int j=0; j<Model.directedWeb.get(i).dst.size(); j++) {%>
			sys.addEdge("<%=Model.directedWeb.get(i).src%>","<%=Model.directedWeb.get(i).dst.get(j)%>", "<%=Model.directedWeb.get(i).info.get(j)%>")
   <%}}%>
   sys.renderer = Renderer("#sitemap")
   //sys.graft(theUI)
   
   var nav = Nav("#nav")
   $(sys.renderer).bind('navigate', nav.navigate)
   $(nav).bind('mode', sys.renderer.switchMode)
   nav.init()
 })
})(this.jQuery)
  
  </script>

<%}%>
</body>
</html>