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
function UpdateUser(userId)
{
	var id = document.getElementById (userId+'_id').value;
	var name = document.getElementById (userId+'_name').value;
	var mail = document.getElementById (userId+'_mail').value;
	var twitter = document.getElementById (userId+'_twitter').value;
	var passwd = document.getElementById (userId+'_passwd').value;
	
	document.getElementById("userId").value = id;
	document.getElementById("userName").value = name;
	document.getElementById("userMail").value = mail;
	document.getElementById("userTwitter").value = twitter;
	document.getElementById("userPasswd").value = passwd;
	
	document.getElementById("myform").action = "/iotms/user/update";
	$( "#dialog-confirm" ).dialog({
		resizable: false,
		height:230,
		modal: true,
		buttons: {
			"Update User": function() {
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
function DeleteUser(userId)
{
	var id = document.getElementById (userId+'_id').value;
	var name = document.getElementById (userId+'_name').value;
	var mail = document.getElementById (userId+'_mail').value;
	var twitter = document.getElementById (userId+'_twitter').value;
	var passwd = document.getElementById (userId+'_passwd').value;
	
	document.getElementById("userId").value = id;
	document.getElementById("userName").value = name;
	document.getElementById("userMail").value = mail;
	document.getElementById("userTwitter").value = twitter;
	document.getElementById("userPasswd").value = passwd;
	
	document.getElementById("myform").action = "/iotms/user/remove";
	$( "#dialog-confirm" ).dialog({
		resizable: false,
		height:230,
		modal: true,
		buttons: {
			"Remove User": function() {
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
       	<th>ID</th>
       	<th>Name</th>
       	<th>Mail</th>
       	<th>Twitter</th>
       	<th>Password</th>
	</tr>  	
	<c:forEach var="item" items="${userlist}">
      	<tr>
      		<th><input id="${item.userId}_id" type="text" value="${item.userId}" readonly></th>
      		<th><input id="${item.userId}_name" type="text" value="${item.userName}"></th>
      		<th><input id="${item.userId}_mail" type="text" value="${item.userMail}"></th>
      		<th><input id="${item.userId}_twitter" type="text" value="${item.userTwitter}"></th>
      		<th><input id="${item.userId}_passwd" type="text" value="${item.userPasswd}"></th>
      		<th><A href="javascript: UpdateUser('${item.userId}')">Update</A></th>
      		<th><A href="javascript: DeleteUser('${item.userId}')">Delete</A></th>
      	</tr>
	</c:forEach>
</table>
<div style="display:none;">
	<form id="myform" method="POST" action="">
		<input id="userId" name="userId" value="" />
	  	<input id="userName" name="userName" value="" />
	  	<input id="userMail" name="userMail" value="" />
	  	<input id="userTwitter" name="userTwitter" value="" />
	  	<input id="userPasswd" name="userPasswd" value="" />
  	</form>
</div>
</body>
</html>