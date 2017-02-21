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
    <title>Spring MVC Starter Application</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css"
          href="<c:url value="/static/resources/css/screen.css"/>"/>
</head>

<body>
<div id="container">
    <div class="dualbrand">
        <img src="<c:url value="/static/resources/gfx/rhjb_eap_logo.png"/>"/>
    </div>
    <div id="content">
        <p>Global Error Handler was invoked with:</p>
        <c:out value="${error}"></c:out>
    </div>
    <div id="footer">
        <p>
            This JBoss EAP quickstart project was generated from a Maven archetype.<br/>
        </p>
    </div>
</div>
</body>
</html>
