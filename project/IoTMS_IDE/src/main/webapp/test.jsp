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
	<script  type="text/javascript">
		function discoverNodes() {
			var nodeid = document.getElementById ("nodeid").value;
			var loc = "/iotms/node/testdiscover?nodeid="+nodeid;
			
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
		function updateThing() {
			var nodeid = document.getElementById ("nodeid").value;
			var thingid = document.getElementById ("thingid").value;
			var type = document.getElementById ("type").value;
			var ctrlvalue = document.getElementById ("ctrlvalue").value;
			var loc = "/iotms/node/testnode?nodeid="+nodeid+"&thingid="+thingid+"&type="+type+"&value="+ctrlvalue;
			
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
	<button onClick="updateThing()" >Update Thing</button></p>

	<table border="1">
		<tr>
      		<th>Node ID</th>
      		<th>Thing ID</th>
      		<th>Type</th>
      		<th>Value</th>
		</tr>
      	<tr>
			<th><input id="nodeid" type="text" value="" ></th>
			<th><input id="thingid" type="text" value="" ></th>
			<th><input id="type" type="text" value="" ></th>
			<th><input id="ctrlvalue" type="text" value="" ></th>
		</tr>
	</table>
	
<div style="display:none;">
	<iframe id="subnewnode" src=""></iframe>
</div>
</body>
</html>