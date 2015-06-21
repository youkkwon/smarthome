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
	function registerNode(nodeid) {
		var serial = document.getElementById (nodeid+"_sn").value;
		var loc = "/iotms/node/remove?nodeid="+nodeid;
		
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:230,
			width:320,
			modal: true,
			buttons: {
				"Register Node": function() {
				     	// Do this after Confirm
				     	document.getElementById("controlpage").setAttribute("src", loc);
				$( this ).dialog( "close" );
				     },
				     Cancel: function() {
				$( this ).dialog( "close" );
				     }
			}
		});
	}
	function CheckNo(sender, min, max, init){
	    if(!isNaN(sender.value)){
	        if(sender.value > max )
	            sender.value = max;
	        if(sender.value < min )
	            sender.value = min;
	    }else{
	          sender.value = init;
	    }
	}
	function controlThing(row) {
		var nodeid = document.getElementById (row+"_nodeid").value;
		var thingid = document.getElementById (row+"_id").value;
		var type = document.getElementById (row+"_type").value;
		var ctrlvalue = document.getElementById (row+"_ctrl").value;
		var loc = "/iotms/thing/control?nodeid="+nodeid+"&thingid="+thingid+"&type="+type+"&value="+ctrlvalue;
		  document.getElementById("controlpage").setAttribute("src", loc);
	}
  </script>
  <script type="text/javascript">
		var wsocket;
		
		function getsockurl(s) {
		    var l = window.location;
		    return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "") + s;
		}
		
		function connect() {
			wsocket = new WebSocket(getsockurl("/iotms/node-ws"));
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
			var json = evt.data;
//			appendMessage(json);
			
			var data = $.parseJSON(json);
			var mon_nodeid = data.NodeID;
			var mon_thingid = data.ThingID;
			var mon_value = data.Value;
			var out = "[NodeID:"+ mon_nodeid+"] [ThingID:"+ mon_thingid+"] [Value:"+ mon_value+"] ";
//			appendMessage(out);
			
			document.getElementById (mon_nodeid+"_"+mon_thingid+"_value").value = mon_value;
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
	<div id="newnodepannel">
		<c:forEach var="node" items="${nodelist}">
		<div id="${node.node_id}">
		<table border="1">
			<tr>
	      		<th>Node ID</th>
	      		<th>Serial Number</th>
			</tr>
	      	<tr>
	      		<th><input id="${node.node_id}_id" type="text" value="${node.node_id}" readonly></th>
	      		<th><input id="${node.node_id}_sn" type="text" value="${node.serialnumber}" readonly></th>
	      		<th><button onClick="removeNode('${node.node_id}')">Remove Node</button></th>
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
				<c:if test="${fn:toLowerCase(thing.stype) == 'actuator'}">
				<c:choose>
				<c:when test="${fn:toLowerCase(thing.vtype) == 'string'}">
				<th>
				<select id="${node.node_id}_${thing.thing_id}_ctrl">
				<option value="${thing.vmin}">${thing.vmin}</option>
				<option value="${thing.vmax}">${thing.vmax}</option>
				</select>
				</th>
		  		</c:when>
				<c:when test="${fn:toLowerCase(thing.vtype) == 'number'}">
				<th>
				<input id="${node.node_id}_${thing.thing_id}_ctrl" type="text" value="${thing.value}" onblur="CheckNo(this,${thing.vmin},${thing.vmax},${thing.value})">
				</th>
		  		</c:when>
				<c:otherwise>
				<th><input id="${node.node_id}_${thing.thing_id}_ctrl" type="text" value="${thing.value}" ></th>
				</c:otherwise>
				</c:choose>
				<th><button onClick="controlThing('${node.node_id}_${thing.thing_id}')" >Send Control</button></th>
				</c:if>
			</tr>
			</c:forEach>
		</table>
		<br>
		</div>
		</c:forEach>
	</div>
	<div id="chatArea" style="display:none;"><div id="chatMessageArea"></div></div>
<div style="display:none;">
	<iframe id="controlpage" src=""></iframe>
</div>
</body>
</html>