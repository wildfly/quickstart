<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hello</title>
</head>
<body>
<form method="post" action="greet">
    <table>
        <tr>
           <td>Enter username: </td>
           <td><input type="text" name="username" value="<c:out value="${username}"/>"></td>
        </tr>
        <tr>
    <td colspan="2"><c:out value="${message}"/></td>
        </tr>
        <tr><td colspan="2" align="center">
    <input type="submit" value="Greet!"></td>
        </tr>
        <tr><td colspan="2" align="center"><a href="create">Add a new user!</a></td></tr>
     </table>
</form>

</body>
</html>