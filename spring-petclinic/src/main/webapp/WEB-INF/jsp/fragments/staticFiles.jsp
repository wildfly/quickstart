<!--
    Copyright 2002-2013 the original author or authors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!--
PetClinic :: a Spring Framework demonstration
-->

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>PetClinic :: a Spring Framework demonstration</title>


    <spring:url value="/vendors/bootstrap/docs/assets/css/bootstrap.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="/resources/css/petclinic.css" var="petclinicCss"/>
    <link href="${petclinicCss}" rel="stylesheet"/>

    <spring:url value="/vendors/jquery/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>

    <!-- jquery-ui.js file is really big so we only load what we need instead of loading everything -->
    <spring:url value="/vendors/query-ui/ui/jquery.ui.core.js" var="jQueryUiCore"/>
    <script src="${jQueryUiCore}"></script>

    <spring:url value="/vendors/query-ui/ui/jquery.ui.datepicker.js" var="jQueryUiDatePicker"/>
    <script src="${jQueryUiDatePicker}"></script>

    <!-- jquery-ui.css file is not that big so we can afford to load it -->
    <spring:url value="/vendors/query-ui/themes/base/jquery.ui.base.css" var="jQueryUiCss"/>
    <link href="${jQueryUiCss}" rel="stylesheet"></link>
</head>


