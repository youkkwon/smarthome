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
</head>

<body>
    <p>Node List</p>

	<c:forEach var="node" items="${nodelist}">
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
	</c:forEach>
<div style="display:none;">
	<iframe id="controlpage" src=""></iframe>
</div>
</body>
</html>