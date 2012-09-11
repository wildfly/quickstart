jboss-as-logging-tools: Internationalization and Localisation with JBoss Logging Tools
======================
Author: Darrin Mison
Level: Beginner
Technologies: JBoss Logging Tools
Summary: Demonstrates the use of JBoss Logging Tools to create internationalized loggers, exceptions, and generic messages
Target Product: EAP

What is it?
------------

This quick start demonstrates the use of JBoss Logging Tools to create internationalized loggers, exceptions, and generic messages; and then provide localizations for them. This is done using a simple JAX-RS service. Translations in French(fr-FR), German(de-DE), and Swedish (sv-SE) are provided courtesy of <http://translate.google.com> for demonstration. My apologies if they are less than ideal translations.

Once the quick start is deployed you can access it using URLs documented below.

Instructions are included below for starting JBoss AS7/EAP6 with a different locale than the system default.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Configure the JBoss Server to Start With a Different Locale (Optional)
---------------

To start the JBoss server with a different locale than the system default:

1. Make a backup copy of the `JBOSS_HOME/bin/standalone.conf` file.
2. Edit the file and append commands to set the JVM parameters for the required country and language.  
   Eg. Germany and German, `DE` and `de`.
    
        JAVA_OPTS="$JAVA_OPTS -Duser.country=DE"
        JAVA_OPTS="$JAVA_OPTS -Duser.language=de"
   This can be done as a single line if you prefer:

        JAVA_OPTS="$JAVA_OPTS -Duser.country=DE -Duser.language=de"   

   Refer to <http://java.sun.com/javase/technologies/core/basic/intl/faq.jsp#set-default-locale>
      

Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._


1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-logging-tools.war` to the running instance of the server.



Access the application 
---------------------

The application will be running at the following URLs:

### http://localhost:8080/jboss-as-logging-tools/

A landing page is found here that has a quick reference to the other URLs.

### http://localhost:8080/jboss-as-logging-tools/rest/greetings/`name`

Demonstrates simple use of localised messages (with parameter) and logging.

Example: <http://localhost:8080/jboss-as-logging-tools/rest/greetings/Harold>

* Returns a localised "hello `name`" string where `name` is the last component of the URL.
* Logs a localised "Hello message sent"

### http://localhost:8080/jboss-as-logging-tools/rest/greetings/`locale`/`name`

Demonstrates how to obtain a message bundle for a specified locale and how to throw a localised exceptions. Note that the localised exception is a wrapper around `WebApplicationException`.

Example: <http://localhost:8080/jboss-as-logging-tools/rest/greetings/fr-FR/Harold>
      
* Returns a localised "hello `name`" string where `name` is the last component of the URL and the locale used is the one supplied in the `locale` URL.
* Logs a localised "Hello message sent in `locale`" message using the JVM locale for the translation
* If the supplied locale is invalid (in this case if it contains more than 3 components, eg. fr-FR-POSIX-FOO):
    * Throws a WebApplicationException (404) using a localizable sub-class of `WebApplicationException` 
   
      Note that WebApplicationException cannot be directly localised by JBoss Logging Tools using the `@Message` annotation due to the message parameter being ignored by `WebApplicationException`'s constructors. Cases like this can be worked around by creating a sub-class with a constructor that does deal with the message parameter.
   
### http://localhost:8080/jboss-as-logging-tools/rest/greetings/crashme
   
Demonstrates throwing a localised exception with another exception specified as the cause.  This is a completely contrived example.
   
Example: <http://localhost:8080/jboss-as-logging-tools/rest/greetings/crashme>
   
* Attempts divide by zero, catches exception and throws localised one.
   
### http://localhost:8080/jboss-as-logging-tools/rest/dates/daysuntil/`targetdate`

Demonstrates how to pass parameters through to the constructor of a localised exception, and how to specify an exception as a cause of a log message.

Example: <http://localhost:8080/jboss-as-logging-tools/rest/dates/daysuntil/25-12-2012>
   
* Attempts to turn the `targetdate` URL component into a date object using the format `dd-MM-yyyy`
* Returns number of days (as an integer) until that date
* If the `targetdate` is invalid:
    * Catches the `ParseException`
    * Creates a localised `ParseException` passing values from the caught exception as parameters to it's constructor
    * Logs a localised message with the localised exception as the cause
    * Throws a `WebApplicationException`(400) with the text from the localised `ParseException`


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc



