<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>registrationPage.jsp</title>
</head>
<body>
    <h1>Welcome to JBoss!</h1>

    <div>
        <p>You have successfully deployed a Java EE 6 web
            application.</p>
        <h3>Your application can run on:</h3>
        <img src="resources/gfx/dualbrand_as7eap.png" />
    </div>

    <form id="reg" action="register.do" method="POST">
        <h2>Member Registration</h2>
        <table>
            <tr>
                <td style="text-align: right;"><label for="name">Name:</label>
                </td>
                <td><input type="text" id=name name="name"
                    value="${newMember.name}" /></td>
            </tr>
            <tr>
                <td style="text-align: right;"><label for="email">Email:</label>
                </td>

                <td><input type="text" id="email" name="email"
                    value="${newMember.email}" /> <!-- <h:message  for="email" errorClass="invalid" />-->
                </td>
            </tr>
            <tr>
                <td style="text-align: right;"><label
                    for="phoneNumber">Phone #:</label></td>
                <td><input id="phoneNumber" name="phoneNumber"
                    type="text" value="${newMember.phoneNumber}" /></td>
            </tr>
        </table>
        <p>
            <input id="register" type="submit" value="Register" /> 
        </p>
        <p>
            <label style="color: green;width: 100%;text-align: left;">${infoMessage}</label> 
        </p>
        <p>
             <label style="color: red; width: 100%;text-align: left;">${errorMessage}</label>
        </p>
    </form>




</body>
</html>
