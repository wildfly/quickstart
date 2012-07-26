<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RegistrationAction</title>
</head>
<body>

<h4>Members</h4>
<table class="simpletablestyle">
<thead>
<tr>
<th>Name</th>
<th>Email</th>
<th>PhoneNumber</th>
</tr>
</thead>
<tbody>
<!--  iterate on the member list @named attribute and display name,email and phone number -->
<c:forEach items="${members}" var="member">
	<tr>
		<td><c:out value="${member.name}"/></td>
		<td><c:out value="${member.email}"/></td>
		<td><c:out value="${member.phoneNumber}"/></td>
	</tr>
</c:forEach>
</tbody>
</table>	

</body>
</html>
