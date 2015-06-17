<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
	<link rel="stylesheet" href="/iotms/jquery-ui/jquery-ui.css">
	<script src="/iotms/jquery-ui/external/jquery/jquery.js"></script>
	<script src="/iotms/jquery-ui/jquery-ui.js"></script>
	<script>
	function discoverNodes() {
		var loc = "/iotms/node/discover";
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:230,
			width:320,
			modal: true,
			buttons: {
				"Discovery Nodes": function() {
				     	// Do this after Confirm
				     	document.getElementById("subnewnode").setAttribute("src", loc);
				$( this ).dialog( "close" );
				     },
				     Cancel: function() {
				$( this ).dialog( "close" );
				     }
			}
		});
	}
	function registerNode() {
		var nodeid = document.getElementById ("nodeid").value;
		var serial = document.getElementById ("serial").value;
		var type = document.getElementById (row+"_type").value;
		var ctrlvalue = document.getElementById (row+"_ctrl").value;
		var loc = "/iotms/node/register?nodeid="+nodeid+"&serial="+serial;
		
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:230,
			width:320,
			modal: true,
			buttons: {
				"Register Node": function() {
				     	// Do this after Confirm
				     	document.getElementById("subnewnode").setAttribute("src", loc);
				$( this ).dialog( "close" );
				     },
				     Cancel: function() {
				$( this ).dialog( "close" );
				     }
			}
		});
	}
  </script>
</head>
<body>
<div id="dialog-confirm" title="IoTMS" style="display:none;">
  <p>Are you sure?</p>
</div>
    <p>Node List</p>
	<p><button onClick="discoverNodes()" >DiscoverNodes</button>
	<button onClick="registerNode()" >Register Node</button></p>

	<table border="1">
		<tr>
      		<th>Node ID</th>
      		<th>Serial Number</th>
		</tr>
      	<tr>
			<th><input id="nodeid" type="text" value="" ></th>
			<th><input id="serial" type="text" value="" ></th>
		</tr>
	</table>
<div style="display:none;">
	<iframe id="subnewnode" src=""></iframe>
</div>
</body>
</html>