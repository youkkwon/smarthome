<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<table border="1">
<tr>
	        	<th>Date</th>
	        	<th>Content</th>
</tr>        	
				<c:forEach var="item" items="${eventhistory}">
	        	<tr>
					<td>${item.getEventDate()}</td>
					<td>${item.getEventContent()}</td>
	        	</tr>
				</c:forEach>	        	
			</table>
</body>
</html>