<%--
    JBoss, Home of Professional Open Source
    Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
    contributors by the @authors tag. See the copyright.txt in the
    distribution for a full listing of individual contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


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
