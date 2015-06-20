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
	<script type="text/javascript">
		var wsocket;
		
		function getsockurl(s) {
		    var l = window.location;
		    return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "") + s;
		}
		
		function connect() {
			wsocket = new WebSocket(getsockurl("/iotms/newnode-ws"));
			wsocket.onopen = onOpen;
			wsocket.onmessage = onMessage;
			wsocket.onclose = onClose;
		}
		function disconnect() {
			wsocket.close();
		}
		function onOpen(evt) {
			appendMessage("연결되었습니다.");
		}
		function onMessage(evt) {
			var data = evt.data;
			//if (data.substring(0, 4) == "msg:") {
				appendMessage(data.substring(4));
			//}
		}
		function onClose(evt) {
			appendMessage("연결을 끊었습니다.");
		}
		
// 		function send() {
// 			var nickname = $("#nickname").val();
// 			var msg = $("#message").val();
// 			wsocket.send("msg:"+nickname+":" + msg);
// 			$("#message").val("");
// 		}
		
		function appendMessage(msg) {
			$("#chatMessageArea").append(msg+"<br>");
			var chatAreaHeight = $("#chatArea").height();
			var maxScroll = $("#chatMessageArea").height() - chatAreaHeight;
			$("#chatArea").scrollTop(maxScroll);
		}
		
		$(document).ready(function() {
			connect();
		});
	</script>
	<style>
	#chatArea {
		width: 500px; height: 100px; overflow-y: auto; border: 1px solid black;
	}
	</style>
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
	
	<c:forEach var="node" items="${newnode}">
	<table border="1">
		<tr>
      		<th>Node ID</th>
      		<th>Node Name</th>
      		<th>Serial Number</th>
		</tr>
      	<tr>
      		<th><input id="${node.node_id}_id" type="text" value="${node.node_id}" readonly></th>
      		<th><input id="${node.node_id}_name" type="text" value="${node.node_name}" readonly></th>
      		<th><input id="${node.node_id}_sn" type="text" value="${node.serialnumber}" readonly></th>
		</tr>
	</table>
	<table border="1">
		<tr>
			<th>Thing ID</th>
			<th>Thing Name</th>
			<th>Thing Type</th>
			<th>Thing Value</th>
			<th>Thing Control</th>
		</tr>
		<c:forEach var="thing" items="${node.things}">
		<tr>
			<th><input id="${node.node_id}_${thing.thing_id}_nodeid" type="hidden" value="${node.node_id}" readonly>
				<input id="${node.node_id}_${thing.thing_id}_id" type="text" value="${thing.thing_id}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_name" type="text" value="${thing.thing_name}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_type" type="text" value="${thing.type}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_value" type="text" value="${thing.value}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_stype" type="text" value="${thing.stype}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_vtype" type="text" value="${thing.vtype}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_vmin" type="text" value="${thing.vmin}" readonly></th>
			<th><input id="${node.node_id}_${thing.thing_id}_vmax" type="text" value="${thing.vmax}" readonly></th>
		</tr>
		</c:forEach>
	</table>
	<br>
	</c:forEach>
	<div id="chatArea"><div id="chatMessageArea"></div></div>
<div style="display:none;">
	<iframe id="subnewnode" src=""></iframe>
</div>
</body>
</html>