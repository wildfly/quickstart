kitchensink-ml: Localized application using an assortment of technologies including Arquillian
========================
Author: Sande Gilda
Level: Intermediate
Technologies: CDI, JSF, JPA, EJB, JPA, JAX-RS, BV
Summary: A localized version of kitchensink
Target Product: EAP

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

This project is setup to allow you to create a _localized_ Java EE 6 compliant application using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. A localized application is one that supports multiple languages. That is what the _-ml_ suffix denotes in the quickstart name `kitchensink-ml`. This quickstart also includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java. 

This quickstart uses the `kitchensink` quickstart as its starting point. It has been enhanced to provide localization of labels and messages. A user sets the preferred language choice in the browser and, if the application supports that language, the application web page is rendered in that language. For demonstration purposes, this quickstart has been tranlated into French(fr) and Spanish (es) using <http://translate.google.com>, so the translations may not be ideal.

### Localization Code Changes

The following changes were made to the quickstart to enable it to use the browser preferred locale setting when displaying the web page:

* Properties files were created for the supported languages. 

    * They are located in the `src/main/resources/org/jboss/as/quickstarts/kitchensink-ml/bundle` directory. 
    
    * This quickstart is localized for Spanish (`Resources_es.properties`) and  French (`Resources_fr.properties`). You can add additional language support by creating properties files with the appropriate suffix and populating the properties with translated values.

* The following XML was added to the `src/main/webapp/WEB-INF/faces-config.xml` file. When you create a property file for a new language, you must add the supported locale to this file.

        <application>
          <locale-config>
                <default-locale>en</default-locale>
                <supported-locale>en-US</supported-locale>
                <supported-locale>es</supported-locale>
                <supported-locale>ES-FR</supported-locale>
                <supported-locale>fr</supported-locale>
                <supported-locale>fr-FR</supported-locale>
            </locale-config>
		    <resource-bundle>
		        <base-name>org/jboss/as/quickstarts/kitchensink-ml/bundle/Resources</base-name>
		        <var>bundle</var>
		    </resource-bundle>
        </application>

* The `src/main/java/org/jboss/as/quickstarts/kitchensink/util/Resources.java` file was modified to add the ResourceBundle producer that loads the correct resource bundle using the browser preferred locale.

        @Produces
        public ResourceBundle produceResourceBundle() {
            return ResourceBundle.getBundle("org.jboss.as.quickstarts.kitchensink-ml.bundle.Resources", FacesContext
                    .getCurrentInstance().getViewRoot().getLocale());
        }

* The `src/main/java/org/jboss/as/quickstarts/kitchensink/controller/MemberController.java` file was modified as follows:

    * It injects the ResourceBundle. 
    
            @Inject
            private ResourceBundle resourceBundle;


    * Messages strings were replaced with strings retrieved using the resource bundle property names. For example:
    
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                                        (String) resourceBundle.getObject("registeredMsg"), 
                                        (String) resourceBundle.getObject("registerSuccessfulMsg"));

* The `index.xhtml` and `template.xhtml` files were modified.

    * The following namespace was added: `xmlns:f="http://java.sun.com/jsf/core`
    * The resource bundle was loaded using: `<f:loadBundle basename="org.jboss.as.quickstarts.kitchensink-ml.bundle.Resources" var="bundle" />`
    * Strings for headers, messages, labels were replaced with the appropriate `#{bundle.<property>}`, for example: `#{bundle.memberWelcomeHeader}`.
    
### Set the Browser Preferred Locale

How you set your browser preferred locale depends on the browser and version you use. Use your browser help option to search for instructions to change the preferred language setting.



System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


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

4. This will deploy `target/jboss-as-kitchensink-ml.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-kitchensink-ml/>.

Change your browser preferred language to French or Spanish and refresh the page to see it displayed in the new language. 

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
