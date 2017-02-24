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
    <title>SpringMVC Matrix Variables Application</title>
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
            <p>You have successfully deployed a SpringMVC web application which showcases Spring's support for matrix
                variables.</p>
        </div>

        <form:form commandName="newMember" id="reg" method="post" action=".">
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

        <h2>Filter</h2>

        <form id="filter">
            <h2>Member Search Filter</h2>
            <table>
                <tbody>
                <tr>
                    <td><label>Name:</label></td>
                    <td><input id="name"/></td>
                </tr>
                <tr>
                    <td><label>Email:</label></td>
                    <td><input id="email"/></td>
                </tr>
                </tbody>
            </table>
            <table>
                <tr>
                    <td>
                        <input type="submit" value="Filter" id="filt" name="filter"/>
                    </td>
                    <td>
                        <input type="submit" value="Clear" id="clear" name="clear"/>
                    </td>
                </tr>
            </table>
        </form>

        <h2>Members</h2>
        <c:choose>
            <c:when test="${members.size()==0}">
                <em>No registered members.</em>
            </c:when>
            <c:otherwise>
                <table id="membersTable" class="simpletablestyle">
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
<script src="resources/js/jquery/jquery-2.0.3.js"></script>
<script type="text/javascript">
    $(document).ready(
        function () {
            $("#filt").click(function (event) {
                event.preventDefault();
                var inputs = $('form#filter :input');
                var matrixvar = "mv/filter;n=" + inputs[0].value + ";e=" + inputs[1].value;
//                 console.log("matrixvar = " + matrixvar);
                $.get(matrixvar, function (data) {
                    // This feels more like a hack to me and should be refactored.  This re-paints the entire page when 
                    //  I think it should just clear the fields and return the correct data.
                    $('body').html(data);
                })
                .done(function(data, textStatus, jqXHR) {
//                     console.log("done filter");
//                     console.log("data = " + data + ", textStatus = " + textStatus + ", jqXHR = " + jqXHR);
//                     alert( "success" );
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("jqXHR.status = " + jqXHR.status);
                    console.log("jqXHR.responseText = " + jqXHR.responseText);
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
//                     alert( "error" );
                })
                // In response to a successful request, the function's arguments are the same as those of .done(): 
                //  data, textStatus, and the jqXHR object. For failed requests the arguments are the same as those of .fail(): 
                //  the jqXHR object, textStatus, and errorThrown.
                .always(function(data, textStatus, jqXHR) {
//                     console.log("always filter");
//                     console.log("data = " + data + ", textStatus = " + textStatus + ", jqXHR = " + jqXHR);
//                     alert( "finished" );
                });
            });

            $("#clear").click(function (event) {
                event.preventDefault();
                $.get("mv/filter;n=;e=", function (data) {
                    // This feels more like a hack to me and should be refactored.  This re-paints the entire page when 
                    //  I think it should just clear the fields and return the correct data.
                    $('body').html(data);
                })
                .done(function(data, textStatus, jqXHR) {
//                     console.log("done clear");
//                     console.log("data = " + data + ", textStatus = " + textStatus + ", jqXHR = " + jqXHR);
//                     alert( "success" );
                })
                .fail(function(jqXHR, textStatus, errorThrown) {
                    console.log("jqXHR.status = " + jqXHR.status);
                    console.log("jqXHR.responseText = " + jqXHR.responseText);
                    console.log("textStatus = " + textStatus);
                    console.log("errorThrown = " + errorThrown);
//                     alert( "error" );
                })
                // In response to a successful request, the function's arguments are the same as those of .done(): 
                //  data, textStatus, and the jqXHR object. For failed requests the arguments are the same as those of .fail(): 
                //  the jqXHR object, textStatus, and errorThrown.
                .always(function(data, textStatus, jqXHR) {
//                     console.log("always clear");
//                     console.log("data = " + data + ", textStatus = " + textStatus + ", jqXHR = " + jqXHR);
//                     alert( "finished" );
                });
            });
        }
    );
</script>
</body>
</html>
