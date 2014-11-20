<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="An ode to Metro.">
    <meta name="author" content="Thomas Park">
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">
    <link href="css/bootswatch.css" rel="stylesheet">
	<!-- <title>Edit</title> -->
	
	
	<!-- Functions stay here! -->
	<script type="text/javascript">
	var SaveBufferTimer = 0;
	function SaveBuffer() {
		//TODO : add function here
		document.getElementById("SaveBufferButton").style.display="none";
		document.getElementById("SaveBufferDiv").style.display="";
		document.getElementById("SaveBufferSync").style.display="";
		document.getElementById("SaveBufferProgressBar").style.width=String(SaveBufferTimer)+"%";
		if(SaveBufferTimer>120) {
			document.getElementById("SaveBufferSync").style.display="none";
			document.getElementById("SaveBufferFinish").style.display="";
		}
		if(SaveBufferTimer>128) {
			SaveBufferTimer = 0;
			document.getElementById("SaveBufferDiv").style.display="none";
			document.getElementById("SaveBufferFinish").style.display="none";
			return;
		}
		SaveBufferTimer += 2;
		setTimeout("SaveBuffer();", 100);
	}
	function EditInfoOK() {
		document.getElementById("SaveBufferButton").style.display="";
	}
	function DeleteThis(elemID) {
		var elem = document.getElementById(elemID);
		var parelm = elem.parentNode;
		parelm.removeChild(elem);
	}
	function Add2Storage(storageID, elemHash, elemName) {
		String elemID = strageID + '_' + elemHash;
		String writeElem = '<a class="dropdown-toggle" data-toggle="dropdown" href="#">'+elemName+'<b class="caret"></b></a>';
		writeElem = writeElem + '<ul class="dropdown-menu"> <li><a href="#">Add as student</a></li> <li><a href="#">Add as teacher</a></li>';
		writeElem = writeElem + '<li class="divider"></li><li><a href="javascript:DeleteThis("' + elemID + '");">Remove</a></li></ul>';
		var storage = document.getElementById(storageID);
		var newelem = document.createElement("li");
		newelem.setAttribute("id", elemID);
		newelem.setAttribute("class", 'dropdown');
		newelem.innerHTML = writeElem;
		storage.appendChild(newelem);
	}
	</script>
</head>

<body class="preview" id="top" data-spy="scroll" data-target=".subnav" data-offset="80">
	 
	 
 <div class="navbar navbar-fixed-top"> <!-- Top Navigation Bar -->
   <div class="navbar-inner">
     <div class="container">
       <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </a>
       <a class="brand" href="http://down.admin5.com/">Bootswatch</a>
       <div class="nav-collapse collapse" id="main-menu">
        <ul class="nav" id="main-menu-left">
          <li><a onclick="pageTracker._link(this.href); return false;" href="http://down.admin5.com">News</a></li>
          <li><a id="swatch-link" href="http://down.admin5.com">Gallery</a></li>
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">Preview</a>
          </li>
          <li class="dropdown" id="SaveBufferButton" style="display:none">
            <a class="dropdown-toggle" data-toggle="dropdown" onclick="javascript:SaveBuffer();">Save </a>
          </li>
        </ul>
	   </div>
	   <div class="span12" id="SaveBufferDiv" style="display:none;">
		<div class="progress">
		<div class="bar" id="SaveBufferProgressBar" style="width: 0%;"></div>
		</div>
		<h4  class="alert-heading" id="SaveBufferSync" style="display:none;">Synchronizing DataBase...</h4>
		<h4  class="alert-heading" id="SaveBufferFinish" style="display:none;">DataBase Updated.</h4>
	   </div>
     </div>
   </div>
   
 </div>
 <div class="container">
 
 
   <div class="row">
    <div class="span4">
	 <h3 id="tabs">Storage</h3>
      <ul class="nav nav-tabs">
        <li class="active"><a href="#ListSectionA" data-toggle="tab">Section 1</a></li>
        <li><a href="#ListSectionB" data-toggle="tab">Section 2</a></li>
        <li><a href="#ListSectionC" data-toggle="tab">Section 3</a></li>
      </ul>
      <div class="tabbable">
        <div class="tab-content">
          <div class="tab-pane active" id="ListSectionA">
            <div class="well" style="padding: 8px 0;">
        	<ul class="nav nav-list">
          	<li class="nav-header">Choose a list</li>
          	<li class="dropdown" id="ListSectionA_Yoda">
          	<a class="dropdown-toggle" data-toggle="dropdown" href="#">Yoda <b class="caret"></b></a>
          	<ul class="dropdown-menu">
            <li><a href="#">Add as student</a></li>
            <li><a href="#">Add as teacher</a></li>
            <li class="divider"></li>
            <li><a href="javascript:DeleteThis('ListSectionA_Yoda');">Remove</a></li>
          </ul>
        </li>
         	<li class="divider"></li>
         	<li><a href="#">Help</a></li>
        	</ul>
      		</div>
          </div>
          <div class="tab-pane" id="ListSectionB">
            <p>Howdy, I'm in Section B.</p>
          </div>
          <div class="tab-pane" id="ListSectionC">
            <p>What up girl, this is Section C.</p>
          </div>
        </div>
      </div>
      </div>>
    </div>
    
    
 <div class="row"><a class="btn btn-inverse" onclick="javascript:EditInfoOK();">Save Changes</a></div>
 
 
 </div>
<!-- Don't change these sources! -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootswatch.js"></script>
</body>
</html>