JBoss AS Quickstarts
====================


Introduction
---------------------

The quickstarts included in this distribution were written to demonstrate Java EE 6 and a few additional technologies. They provide small, specific, working examples that can used as a reference for your own project.

These quickstarts run on both JBoss Enterprise Application Platform 6 and JBoss AS 7. If you want to run the quickstarts on JBoss Enterprise Application Platform 6, we recommend using the JBoss Enterprise Application Platform 6 zip file. This version uses the correct dependencies and ensures you test and compile against your runtime environment. 

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

*   [Available Quickstarts](#availableQuickstarts): List of the available quickstarts and details about each one.

*   [System Requirements](#systemrequirements): List of software required to run the quickstarts.

*   [Configure Maven](#mavenconfiguration): How to configure the Maven repository for use by the quickstarts.

*   [Build and Deploy the Quickstarts](#runningquickstarts): General instructions for building and deploying the quickstarts.

*   [Suggested Approach to the Quickstarts](#suggestedApproach): A suggested approach on how to work with the quickstarts.

*   [Optional Components](#optionalcomponents): How to install and configure optional components required by some of the quickstarts.

<a id="availableQuickstarts"/>

Available Quickstarts 
---------------------
The following is a list of the currently available quickstarts. The table lists each quickstart name, the technologies it demonstrates, gives a brief description of the quickstart, and the level of experience required to set it up. For more detailed information about a quickstart, click on the quickstart name.

Some quickstarts are designed to enhance or extend other quickstarts. These are noted in the **Prerequisites to This Quickstart** column. If a quickstart lists prerequisites, those must be installed before working with the quickstart.

Quickstarts with tutorials in the [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide "Getting Started Developing Applications Guide") are noted with two asterisks ( ** ) following the quickstart name. 

| **Quickstart Name** | **Demonstrated Technologies** | **Description** | **Experience Level Required** | **Prerequisites to This Quickstart** |
|:-----------|:-----------|:-----------|:-----------|:-----------|
| [bean-validation](bean-validation/README.html "bean-validation") | Bean Validation, JPA | Shows how to use Arquillian to test Bean Validation | Beginner | None |
| [bmt](bmt/README.html "bmt") | EJB, BMT | EJB that demonstrates bean-managed transactions (BMT) | Beginner | None |
| [cdi-injection](cdi-injection/README.html "cdi-injection") | CDI injection, Qualifiers, Servlet | Demonstrates the use of CDI 1.0 Injection and Qualifiers with JSF as the front-end client. | Beginner | None |
| [cmt] (cmt/README.html "cmt") | EJB, container-managed transaction (CMT) | EJB that demonstrates container-managed transactions (CMT) | Beginner | None |
| [ejb-in-ear](ejb-in-ear/README.html "ejb-in-ear") | EJB, JSF, JAR, and WAR deployed as an EAR | Packages an EJB JAR and WAR in an EAR | Beginner | None |
| [ejb-in-war](ejb-in-war/README.html "ejb-in-war") | EJB and JSF deployed as a WAR | Packages an EJB JAR in a WAR | Beginner | None |
| [ejb-remote](ejb-remote/README.html "ejb-remote") | Remote EJB | Shows how to access an EJB from a remote Java client program using JNDI | Beginner | None |
| [forge-from-scratch](forge-from-scratch/README.html "forge-from-scratch") | Forge | Demonstrates how to generate a fully Java EE compliant project using nothing but JBoss Forge | Beginner | None |
| [greeter](greeter/README.html "greeter") | CDI, JSF, JPA, EJB, JTA | Demonstrates the use of CDI 1.0, JPA 2.0, JTA 1.1, EJB 3.1 and JSF 2.0 | Beginner | None |
| [h2-console](h2-console/README.html "h2-console") | H2 Database Console | Shows how to use the H2 console with JBoss AS | Beginner | greeter |
| [helloworld**](helloworld/README.html "helloworld") | Basic CDI, Servlet | Basic example that can be used to verify that the server is configured and running correctly | Beginner | None |
| [helloworld-gwt](helloworld-gwt/README.html "helloworld-gwt") | GWT | Demonstrates the use of CDI 1.0 and JAX-RS with a GWT front-end client  | Beginner | None |
| [helloworld-html5](helloworld-html5/README.html "helloworld-html5") | Basic HTML5 |Demonstrates the use of CDI 1.0 and JAX-RS using the POH5 architecture and RESTful services on the backend | Beginner | None |
| [helloworld-jms](helloworld-jms/README.html "helloworld-jms") | JMS | Demonstrates the use of external JMS clients | Intermediate | None |
| [helloworld-jsf](helloworld-jsf/README.html "helloworld-jsf") | Basic CDI, JSF | Similar to the helloworld quickstart, but with a JSF front end | Beginner | None |
| [helloworld-mdb](helloworld-mdb/README.html "helloworld-mdb") | Basic JMS, message-driven bean (MDB) | Demonstrates the use of JMS 1.1 and EJB 3.1 Message-Driven Bean  | Intermediate | None |
| [helloworld-osgi**](helloworld-osgi/README.html "helloworld-osgi") | OSGi JAR | Shows how to create and deploy a simple OSGi Bundle | Beginner | None |
| [helloworld-rs](helloworld-rs/README.html "helloworld-rs") | CDI, JAX-RS | Demonstrates the use of CDI 1.0 and JAX-RS | Intermediate | None |
| [helloworld-singleton](helloworld-singleton/README.html "helloworld-singleton") | Singleton Session Bean | Demonstrates the use of an EJB 3.1 Singleton Session Bean | Beginner | None |
| [hibernate3](hibernate3/README.html "hibernate3") | Hibernate 3 | Performs the same functions as _hibernate4_ quickstart, but uses Hibernate version 3 for database access | Beginner | None |
| [hibernate4](hibernate4/README.html "hibernate4") | Hibernate 4 | Performs the same functions as _hibernate3_ quickstart, but uses Hibernate version 4 for database access | Beginner | None |
| [jax-rs-client](jax-rs-client/README.html "jax-rs-client") | JAX-RS | Demonstrates the use an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses CDI 1.0 and JAX-RS | Intermediate | helloworld-rs |
| [jts](jts/README.html "jts") | JTS | Uses Java Transaction Service (JTS) to coordinate distributed transactions | Intermediate | cmt |
| [kitchensink**](kitchensink/README.html "kitchensink") | CDI, JSF, JPA, EJB, JPA, JAX-RS, BV | An example that incorporates multiple technologies | Beginner | None |
| [kitchensink-ear](kitchensink-ear/README.html "kitchensink-ear")  | EAR | Based on kitchensink, but deployed as an EAR | Beginner | None |
| [kitchensink-html5-mobile](kitchensink-html5-mobile/README.html "kitchensink-html5-mobile") | HTML5 | Based on kitchensink, but uses HTML5, making it suitable for mobile and tablet computers | Beginner | None |
| [kitchensink-jsp](kitchensink-jsp/README.html "kitchensink-jsp") | JSP | Based on kitchensink, but uses a JSP for the user interface | Beginner | None |
| [log4j](log4j/README.html "log4j") | JBoss Modules | Demonstrates how to use modules to control class loading for 3rd party logging frameworks | Beginner | None |
| [logging](logging/README.html "logging") | JBoss Logging | Demonstrates the use of JBoss Logging | Beginner | None |
| [mail](mail/README.html "mail") | JavaMail | Demonstrates the use of JavaMail | Beginner | None |
| [numberguess**](numberguess/README.html "numberguess") | CDI, JSF | Demonstrates the use of CDI 1.0 and JSF 2.0  | Beginner | None |
| [payment-cdi-event](payment-cdi-event/README.html "payment-cdi-event") | CDI, Events | Demonstrates how to use CDI 1.0 Events  | Beginner | None |
| [servlet-async](servlet-async/README.html "servlet-async") | CDI, EJB, Servlet | Demonstrates CDI, plus asynchronous Servlets and EJBs | Intermediate | None |
| [servlet-filterlistener](servlet-filterlistener/README.html "servlet-filterlistener") | Servlet | Demonstrates Servlet filters and listeners | Intermediate | None |
| [wsat-simple](wsat-simple/README.html "wsat-simple") | WS-AT, Web service, JAX-WS | Deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a WAR archive  | Intermediate | None |
| [wsba-coordinator-completion-simple](wsba-coordinator-completion-simple/README.html "wsba-coordinator-completion-simple") | WS-BA, Web service, JAX-WS | Deployment of a WS-BA (WS-BusinessActivity) enabled JAX-WS Web service bundled in a WAR archive (Participant Completion protocol) | Intermediate | None |
| [wsba-participant-completion-simple](wsba-participant-completion-simple/README.html "wsba-participant-completion-simple") | WS-BA, Web service, JAX-WS | Deployment of a WS-BA (WS-BusinessActivity) enabled JAX-WS Web service bundled in a war archive (Coordinator Completion protocol) | Intermediate | None |



<a id="suggestedApproach"/>

Suggested Approach to the Quickstarts 
--------------------------------------

We suggest you approach the quickstarts as follows:

*   Regardless of your level of expertise, we suggest you start with the **helloworld** quickstart. It is the simplest example and is an easy way to prove your server is configured and started correctly.
*   If you are a beginner or new to JBoss, start with the quickstarts labeled **Beginner**, then try those marked as **Intermediate**. When you're comfortable with those, move on to the **Advanced**.
*   Some quickstarts are based on other quickstarts, but have expanded capabilities and functionality. If a prerequisite quickstart is listed, be sure to run through it before looking at the expanded version.



<a id="systemrequirements"/>

System Requirements 
-------------

To run these quickstarts with the provided build scripts, you need the following:

1.   Java 1.6, to run JBoss AS and Maven. You can choose from the following:
    *   OpenJDK
    *   Oracle Java SE
    *   Oracle JRockit

2.   Maven 3.0.0 or newer, to build and deploy the examples
    *   Follow the official Maven installation guide if you don't already have Maven 3 installed. 
    *   If you have Maven installed, you can check the version by typing the following in a command line:

                mvn --version 

3.   The JBoss AS 7 distribution zip or the JBoss Enterprise Application Platform 6 distribution zip
    *   For information on how to install and run JBoss, refer to the product documentation.

4.   You can also deploy the quickstarts from Eclipse using JBoss tools. For more information on how to set up Maven and the JBoss tools, refer to the 
     [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide "Getting Started Developing Applications Guide").



<a id="mavenconfiguration"/>

Configure Maven 
-------------

Maven configuration is dependent on whether you are running JBoss Enterprise Application Platform 6 or JBoss AS7.

<a id="as7mavenconfig"/>

### Configure Maven for JBoss AS 7

If you are using the JBoss AS 7 Quickstart distribution, the community artifacts are available in the Maven central repository so no additional configuration is needed.

<a id="eap6mavenconfig"/>

### Configure Maven for JBoss Enterprise Application Platform 6 

If you are using the JBoss Enterprise Application Platform 6 distribution, you need to download and configure the Maven repository.

1.   Download the JBoss Enterprise Application Platform 6 Maven repository distribution zip and unzip it into a directory of your choice.

2.   Modify the example-settings.xml file located in the root of your quickstarts folder. Replace all instances of `path/to/jboss-eap/repo` within `file:///path/to/jboss-eap/repo` with the fully qualified path to the Maven repository you unzipped in the previous step.

3.   When you run Maven commands, you need to append `-s _PathToQuickstarts_/example-settings.xml` to the command, for example:

                mvn jboss-as:deploy -s _PathToQuickstarts_/example-settings.xml

4.   You can configure the Maven user settings if you do not want to add the alternate path parameter each time you issue a Maven command.
    *   If you have an existing ~/.m2/settings.xml file, modify it with the configuration information from the example-settings.xml file.
    *   If there is no ~/.m2/settings.xml file, copy the example-settings.xml file to the ~/.m2 directory and rename it to settings.xml.



<a id="runningquickstarts"/>

Build and Deploy the Quickstarts 
--------------------------------------

To run the quickstarts, in most cases you do the following:

<a id="startjboss"/>

1.   Start the JBoss application server.  

    a.   Open a command line and navigate to the root of the JBoss directory.

    b.   For most of the quickstarts, you run the standalone script located in the JBoss server /bin/ folder with no arguments. The following shows the command line to start the Enterprise Application Platform 6 or JBoss AS 7 Server.

    | **Operating System** | **Command Line to Start the Server** |
    |:-----------|:-----------|
    | Linux | JBOSS_HOME/bin/standalone.sh |
    | Windows | JBOSS_HOME\bin\standalone.bat |

    c.  Some quickstarts use subsystems or services included only in the full profile. 
      *   By default, the JBoss Enterprise Application Platform 6 standalone configuration contains the full profile, so you start the server with the same command line as above. No argument is required. 
      *   However, in JBoss AS 7, the default configuration defines minimal subsystems and services, so you need to add a parameter to the command line when you start the server. 
      *   The following shows the command line to start JBoss AS 7 with the full profile.

        | **Operating System** | **Command Line to Start the Server with a Full Profile** |
        |:-----------|:-----------|
        | Linux | JBOSS_HOME/bin/standalone.sh  -c standalone-full.xml |
        | Windows | JBOSS_HOME\bin\standalone.bat -c standalone-full.xml |

    d.  Some quickstarts require custom configuration and require command line arguments. See each quickstart README for specific instructions.  
<a id="buildanddeploy"/>

2.   Build and deploy the quickstarts
    *   Open a command line and navigate to the root of the directory of the quickstart you want to run.
    *   Build the archive
        *  The command used to build the quickstart depends on the individual quickstart, the server version, and how you configured Maven. 
        *  If you are running JBoss AS 7, it uses community artifacts available in the Maven central repository, so command line arguments are not usually required. 
        *  If you are running JBoss Enterprise Application Platform 6 and did not configure the Maven user settings described in [Configure Maven for JBoss Enterprise Application Platform 6](#eap6mavenconfig) above, you need to specify command line arguments. 
        *  Although some of the quickstarts require special commands, for most of the quickstarts you do the following. 

        | **Server Version** | **Command to Build the Quickstart** |
        |:-----------|:-----------|
        | JBoss AS 7 | mvn clean package |
        | JBoss Enterprise Application Platform 6, Maven user settings configured | mvn clean package |
        | JBoss Enterprise Application Platform 6, Maven user settings NOT configured | mvn clean package -s _PathToQuickstarts_/example-settings.xml |
    *   Deploy the archive built in the previous step by typing the following in the command line:
                  `mvn jboss-as:deploy`
    *   Build and deploy the quickstart in one step
        *  The command you issue to build and deploy the quickstart also depend on the individual quickstart, the server version, and how you configured Maven. 
        *  As mentioned above, if you are running JBoss Enterprise Application Platform 6 and did not configure the Maven user settings described in [Configure Maven for JBoss Enterprise Application Platform 6](#eap6mavenconfig) above, you need to specify command line arguments. 
        *  Although some of the quickstarts require special commands, for most of the quickstarts you do the following.

        | **Server Version** | **Command to Build and Deploy the Quickstart** |
        |:-----------|:-----------|
        | JBoss AS 7 | mvn package jboss-as:deploy |
        | JBoss Enterprise Application Platform 6, Maven user settings configured | mvn package jboss-as:deploy |
        | JBoss Enterprise Application Platform 6, Maven user settings NOT configured | mvn package jboss-as:deploy -s _PathToQuickstarts_/example-settings.xml |
    *   The command to undeploy the quickstart is simply: 
                  `mvn jboss-as:undeploy`

3.   See the README file in each individual quickstart folder for specific details and information on how to run and access the example.
4.   You can also start JBoss AS and deploy the quickstarts using Eclipse. See the [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide "Getting Started Developing Applications Guide") for more information.



<a id="optionalcomponents"/>

Optional Components 
-------------------
The following components are needed for only a small subset of the quickstarts. Do not install or configure them unless the quickstart requires it.

*   [Install and Configure the PostgreSQL Database](#postgresql): Database used for distributed transaction quickstarts.

*   [Install and Configure Byteman](#byteman): Tool used to demonstrated crash recovery for distributed transaction quickstarts.

<a id="postgresql"/>

### Install and Configure the PostgreSQL Database

Some of the quickstarts require the PostgreSQL database. This section describes how to install and configure the database for use with the quickstarts.

#### Download and Install PostgreSQL 9.1.2

The following is a brief overview of how to install PostgreSQL. More detailed instructions for installing and starting PostgreSQL can be found on the internet. 

##### Linux Instructions

Use the following steps to install and configure PostgreSQL on Linux. You can download the PDF installation guide here: http://yum.postgresql.org/files/PostgreSQL-RPM-Installation-PGDG.pdf
  
1.  Install PostgreSQL
  * The yum install instructions for PostgreSQL can be found here: <http://yum.postgresql.org/howtoyum.php/>
  * Download the repository RPM from here: <http://yum.postgresql.org/repopackages.php/>
  * To install PostgreSQL, in a command line type: 

        * `sudo rpm -ivh pgdg-fedora91-9.1-4.noarch.rpm`
  * Edit your distributions package manager definitions to exclude PostgreSQL. See the "important note" on <http://yum.postgresql.org/howtoyum.php/> for details on how to exclude postgresql packages from the repository of the distribution.
  * Install _postgresql91_ and _postgres91-server_ by typing the following in a command line:

        * `sudo yum install postgresql91 postgresql91-server`
2. Set a password for the _postgres_ user
  * In a command line, login as root and set the postgres password by typing the following commands: 

        * `su`
        * `passwd postgres`
  * Choose a password
3. Configure the test database
  * In a command line, login as the _postgres_ user, navigate to the postgres directory, and initialize the database by typing:

        * `su postgres`
        * `cd /usr/pgsql-9.1/bin/`
        * `./initdb -D /var/lib/pgsql/9.1/data`
  * Modify the `/var/lib/pgsql/9.1/data/pg_hba.conf` file to set the authentication scheme to password for tcp connections. Modify the line following the IPv4 local connections: change trust to to password. The line should look like this:
    
                host    all    all    127.0.0.1/32    password
  * Modify the /var/lib/pgsql/9.1/data/postgresql.conf file to allow prepared transactions and reduce the maximum number of connections

                max_prepared_transactions = 10
                max_connections = 10
4. Start the database server 
   * In the same command line, type the following:

        * `./postgres -D /var/lib/pgsql/9.1/data`
   * Note, this command does not release the command line. In the next step you need to open a new command line.
5.  Create the _test1_ database
  * Open a new command line and login again as the _postgres_ user, navigate to the postgres directory, and create the _test1_ database by typing the following:

        * `su postgres`
        * `cd /usr/pgsql-9.1/bin/`
        * `./createdb test1`


##### Mac OS X Instructions

The following are the steps to install and start PostreSQL on Mac OS X. Note that this guide covers only 'One click installer' option.

1.  Install PostgreSQL using Mac OS X One click installer: <http://www.postgresql.org/download/macosx/>
2.  Allow prepared transactions:
    * `sudo su - postgres`
    * Edit `/Library/PostgreSQL/9.1/data/postgresql.conf` to allow prepared transactions
      
                max_prepared_transactions = 10
3. Start the database server 
    * `cd /Library/PostgreSQL/9.1/bin`
    * `./pg_ctl -D ../data restart`
4.  Create the _test1_ database using the password you specified in Step 1
    * `./createdb test1` 
4.  Verify that everything works. As the _postgres_ user using the password you specified in Step 1, type the following:
    * `cd /Library/PostgreSQL/9.1/bin`
    * `./psql -U postgres` 
    
                start transaction;
                select 1;
                prepare transaction 'foobar';
                commit prepared 'foobar';
    

##### Windows Instructions

Use the following steps to install and configure PostgreSQL on Windows:

1.  Install PostgreSQL using the Windows installer: <http://www.postgresql.org/download/windows/>
2.  Enable password authentication and configure PostgreSQL to allow prepared transactions
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
4.  Create the _test1_ database
    * Open a command line
    * cd `C:\Program Files\PostgreSQL\9.1\bin\`
    * `createdb.exe -U postgres test1`


#### Create a Database User

1.  Make sure the PostgreSQL bin directory is in your PATH. 
    * Open a command line and change to the root directory
    * Type:  `psql`
    * If you see an error that 'psql' is not a recognized command, you need to add the PostgreSQL bin directory to your PATH environment variable. 
2.  As the _postgres_ user, start the PostgreSQL interactive terminal by typing the following command:
    * `psql -U postgres`
3.  Create the user sa with password sa and all privileges on database _test1_ by typing the following commands: 
    * `create user sa with password 'sa';`
    * `grant all privileges on database test1 to sa;`
    * `\q` (to quit)
4.  Test the connection to the database using the TCP connection as user 'sa'. This validates that the change to `pg_hba.conf` was made correctly: 
    * `psql -h 127.0.0.1 -U sa test1`

#### Add the PostgreSQL Module to JBossAS

1. Create the following directory structure: JBOSS_HOME/modules/org/postgresql/main
1. Download the JBDC driver from <http://jdbc.postgresql.org/download.html /> and copy it into the directory you created in the previous step.
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

#### Add the Driver Configuration to JBossAS

1.  Backup the file: JBOSS_HOME/standalone/configuration/standalone-full.xml
2.  Open the JBOSS_HOME/standalone/configuration/standalone-full.xml file in an editor and locate the subsystem "urn:jboss:domain:datasources:1.0". 
3.  Add the following driver to the `<drivers>` section that subsystem. You may need to merge with other drivers in that section:

             <driver name="postgresql" module="org.postgresql">
                  <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
              </driver>

#### Important Quickstart Testing Information

The installation of PostgreSQL is a one time procedure. However, unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" above for your operating system every time you reboot your machine.


<a id="byteman"/>

### Install and Configure Byteman 

_Byteman_ is used by a few of the quickstarts to demonstrate distributed transaction processing and crash recovery.

#### What is It?

_Byteman_ is a tool which simplifies tracing and testing of Java programs. Byteman allows you to insert extra Java code into your application, either as it is loaded during JVM startup or even after it has already started running. This code can be used to trace what the application is doing and to monitor and debug deployments to be sure it is operating correctly. You can also use _Byteman_ to inject faults or synchronization code when testing your application. A few of the quickstarts use _Byteman_ to halt an application server in the middle of a distributed transaction to demonstrate crash recovery.

#### Download and Configure Byteman

1. Download Byteman from <http://www.jboss.org/byteman/downloads/>
2. Extract the zip file to a directory of your choice.
3. By default, the Byteman download provides unrestricted permissions to _others_ which can cause a problem when running Ruby commands for the OpenShift quickstarts. To restrict the permissions to _others_, open a command line and type the followinge:

                cd byteman-download-2.0.0/
                chmod -R o-rwx byteman-download-2.0.0/

#### How to Halt the Application Using Byteman

When instructed to use Byteman to halt the application, perform the following steps:
 
1. Find the appropriate configuration file for your operating system in the table below and make a backup copy. Open the file and append the text shown for your operating system to the end of the file. Replace the file paths with the correct location of your quickstarts and the _Byteman_ download.

    | **Operating System** | **Configuration File** |  **Text to Append to the Configuration File** |
    |:-----------|:-----------|:-----------|
    | Linux | JBOSS_HOME/bin/standalone.conf |JAVA_OPTS="-javaagent:/home/your-user-name/byteman-download-2.0.0/lib/byteman.jar=script:/home/user/quickstart/jta-crash-rec/src/main/scripts/xa.btm ${JAVA_OPTS}" |
    | Windows | JBOSS_HOME\bin\standalone.conf.bat | SET "JAVA_OPTS=%JAVA_OPTS% -javaagent:C:\byteman-download-2.0.0\lib\byteman.jar=script:C:\quickstart\jta-crash-rec\src\main\scripts\xa.btm %JAVA_OPTS%" |
2. When you are done testing the quickstart, remember to restore the configuration file with the backup copy.


