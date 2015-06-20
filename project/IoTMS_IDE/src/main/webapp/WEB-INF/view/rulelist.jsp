<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
	<link rel="stylesheet" href="/iotms/jquery-ui/jquery-ui.css">
	<script src="/iotms/jquery-ui/external/jquery/jquery.js"></script>
	<script src="/iotms/jquery-ui/jquery-ui.js"></script>
	<script>
  </script>
</head>
<script type="text/javascript">
function CreateRule()
{
	var ruleset = document.getElementById("newrule").value;
	
	document.getElementById("ruleSet_ID").value = "0";
	document.getElementById("ruleSet").value = ruleset;
	
	document.getElementById("myform").action = "/iotms/rule/create";
	$( "#dialog-confirm" ).dialog({
		resizable: false,
		height:230,
		width:320,
		modal: true,
		buttons: {
			"Create RuleSet": function() {
			     	// Do this after Confirm
			     	document.getElementById("myform").submit();
			$( this ).dialog( "close" );
			     },
			     Cancel: function() {
			$( this ).dialog( "close" );
			     }
		}
	});
}
function DeleteRule(ruleid)
{
	var ruleset = document.getElementById (ruleid).value;
	
	document.getElementById("ruleSet_ID").value = ruleid;
	document.getElementById("ruleSet").value = ruleset;
	
	document.getElementById("myform").action = "/iotms/rule/remove";
	$( "#dialog-confirm" ).dialog({
		resizable: false,
		height:230,
		width:320,
		modal: true,
		buttons: {
			"Remove RuleSet": function() {
			     	// Do this after Confirm
			     	document.getElementById("myform").submit();
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
			wsocket = new WebSocket(getsockurl("/iotms/rule-ws"));
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
			appendMessage(data);
//			location.reload();
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
		width: 200px; height: 100px; overflow-y: auto; border: 1px solid black;
	}
	</style>
<body>
<div id="dialog-confirm" title="IoTMS" style="display:none;">
  <p>Are you sure?</p>
</div>
<table border="1">
	<tr>
       	<th>RuleSet</th>
	</tr>
	<c:forEach var="item" items="${rulelist}">
      	<tr>
      		<th><input id="${item.ruleId}" type="text" value="${item.ruleSet}" readonly></th>
      		<th><A href="javascript: DeleteRule('${item.ruleId}')">Delete</A></th>
      	</tr>
	</c:forEach>
</table>
<div id="chatArea"><div id="chatMessageArea"></div></div>
<p><button onClick="CreateRule()" >Add Rule</button><input id="newrule" type="text" value=""></p>

<div style="display:none;">
	<form id="myform" method="POST" action="">
		<input id="ruleSet_ID" name="ruleSet_ID" value="" />
		<input id="ruleSet" name="ruleSet" value="" />
  	</form>
</div>
</body>
</html>