<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>kitchensink-jsp</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Here we include the css file  -->
<link rel="stylesheet" type="text/css" href="resources/css/screen.css" />
<head>
<body>
    <div id="container">
        <div align="right" class="dualbrand">
            <img src="resources/gfx/dualbrand_logo.png" />
        </div>
        <div id="content">

            <!-- here we include the Registration Form in the template page
   			using the jsp include directive 
   			 -->

            <%@ include file="registrationForm.jsp"%>


            <!-- Statically inculde the Registration result at compilation time -->
            <%@ include file="registrationResult.jsp"%>

        </div>
        <div id="aside">
            <p>Learn more about JBoss Enterprise Application
                Platform 6.</p>
            <ul>
               <li><a href="https://access.redhat.com/knowledge/docs/JBoss_Enterprise_Application_Platform/">Documentation</a></li>
               <li><a href="http://red.ht/jbeap-6">Product Information</a></li>
            </ul>
            <p>Learn more about JBoss AS 7.</p>
            <ul>
                <li><a
                    href="http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/guide/Introduction/">Getting
                        Started Developing Applications Guide</a></li>
                <li><a href="http://jboss.org/jbossas">Community
                        Project Information</a></li>
            </ul>

        </div>
        <div id="footer">
            <img src="resources/gfx/logo.png" alt="Weld logo" />
            <p>
                This project was generated from a Maven archetype from
                JBoss.<br />
            </p>
        </div>
    </div>
</body>
</html>
