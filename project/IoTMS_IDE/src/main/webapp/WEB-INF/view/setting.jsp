<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<form:form method="post" modelAttribute="setting" action="/iotms/setsetting">
	<form:errors />
	<table border="1">
		<tr>
        	<th>Auto Security</th>
        	<!-- 
			<th>${setting.securitySettime}</th>
			 -->
			<th><form:input path="securitySettime" value="${setting.securitySettime}" /></th>
		</tr>
		<tr>
        	<th>Auto Light Off</th>
			<!-- 
			<th>${setting.lightoffSettime}</th>
			-->
			<th><form:input path="lightoffSettime" value="${setting.lightoffSettime}" /></th>
		</tr>        	
       	<tr>
        	<th>Logging Duration</th>
        	<!--  
			<th>${setting.loggingDuration}</th>
			-->
			<th><form:input path="loggingDuration" value="${setting.loggingDuration}" /></th>
       	</tr>
       	<tr>
        	<th><input type="submit" value="Save"></th>
       	</tr>
	</table>
	</form:form>
</body>
</html>