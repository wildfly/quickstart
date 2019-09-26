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
        <p>You have successfully deployed a Jakarta EE web
            application.</p>
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
