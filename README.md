JBoss AS Quickstarts
====================


Introduction
---------------------

The quickstarts included in this distribution were written to demonstrate Java EE 6 and a few additional technologies. They provide small, specific, working examples that can be used as a reference for your own project.

These quickstarts run on both JBoss Enterprise Application Platform 6 and JBoss AS 7. If you want to run the quickstarts on JBoss Enterprise Application Platform 6, we recommend using the JBoss Enterprise Application Platform 6 ZIP file. This version uses the correct dependencies and ensures you test and compile against your runtime environment. 

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

* [Available Quickstarts](#availableQuickstarts): List of the available quickstarts and details about each one.

* [System Requirements](#systemrequirements): List of software required to run the quickstarts.

* [Configure Maven](#mavenconfiguration): How to configure the Maven repository for use by the quickstarts.

* [Run the Quickstarts](#runningquickstarts): General instructions for building, deploying, and running the quickstarts.

* [Run the Arquillian Tests](#arquilliantests): How to run the Arquillian tests provided by some of the quickstarts.

* [Suggested Approach to the Quickstarts](#suggestedApproach): A suggested approach on how to work with the quickstarts.

* [Optional Components](#optionalcomponents): How to install and configure optional components required by some of the quickstarts.


<a id="availableQuickstarts"></a>

Available Quickstarts 
---------------------

The following is a list of the currently available quickstarts. The table lists each quickstart name, the technologies it demonstrates, gives a brief description of the quickstart, and the level of experience required to set it up. For more detailed information about a quickstart, click on the quickstart name.

Some quickstarts are designed to enhance or extend other quickstarts. These are noted in the **Prerequisites** column. If a quickstart lists prerequisites, those must be installed or deployed before working with the quickstart.

Quickstarts with tutorials in the [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide "Getting Started Developing Applications Guide") are noted with two asterisks ( ** ) following the quickstart name. 

| **Quickstart Name** | **Demonstrated Technologies** | **Description** | **Experience Level Required** | **Prerequisites** |
|:-----------|:-----------|:-----------|:-----------|:-----------|
| [bean-validation](bean-validation/README.md "bean-validation") | Bean Validation, JPA | Shows how to use Arquillian to test Bean Validation | Beginner | None |
| [bmt](bmt/README.md "bmt") | EJB, Bean-Managed Transactions (BMT) | EJB that demonstrates bean-managed transactions (BMT) | Beginner | None |
| [cdi-injection](cdi-injection/README.md "cdi-injection") | CDI injection, Qualifiers, Servlet | Demonstrates the use of CDI 1.0 Injection and Qualifiers with JSF as the front-end client. | Beginner | None |
| [cmt] (cmt/README.md "cmt") | EJB, Container-Managed Transactions (CMT) | EJB that demonstrates container-managed transactions (CMT) | Intermediate | None |
| [ejb-in-ear](ejb-in-ear/README.md "ejb-in-ear") | EJB, JSF, JAR, and WAR deployed as an EAR | Packages an EJB JAR and WAR in an EAR | Beginner | None |
| [ejb-in-war](ejb-in-war/README.md "ejb-in-war") | EJB and JSF deployed as a WAR | Packages an EJB JAR in a WAR | Beginner | None |
| [ejb-remote](ejb-remote/README.md "ejb-remote") | Remote EJB | Shows how to access an EJB from a remote Java client program using JNDI | Beginner | None |
| [ejb-security](ejb-security/README.md "ejb-security") | EJB, Security | Shows how to use Java EE Declarative Security to Control Access to EJB 3 | Beginner | None |
| [forge-from-scratch](forge-from-scratch/README.md "forge-from-scratch") | Forge | Demonstrates how to generate a fully Java EE compliant project using nothing but JBoss Forge | Beginner | None |
| [greeter](greeter/README.md "greeter") | CDI, JSF, JPA, EJB, JTA | Demonstrates the use of CDI 1.0, JPA 2.0, JTA 1.1, EJB 3.1 and JSF 2.0 | Beginner | None |
| [h2-console](h2-console/README.md "h2-console") | H2 Database Console | Shows how to use the H2 console with JBoss AS | Beginner | greeter |
| [helloworld**](helloworld/README.md "helloworld") | Basic CDI, Servlet | Basic example that can be used to verify that the server is configured and running correctly | Beginner | None |
| [helloworld-errai](helloworld-errai/README.md "helloworld-errai") | Errai, JAX-RS | Helloworld using the Errai framework  | Beginner | None |
| [helloworld-gwt](helloworld-gwt/README.md "helloworld-gwt") | GWT | Demonstrates the use of CDI 1.0 and JAX-RS with a GWT front-end client  | Beginner | None |
| [helloworld-html5](helloworld-html5/README.md "helloworld-html5") | Basic HTML5 |Demonstrates the use of CDI 1.0 and JAX-RS using the POH5 architecture and RESTful services on the backend | Beginner | None |
| [helloworld-jms](helloworld-jms/README.md "helloworld-jms") | JMS | Demonstrates the use of a standalone (Java SE) JMS client | Intermediate | None |
| [helloworld-jsf](helloworld-jsf/README.md "helloworld-jsf") | Basic CDI, JSF | Similar to the helloworld quickstart, but with a JSF front end | Beginner | None |
| [helloworld-mdb](helloworld-mdb/README.md "helloworld-mdb") | Basic JMS, Message-Driven Bean (MDB) | Demonstrates the use of JMS 1.1 and EJB 3.1 Message-Driven Bean  | Intermediate | None |
| [helloworld-osgi**](helloworld-osgi/README.md "helloworld-osgi") | OSGi JAR | Shows how to create and deploy a simple OSGi Bundle | Beginner | None |
| [helloworld-rs](helloworld-rs/README.md "helloworld-rs") | CDI, JAX-RS | Demonstrates the use of CDI 1.0 and JAX-RS | Intermediate | None |
| [helloworld-singleton](helloworld-singleton/README.md "helloworld-singleton") | Singleton Session Bean | Demonstrates the use of an EJB 3.1 Singleton Session Bean, instantiated once, maintaining state for the life of the session | Beginner | None |
| [hibernate3](hibernate3/README.md "hibernate3") | Hibernate 3 | Performs the same functions as _hibernate4_ quickstart, but uses Hibernate version 3 for database access | Beginner | None |
| [hibernate4](hibernate4/README.md "hibernate4") | Hibernate 4 | Performs the same functions as _hibernate3_ quickstart, but uses Hibernate version 4 for database access | Beginner | None |
| [jax-rs-client](jax-rs-client/README.md "jax-rs-client") | JAX-RS | Demonstrates the use an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses CDI 1.0 and JAX-RS | Intermediate | helloworld-rs |
| [jta-crash-rec](jta-crash-rec/README.md "jta-crash-rec") | JTA, Crash Recovery | Uses Java Transaction API and JBoss Transactions to demonstrate recovery of a crashed transaction | Advanced | cmt |
| [jts](jts/README.md "jts") | JTS | Uses Java Transaction Service (JTS) to coordinate distributed transactions | Intermediate | cmt |
| [jts-distributed-crash-rec](jts-distributed-crash-rec/README.md "jts-distributed-crash-rec") | JTS | Demonstrates recovery of distributed crashed components | Advanced | jts |
| [kitchensink**](kitchensink/README.md "kitchensink") | CDI, JSF, JPA, EJB, JPA, JAX-RS, BV | An example that incorporates multiple technologies | Beginner | None |
| [kitchensink-ear](kitchensink-ear/README.md "kitchensink-ear")  | EAR | Based on kitchensink, but deployed as an EAR | Beginner | None |
| [kitchensink-html5-mobile](kitchensink-html5-mobile/README.md "kitchensink-html5-mobile") | HTML5 | Based on kitchensink, but uses HTML5, making it suitable for mobile and tablet computers | Beginner | None |
| [kitchensink-jsp](kitchensink-jsp/README.md "kitchensink-jsp") | JSP | Based on kitchensink, but uses a JSP for the user interface | Beginner | None |
| [log4j](log4j/README.md "log4j") | JBoss Modules | Demonstrates how to use modules to control class loading for 3rd party logging frameworks | Beginner | None |
| [logging-tools](logging-tools/README.md "logging-tools") | JBoss Logging Tools | Demonstrates the use of JBoss Logging Tools to create internationalized loggers, exceptions, and generic messages | Beginner | None |
| [mail](mail/README.md "mail") | JavaMail | Demonstrates the use of JavaMail | Beginner | None |
| [numberguess**](numberguess/README.md "numberguess") | CDI, JSF | Demonstrates the use of CDI 1.0 and JSF 2.0  | Beginner | None |
| [payment-cdi-event](payment-cdi-event/README.md "payment-cdi-event") | CDI, Events | Demonstrates how to use CDI 1.0 Events  | Beginner | None |
| [richfaces-validation](richfaces-validation/README.md "richfaces-validation") | RichFaces | Demonstrates RichFaces and bean validation | Beginner | None |
| [servlet-async](servlet-async/README.md "servlet-async") | CDI, EJB, Servlet | Demonstrates CDI, plus asynchronous Servlets and EJBs | Intermediate | None |
| [servlet-filterlistener](servlet-filterlistener/README.md "servlet-filterlistener") | Servlet | Demonstrates Servlet filters and listeners | Intermediate | None |
| [servlet-security](servlet-security/README.md "servlet-security") | Servlet, Security | Demonstrates how to use Java EE declarative security to control access to Servlet 3 | Beginner | None |
| [shopping-cart](shopping-cart/README.md "shopping-cart") | Stateful Session Bean (SFSB) | Demonstrates a stateful session bean | Beginner | None |
| [tasks](tasks/README.md "tasks") | Arquillian, JPA, CRUD | Demonstrates testing JPA using Arquillian | Intermediate | None |
| [tasks-jsf](tasks-jsf/README.md "tasks-jsf") | JSF, JPA | Provides a JSF 2.0 as view layer for the `tasks` quickstart | Intermediate | None |
| [tasks-rs](tasks-rs/README.md "tasks-rs") | JAX-RS, JPA | Demonstrates how to use JAX-RS and JPA together | Intermediate | None |
| [temperature-converter](temperature-converter/README.md "temperature-converter") | Stateless Session Bean (SLSB) | Demonstrates a stateless session bean | Intermediate | None |
| [wicket-ear](wicket-ear/README.md "wicket-ear") | Apache Wicket, CRUD, JPA | Demonstrates how to use the Wicket Framework 1.5 with the JBoss server using the Wicket-Stuff Java EE integration, packaged as an EAR  | Intermediate | None |
| [wicket-war](wicket-war/README.md "wicket-war") | Apache Wicket, CRUD, JPA | Demonstrates how to use the Wicket Framework 1.5 with the JBoss server using the Wicket-Stuff Java EE integration packaged as a WAR  | Intermediate | None |
| [wsat-simple](wsat-simple/README.md "wsat-simple") | WS-AT, Web service, JAX-WS | Deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a WAR archive  | Intermediate | None |
| [wsba-coordinator-completion-simple](wsba-coordinator-completion-simple/README.md "wsba-coordinator-completion-simple") | WS-BA, Web service, JAX-WS | Deployment of a WS-BA (WS-BusinessActivity) enabled JAX-WS Web service bundled in a WAR archive (Participant Completion protocol) | Intermediate | None |
| [wsba-participant-completion-simple](wsba-participant-completion-simple/README.md "wsba-participant-completion-simple") | WS-BA, Web service, JAX-WS | Deployment of a WS-BA (WS-BusinessActivity) enabled JAX-WS Web service bundled in a war archive (Coordinator Completion protocol) | Intermediate | None |
| [xml-dom4j](xml-dom4j/README.md "xml-dom4j") | DOM4J, Servlet, JSF 2.0 | Demonstrates how to upload an XML file and parse it using 3rd party XML parsing libraries | Intermediate | None |
| [xml-jaxp](xml-jaxp/README.md "xml-jaxp") | JAXP, SAX, DOM, Servlet | Upload, validation and parsing of XML using SAX or DOM | Intermediate | None |


<a id="suggestedApproach"></a>
Suggested Approach to the Quickstarts 
--------------------------------------

We suggest you approach the quickstarts as follows:

* Regardless of your level of expertise, we suggest you start with the **helloworld** quickstart. It is the simplest example and is an easy way to prove your server is configured and started correctly.
* If you are a beginner or new to JBoss, start with the quickstarts labeled **Beginner**, then try those marked as **Intermediate**. When you're comfortable with those, move on to the **Advanced** quickstarts.
* Some quickstarts are based upon other quickstarts but have expanded capabilities and functionality. If a prerequisite quickstart is listed, be sure to deploy and test it before looking at the expanded version.


<a id="systemrequirements"></a>
System Requirements 
-------------

To run these quickstarts with the provided build scripts, you need the following:

1. Java 1.6, to run JBoss AS and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version 

3. The JBoss Enterprise Application Platform 6 distribution ZIP or the JBoss AS 7 distribution ZIP.
    * For information on how to install and run JBoss, refer to the product documentation.

4. You can also use [JBoss Developer Studio or Eclipse](#useeclipse) to run the quickstarts. 


<a id="mavenconfiguration"></a>
Configure Maven 
-------------

Maven configuration is dependent on whether you are running JBoss Enterprise Application Platform 6 or JBoss AS7.

<a id="eap6mavenconfig"></a>
### Configure Maven for JBoss Enterprise Application Platform 6 

If you are using the JBoss Enterprise Application Platform 6 distribution, you need to download and configure the Maven repository.

1. Download the JBoss Enterprise Application Platform 6 Maven repository distribution ZIP and unzip it into a directory of your choice.

2. Modify the `example-settings.xml` file located in the root of your quickstarts folder. Replace all instances of `path/to/jboss-eap/repo` within `file:///path/to/jboss-eap/repo` with the fully qualified path to the Maven repository you unzipped in the previous step.

3. When you run Maven commands, you need to append `-s PATH_TO_QUICKSTARTS/example-settings.xml` to the command, for example:

        mvn jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml

4. If you do not want to append the alternate path parameter each time you issue a Maven command, you can configure the Maven user settings as follows: 
    * If you have an existing `~/.m2/settings.xml` file, modify it with the configuration information from the `example-settings.xml` file.
    * If there is no `~/.m2/settings.xml` file, copy the modified `example-settings.xml` file to the `~/.m2` directory and rename it to `settings.xml`.

<a id="as7mavenconfig"></a>
### Configure Maven for JBoss AS 7

If you are using the JBoss AS 7 Quickstart distribution, the community artifacts are available in the Maven central repository so no additional configuration is needed.

<a id="runningquickstarts"></a>
Run the Quickstarts 
-------------------

The root folder of each quickstart contains a README file with specific details on how to build and run the example. In most cases you do the following:

* [Start the JBoss server](#startjboss)
* [Build and deploy the quickstart](#buildanddeploy)


<a id="startjboss"></a>
### Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server

Before you deploy a quickstart, in most cases you need a running server. A few of the Arquillian tests do not require a running server. This will be noted in the README for that quickstart. 

The JBoss server can be started a few different ways.

* [Start the JBoss Server With the _web_ profile](#startserverweb): This is the default configuration. It defines minimal subsystems and services.
* [Start the JBoss Server with the _full_ profile](#startserverfull): This profile configures many of the commonly used subsystems and services.
* [Start the JBoss Server with a custom configuration](#startservercustom): Custom configuration parameters can be specified on the command line when starting the server.    

The README for each quickstart will specify which configuration is required to run the example.

<a id="startserverweb"></a>
#### Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

<a id="startserverfull"></a>
#### Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml

<a id="startservercustom"></a>
#### Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with Custom Configuration Options

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server. Replace the CUSTOM_OPTIONS with the custom optional parameters specified in the quickstart.

        For Linux:   JBOSS_HOME/bin/standalone.sh CUSTOM_OPTIONS
        For Windows: JBOSS_HOME\bin\standalone.bat CUSTOM_OPTIONS
           
<a id="buildanddeploy"></a>
### Build and Deploy the Quickstarts 

Review the README file in the root folder of the quickstart for specific details on how to build and run the example. In most cases you do the following:

1. The command used to build the quickstart depends on the individual quickstart, the server version, and how you configured Maven.
    * If you are running JBoss Enterprise Application Platform 6 and did not configure the Maven user settings described in [Configure Maven for JBoss Enterprise Application Platform 6](#eap6mavenconfig) above, you need to specify command line arguments. 
    * If you are running JBoss AS 7, it uses community artifacts available in the Maven central repository, so command line arguments are not usually required. 
2. See the README file in each individual quickstart folder for specific details and information on how to run and access the example.

#### Build the Quickstart Archive

In some cases, you may want to build the application to test for compile errors or view the contents of the archive. 

1. Open a command line and navigate to the root directory of the quickstart you want to build.
2. Use this command if you only want to build the archive, but not deploy it:

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean package -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean package

#### Build and Deploy the Quickstart Archive

1. Make sure you [start the JBoss Server](#startjboss) as described in the README.
2. Open a command line and navigate to the root directory of the quickstart you want to run.
3. Use this command to build and deploy the archive:

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean package jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean package jboss-as:deploy

#### Undeploy an Archive

The command to undeploy the quickstart is simply: 

        mvn jboss-as:undeploy
 

<a id="arquilliantests"></a>
### Run the Arquillian Tests 
-------------------------

Some of the quickstarts provide Arquillian tests. By default, these tests are configured to be skipped, as Arquillian tests require the use of a container. 

You can run these tests using either a remote or managed container. The quickstart README should tell you what you should expect to see in the console output and server log when you run the test.

<a id="testremote"></a>

1. Test the quickstart on a Remote Server
    * A remote container requires you start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server before running the test. [Start the JBoss Server](#startjboss) as described in the quickstart README file.
    * Run the test goal with the following profile activated:

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean test -Parq-jbossas-remote -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean test -Parq-jbossas-remote 
<a id="testmanaged"></a>

2. Test the quickstart on Managed Server

    _Note: This test requires that your server is not running. Arquillian will start the container for you, however, you must first let it know where to find the remote JBoss container._
    * Open the test/resources/arquillian.xml file located in the quickstart directory. 
    * Find the configuration for the remote JBoss container. It should look like this:

            <container qualifier="jboss" default="true">
                <!-- If you want to use the JBOSS_HOME environment variable, just delete the jbossHome property -->
                <configuration>
                    <property name="jbossHome">/path/to/jboss/as</property>
                </configuration>
            </container>
    * Find the "jbossHome" property and replace the "/path/to/jboss/as" value with the actual path to your JBoss Enterprise Application Platform 6 or JBoss AS 7 server.
    * Run the test goal with the following profile activated:

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean test -Parq-jbossas-managed  -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean test -Parq-jbossas-managed


<a id="useeclipse"></a>
Use JBoss Developer Studio or Eclipse to Run the Quickstarts
-------------------------------------
You can also deploy the quickstarts from Eclipse using JBoss tools. For more information on how to set up Maven and the JBoss tools, refer to the [JBoss Enterprise Application Platform 6 Beta](http://docs.redhat.com/docs/en-US/JBoss_Enterprise_Application_Platform/6/html/Beta_Documentation/HOME.html) documentation or the [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide "Getting Started Developing Applications Guide").


<a id="optionalcomponents"></a>
Optional Components 
-------------------
The following components are needed for only a small subset of the quickstarts. Do not install or configure them unless the quickstart requires it.

* [Add a User](#adduser): Add a Management or Application user for the quickstarts that run in a secured mode.

* [Install and Configure the PostgreSQL Database](#postgresql): The PostgreSQL database is used for the distributed transaction quickstarts.

* [Install and Configure Byteman](#byteman): This tool is used to demonstrate crash recovery for distributed transaction quickstarts.


<a id="adduser"></a>
### Add a Management or Application User

By default, JBoss Enterprise Application Platform 6 and JBoss AS 7 are now distributed with security enabled for the management interfaces. A few of the quickstarts use these management interfaces and require that you create a management or application user to access the running application. A script is provided in the `JBOSS_HOME/bin` directory for that purpose.

The following procedures describe how to add a user with the appropriate permissions to run the quickstarts that depend on them.


<a id="addmanagementuser"></a>
#### Add an Management User
1. Open a command line
2. Type the command for your operating system

        For Linux:   JBOSS_HOME/bin/add-user.sh
        For Windows: JBOSS_HOME\bin\add-user.bat
3. You should see the following response:

        What type of user do you wish to add? 

        a) Management User (mgmt-users.properties) 
        b) Application User (application-users.properties)
        (a):

    At the prompt, press enter to use the default Management User
4. You should see the following response:

        Enter the details of the new user to add.
        Realm (ManagementRealm) : 

    If the quickstart README specifies a realm, type it here. Otherwise, press enter to use the default `ManagementRealm`. 
5. When prompted, enter the following
 
        Username : admin
        Password : (choose a password for the admin user)
    Repeat the password
6. Choose yes for the remaining promts.


<a id="addapplicationuser"></a>
#### Add an Application User 

1. Open a command line
2. Type the command for your operating system

        For Linux:   JBOSS_HOME/bin/add-user.sh
        For Windows: JBOSS_HOME\bin\add-user.bat
3. You should see the following response:

        What type of user do you wish to add? 

        a) Management User (mgmt-users.properties) 
        b) Application User (application-users.properties)
        (a):

    At the prompt, type:  b
4. You should see the following response:

        Enter the details of the new user to add.
        Realm (ApplicationRealm) : 

    If the quickstart README specifies a realm, type it here. Otherwise, press enter to use the default `ApplicationRealm`. 
5. When prompted, enter the the Username and Passord. If the quickstart README specifies a Username nad Password, enter them here. Otherwise, use the default Username `quickstartUser` and Password `quickstartPassword`.
 
        Username : quickstartUser
        Password : quickstartPassword
6. At the next prompt, you will be asked "What roles do you want this user to belong to?". If the quickstart README specifies a role to use, enter that here. Otherwise, type the role: `guest`


<a id="postgresql"></a>
### Install and Configure the PostgreSQL Database

Some of the quickstarts require the PostgreSQL database. This section describes how to install and configure the database for use with these quickstarts.


#### Download and Install PostgreSQL 9.1.2

The following is a brief overview of how to install PostgreSQL. More detailed instructions for installing and starting PostgreSQL can be found on the internet.

_Note_: Although the database only needs to be installed once, to help partition each quickstart we recommend using a separate database per quickstart. Where you see QUICKSTART_DATABASENAME, you should replace that with the name provided in the particular quickstart's README

##### Linux Instructions

Use the following steps to install and configure PostgreSQL on Linux. You can download the PDF installation guide here: <http://yum.postgresql.org/files/PostgreSQL-RPM-Installation-PGDG.pdf>
  
1. Install PostgreSQL
    * The yum install instructions for PostgreSQL can be found here: <http://yum.postgresql.org/howtoyum.php/>
    * Download the repository RPM from here: <http://yum.postgresql.org/repopackages.php/>
    * To install PostgreSQL, in a command line type: 

            sudo rpm -ivh pgdg-fedora91-9.1-4.noarch.rpm
    * Edit your distributions package manager definitions to exclude PostgreSQL. See the "important note" on <http://yum.postgresql.org/howtoyum.php/> for details on how to exclude postgresql packages from the repository of the distribution.
    * Install _postgresql91_ and _postgres91-server_ by typing the following in a command line:

            sudo yum install postgresql91 postgresql91-server
2. Set a password for the _postgres_ user
    * In a command line, login as root and set the postgres password by typing the following commands: 

            su
            passwd postgres
    * Choose a password
3. Configure the test database
    * In a command line, login as the _postgres_ user, navigate to the postgres directory, and initialize the database by typing:

            su postgres
            cd /usr/pgsql-9.1/bin/
            ./initdb -D /var/lib/pgsql/9.1/data
    * Modify the `/var/lib/pgsql/9.1/data/pg_hba.conf` file to set the authentication scheme to password for tcp connections. Modify the line following the IPv4 local connections: change trust to to password. The line should look like this:
    
            host    all    all    127.0.0.1/32    password
    * Modify the `/var/lib/pgsql/9.1/data/postgresql.conf` file to allow prepared transactions and reduce the maximum number of connections

            max_prepared_transactions = 10
            max_connections = 10

4. Start the database server 
    * In the same command line, type the following:

            ./postgres -D /var/lib/pgsql/9.1/data
    * Note, this command does not release the command line. In the next step you need to open a new command line.
5.  Create a database for the quickstart (as noted above, replace QUICKSTART_DATABASENAME with the name provided in the particular quickstart)
    * Open a new command line and login again as the _postgres_ user, navigate to the postgres directory, and create the  database by typing the following:

            su postgres
            cd /usr/pgsql-9.1/bin/
            ./createdb QUICKSTART_DATABASENAME


##### Mac OS X Instructions

The following are the steps to install and start PostreSQL on Mac OS X. Note that this guide covers only 'One click installer' option.

1. Install PostgreSQL using Mac OS X One click installer: <http://www.postgresql.org/download/macosx/>
2. Allow prepared transactions:

        sudo su - postgres
    * Edit `/Library/PostgreSQL/9.1/data/postgresql.conf` to allow prepared transactions
      
            max_prepared_transactions = 10
3. Start the database server 

        cd /Library/PostgreSQL/9.1/bin
        ./pg_ctl -D ../data restart
4. Create a database for the quickstart (as noted above, replace QUICKSTART_DATABASENAME with the name provided in the particular quickstart)

        ./createdb QUICKSTART_DATABASENAME
5.  Verify that everything works. As the _postgres_ user using the password you specified in Step 1, type the following:

        cd /Library/PostgreSQL/9.1/bin
        ./psql -U postgres    
    At the prompt

        start transaction;
        select 1;
        prepare transaction 'foobar';
        commit prepared 'foobar';
    

##### Windows Instructions

Use the following steps to install and configure PostgreSQL on Windows:

1. Install PostgreSQL using the Windows installer: <http://www.postgresql.org/download/windows/>
2. Enable password authentication and configure PostgreSQL to allow prepared transactions
    * Modify the `C:\Program Files\PostgreSQL\9.1\data\pg_hba.conf` file to set the authentication scheme to password for tcp connections. Modify the line following the IPv4 local connections: change trust to to password. The line should look like this:

            host    all    all    127.0.0.1/32    password`
    * Modify the `C:\Program Files\PostgreSQL\9.1\data\postgresql.conf` file to allow prepared transactions and reduce the maximum number of connections:

            max_prepared_transactions = 10
            max_connections = 10
3.  Start the database server
    * Choose Start -> All Programs -> PostgreSQL 9.1\pgAdmin III
    * Server Groups -> Servers (1) -> PostreSQL 9.1 (localhost:5432)
    * Right click -> Stop Service
    * Right click -> Start Service
4.   Create a database for the quickstart (as noted above, replace QUICKSTART_DATABASENAME with the name provided in the particular quickstart)
    * Open a command line

            cd C:\Program Files\PostgreSQL\9.1\bin\
            createdb.exe -U postgres QUICKSTART_DATABASENAME


#### Create a Database User

1.  Make sure the PostgreSQL bin directory is in your PATH. 
    * Open a command line and change to the root directory
            psql
    * If you see an error that 'psql' is not a recognized command, you need to add the PostgreSQL bin directory to your PATH environment variable. 
2.  As the _postgres_ user, start the PostgreSQL interactive terminal by typing the following command:

        psql -U postgres
3.  Create the user sa with password sa and all privileges on the database by typing the following commands (as noted above, replace QUICKSTART_DATABASENAME with the name provided in the particular quickstart):

        create user sa with password 'sa';
        grant all privileges on database "QUICKSTART_DATABASENAME" to sa;
        \q
4.  Test the connection to the database using the TCP connection as user `'sa'`. This validates that the change to `pg_hba.conf` was made correctly: 

        psql -h 127.0.0.1 -U sa QUICKSTART_DATABASENAME

<a id="addpostgresqlmodule"></a>

#### Add the PostgreSQL Module to the JBoss server

1. Create the following directory structure: `JBOSS_HOME/modules/org/postgresql/main`
2. Download the JBDC driver from <http://jdbc.postgresql.org/download.html> and copy it into the directory you created in the previous step.
3. In the same directory, create a file named module.xml. Copy the following contents into the file:

        <?xml version="1.0" encoding="UTF-8"?>
        <module xmlns="urn:jboss:module:1.0" name="org.postgresql">
            <resources>
                <resource-root path="postgresql-9.1-901.jdbc4.jar"/>
            </resources>
            <dependencies>
                <module name="javax.api"/>
                <module name="javax.transaction.api"/>
            </dependencies>
        </module>

<a id="addpostgresqldriver"></a>

#### Add the Driver Configuration to the JBoss server
1.  Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
2.  Open the `JBOSS_HOME/standalone/configuration/standalone-full.xml` file in an editor and locate the subsystem `urn:jboss:domain:datasources:1.0`. 
3.  Add the following driver to the `<drivers>` section that subsystem. You may need to merge with other drivers in that section:

        <driver name="postgresql" module="org.postgresql">
            <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
        </driver>

#### Important Quickstart Testing Information

The installation of PostgreSQL is a one time procedure. However, unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" above for your operating system every time you reboot your machine.


<a id="byteman"></a>
### Install and Configure Byteman 

_Byteman_ is used by a few of the quickstarts to demonstrate distributed transaction processing and crash recovery.

#### What is It?

_Byteman_ is a tool which simplifies tracing and testing of Java programs. Byteman allows you to insert extra Java code into your application, either as it is loaded during JVM startup or after it has already started running. This code can be used to trace what the application is doing and to monitor and debug deployments to be sure it is operating correctly. You can also use _Byteman_ to inject faults or synchronization code when testing your application. A few of the quickstarts use _Byteman_ to halt an application server in the middle of a distributed transaction to demonstrate crash recovery.

<a id="byteman-install"></a>
#### Download and Configure Byteman

1. Download Byteman from <http://www.jboss.org/byteman/downloads/>
2. Extract the ZIP file to a directory of your choice.
3. By default, the Byteman download provides unrestricted permissions to _others_ which can cause a problem when running Ruby commands for the OpenShift quickstarts. To restrict the permissions to _others_, open a command line and type the followinge:

        cd byteman-download-2.0.0/
        chmod -R o-rwx byteman-download-2.0.0/

<a id="byteman-halt"></a>
#### Halt the Application Using Byteman

When instructed to use Byteman to halt the application, perform the following steps:
 
1. Find the appropriate configuration file for your operating system in the list below.

        For Linux: JBOSS_HOME/bin/standalone.conf 
        For Windows: JBOSS_HOME\bin\standalone.conf.bat

2. **Important**: Make a backup copy of this file before making any modifications.

3. The quickstart README should specify the text you need to append to the server configuration file.

4. Open the configuration file and append the text specified by the quickstart to the end of the file. Make sure to replace the file paths with the correct location of your quickstarts and the _Byteman_ download. 

5. The following is an example of of the configuration changes needed for the _jta-crash-rec_ quickstart: 

    For Linux, open the `JBOSS_HOME/bin/standalone.conf` file and append the following line:

        JAVA_OPTS="-javaagent:/PATH_TO_BYTEMAN_DOWNLOAD/lib/byteman.jar=script:/PATH_TO_QUICKSTARTS/jta-crash-rec/src/main/scripts/xa.btm ${JAVA_OPTS}" 
    For Windows, open the `JBOSS_HOME\bin\standalone.conf.bat` file and append the following line:

        SET "JAVA_OPTS=%JAVA_OPTS% -javaagent:C:PATH_TO_BYTEMAN_DOWNLOAD\lib\byteman.jar=script:C:\PATH_TO_QUICKSTARTS\jta-crash-rec\src\main\scripts\xa.btm %JAVA_OPTS%"

<a id="byteman-disable"></a>

#### Disable the Byteman Script
 
When you are done testing the quickstart, remember to restore the configuration file with the backup copy you made in step 2 above.


