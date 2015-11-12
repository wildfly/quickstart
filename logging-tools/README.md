wildfly-logging-tools: Internationalization and Localisation with JBoss Logging Tools
======================
Author: Darrin Mison  
Level: Beginner  
Technologies: JBoss Logging Tools  
Summary: The `jboss-logging-tools` quickstart shows how to use JBoss Logging Tools to create internationalized loggers, exceptions, and messages and localize them.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
------------

The `jboss-logging-tools` quickstart demonstrates the use of JBoss Logging Tools in Red Hat JBoss Enterprise Application Platform. The logging tools create internationalized loggers, exceptions, and generic messages; and then provide localizations for them. This is done using a simple JAX-RS service. Translations in French(fr-FR), German(de-DE), and Swedish (sv-SE) are provided courtesy of <http://translate.google.com> for demonstration. My apologies if they are less than ideal translations.

Once the quick start is deployed you can access it using URLs documented below.

Instructions are included below for starting WildFly with a different locale than the system default.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Configure the WildFly Server to Start With a Different Locale (Optional)
---------------

To start the WildFly server with a different locale than the system default:

1. Make a backup copy of the `WILDFLY_HOME/bin/standalone.conf` file.
2. Edit the file and append commands to set the JVM parameters for the required country and language.
   Eg. Germany and German, `DE` and `de`.
    
        JAVA_OPTS="$JAVA_OPTS -Duser.country=DE"
        JAVA_OPTS="$JAVA_OPTS -Duser.language=de"
   This can be done as a single line if you prefer:

        JAVA_OPTS="$JAVA_OPTS -Duser.country=DE -Duser.language=de"   

   Refer to <http://java.sun.com/javase/technologies/core/basic/intl/faq.jsp#set-default-locale>
      

Start the WildFly Server
-------------------------

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/wildfly-logging-tools.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-logging-tools/>

This landing page provides details and links to test the quickstart features. You can also directly access the following URLs.

1.  `http://localhost:8080/jboss-logging-tools/rest/greetings/'name'` 
    * Example:  <http://localhost:8080/jboss-logging-tools/rest/greetings/Harold>
    * Demonstrates simple use of localized messages (with parameter) and logging.
    * It returns the localized "hello `name`" string where `name` is the last component of the URL.
    * It also logs the localized "Hello message sent" in the server log.

2. `http://localhost:8080/jboss-logging-tools/rest/greetings/'locale'/'name'`
    * Example: <http://localhost:8080/jboss-logging-tools/rest/greetings/fr-FR/Harold>
    * Demonstrates how to obtain a message bundle for a specified locale and how to throw a localized exceptions. Note that the localized exception is a wrapper around `WebApplicationException`.
    * Returns a localized "hello `name`" string where `name` is the last component of the URL and the locale used is the one supplied in the `locale` URL.
    * Logs a localized "Hello message sent in `locale`" message using the JVM locale for the translation.
    * If the supplied locale is invalid (in this case if it contains more than 3 components, eg. fr-FR-POSIX-FOO), it throws a `WebApplicationException` (404) using a localizable sub-class of `WebApplicationException`.
   
      Note that `WebApplicationException` cannot be directly localized by JBoss Logging Tools using the `@Message` annotation due to the message parameter being ignored by the `WebApplicationException` constructors. Cases like this can be worked around by creating a subclass with a constructor that does deal with the message parameter.
   
3. <http://localhost:8080/jboss-logging-tools/rest/greetings/crashme>
    * Demonstrates how to throw a localized exception with another exception specified as the cause.  This is a completely contrived example.
    * Attempts to divide by zero, catches the exception, and throws the localized one.
   
4. `http://localhost:8080/jboss-logging-tools/rest/dates/daysuntil/'targetdate'`
    * Example: <http://localhost:8080/jboss-logging-tools/rest/dates/daysuntil/2020-12-25>
    * Demonstrates how to pass parameters through to the constructor of a localized exception, and how to specify an exception as a cause of a log message. 
    * Attempts to turn the `targetdate` URL component into a date object using the format `dd-MM-yyyy`
    * Returns number of days (as an integer) until that date
    * If the `targetdate` is invalid, for example, <http://localhost:8080/jboss-logging-tools/rest/dates/daysuntil/2015-02-31>:
        * Catches the `ParseException`
        * Creates a localized `ParseException` passing values from the caught exception as parameters to its constructor
        * Logs a localized message with the localized exception as the cause
        * Throws a `WebApplicationException`(400) with the text from the localized `ParseException`


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

      mvn dependency:sources
     


