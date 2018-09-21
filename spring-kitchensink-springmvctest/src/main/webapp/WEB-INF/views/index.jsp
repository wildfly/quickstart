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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <title>SpringMVC-Test Application</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/resources/css/screen.css"/>"/>
</head>

<body>
<div id="container">
    <div class="dualbrand">
        <img src="<c:url value="/static/resources/gfx/rhjb_eap_logo.png"/>"/>
    </div>
    <div id="content">
        <h1>Welcome to JBoss!</h1>

        <div>
            <p>You have successfully deployed a SpringMVC web application which used Spring MVC Tests during the
                build.</p>
        </div>

        <form:form modelAttribute="newMember" id="reg">
            <h2>Member Registration</h2>

            <p>Enforces annotation-based constraints defined on the model class.</p>
            <table>
                <tbody>
                <tr>
                    <td><form:label path="name">Name:</form:label></td>
                    <td><form:input path="name"/></td>
                    <td><form:errors class="invalid" path="name"/></td>
                </tr>
                <tr>
                    <td><form:label path="email">Email:</form:label></td>
                    <td><form:input path="email"/></td>
                    <td><form:errors class="invalid" path="email"/></td>
                </tr>
                <tr>
                    <td><form:label path="phoneNumber">Phone #:</form:label></td>
                    <td><form:input path="phoneNumber"/></td>
                    <td><form:errors class="invalid" path="phoneNumber"/></td>
                </tr>
                <tr>
                    <td><p style="color: red">${error}</p></td>
                </tr>
                </tbody>
            </table>
            <table>
                <tr>
                    <td>
                        <input type="submit" value="Register" class="register"/>
                        <input type="reset" value="Cancel" class="cancel"/>
                    </td>
                </tr>
            </table>
        </form:form>
        <h2>Members</h2>
        <c:choose>
            <c:when test="${members.size()==0}">
                <em>No registered members.</em>
            </c:when>
            <c:otherwise>
                <table class="simpletablestyle">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Phone #</th>
                            <th>REST URL</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${members}" var="member">
                            <tr>
                                <td>${member.id}</td>
                                <td>${member.name}</td>
                                <td>${member.email}</td>
                                <td>${member.phoneNumber}</td>
                                <td><a href="<c:url value="/rest/members/${member.id}"/>">/rest/members/${member.id}</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <table class="simpletablestyle">
                    <tr>
                        <td>
                            REST URL for all members: <a href="<c:url value="/rest/members"/>">/rest/members</a>
                        </td>
                    </tr>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="aside">
        <p>Learn more about Red Hat JBoss Enterprise Application Platform.</p>
        <ul>
            <li><a href="https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/">Documentation</a></li>
            <li><a href="http://www.redhat.com/en/technologies/jboss-middleware/application-platform">Product Information</a></li>
        </ul>
    </div>
    <div id="footer">
        <p>
           This JBoss EAP quickstart project was generated from a Maven archetype.<br/>
        </p>
    </div>
</div>
</body>
</html>
