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
<div style="display:none;">
	<form id="myform" method="POST" action="">
		<input id="ruleSet_ID" name="ruleSet_ID" value="" />
		<input id="ruleSet" name="ruleSet" value="" />
  	</form>
</div>
</body>
</html>