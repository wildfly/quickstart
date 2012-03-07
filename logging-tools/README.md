# jboss-as-logging-tools: Internationalization and Localisation with JBoss Logging Tools

Authors: Darrin Mison dmison@me.com (dmison@redhat.com)

## What is it?

This quick start demonstrates the use of JBoss Logging Tools to create internationalized loggers,
exceptions, and generic messages; and then provide localizations for them. This is done using a
simple JAX-RS service. Translations in French(fr-FR), German(de-DE), and Swedish (sv-SE) are
provided courtesy of translate.google.com for demonstration. My apologies if they are less than
ideal translations.

Once the quick start is deployed you can access it using URLs documented below.

Instructions are included below for starting JBoss AS7/EAP6 with a different locale than the system 
default.


## System requirements

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. The following
instructions target JBoss AS 7, but they also apply to JBoss EAP 6.


## Building and deploying

Follow these steps to build, deploy and run the quick start.

1. Optional: Configure AS7/EAP6 to start with a different locale.

   To start AS7/EAP6 with a different locale than the system default, edit the `$JBOSS_HOME/bin/standalone.conf` 
   and append the lines to add the JVM parameters for the required country and language.  It is recommended that a 
   backup copy be made of this file to restore to in case of difficulties or when testing is complete.
    
   Eg. Germany and German, `DE` and `de`.
    
        JAVA_OPTS="$JAVA_OPTS -Duser.country=DE"
        JAVA_OPTS="$JAVA_OPTS -Duser.language=de"

    This can be done as a single line if you prefer:

        JAVA_OPTS="$JAVA_OPTS -Duser.country=DE -Duser.language=de"	  

     Refer to <http://java.sun.com/javase/technologies/core/basic/intl/faq.jsp#set-default-locale>
      
2. Start JBoss AS 7 (or EAP 6):

         $JBOSS_HOME/bin/standalone.sh

3. Build the application

         mvn clean package
	   
4. Deploy the application 

         mvn jboss-as:deploy
	   
## Running the quick start

Once deployed, you can access the quick start using the following URLs.

### http://localhost:8080/jboss-as-logging-tools/rest/greetings/`name`

   Demonstrates simple use of localised messages (with parameter) and logging.

   Example: <http://localhost:8080/jboss-as-logging-tools/rest/greetings/Harold>

   * returns a localised "hello `name`" string where `name` is the last component of the URL.
   * logs a localised "Hello message sent"

### http://localhost:8080/jboss-as-logging-tools/rest/greetings/`locale`/`name`

   Demonstrates how to obtain a message bundle for a specified locale and how to throw a localised
   exceptions. Note that the localised exception is a wrapper around `WebApplicationException`.

   Example: <http://localhost:8080/jboss-as-logging-tools/rest/greetings/fr-FR/Harold>
      
   * returns a localised "hello `name`" string where `name` is the last component of the URL and
     the locale used is the one supplied in the `locale` URL.
   * logs a localised "Hello message sent in `locale`" message using the JVM locale for the translation
   * if the supplied locale is invalid (in this case if it contains more than 3 components, eg. fr-FR-POSIX-FOO):
      * throws a WebApplicationException (404) using a localizable sub-class of `WebApplicationException` 
   
   Note that WebApplicationException cannot be directly localised by JBoss Logging Tools using the
   `@Message` annotation due to the message parameter being ignored by `WebApplicationException`'s
   constructors. Cases like this can be worked around by creating a sub-class with a constructor
   that does deal with the message parameter.
   
### http://localhost:8080/jboss-as-logging-tools/rest/greetings/crashme
   
   Demonstrates throwing a localised exception with another exception specified as the cause.  This is a 
   completely contrived example.
   
   Example: <http://localhost:8080/jboss-as-logging-tools/rest/greetings/crashme>
   
   * attempts divide by zero, catches exception and throws localised one.
   
### http://localhost:8080/jboss-as-logging-tools/rest/dates/daysuntil/`targetdate`

   Demonstrates how to pass parameters through to the constructor of a localised exception, and
   how to specify an exception as a cause of a log message.

   Example: <http://localhost:8080/jboss-as-logging-tools/rest/dates/daysuntil/25-12-2012>
   
   * attempts to turn the `targetdate` URL component into a date object using the format `dd-MM-yyyy`
   * returns number of days (as an integer) until that date
   * if the `targetdate` is invalid:
   
     * catches the `ParseException`
     * creates a localised `ParseException` passing values from the caught exception as parameters to it's constructor
     * logs a localised message with the localised exception as the cause
     * throws a `WebApplicationException`(400) with the text from the localised `ParseException`

## Undeploying the quick start

The quick start can be un-deployed from the Management Console or with:

    mvn jboss-as:undeploy
