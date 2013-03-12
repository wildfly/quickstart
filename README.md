JBoss AS Quickstarts
====================
Summary: The quickstarts demonstrate Java EE 6 and a few additional technologies from the JBoss stack. They provide small, specific, working examples that can be used as a reference for your own project.

Introduction
---------------------


These quickstarts run on both JBoss Enterprise Application Platform 6 and JBoss AS 7. If you want to run the quickstarts on JBoss Enterprise Application Platform 6, we recommend using the JBoss Enterprise Application Platform 6 ZIP file. This version uses the correct dependencies and ensures you test and compile against your runtime environment. 

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

* [Available Quickstarts](#availableQuickstarts): List of the available quickstarts and details about each one.

* [Suggested Approach to the Quickstarts](#suggestedApproach): A suggested approach on how to work with the quickstarts.

* [System Requirements](#systemrequirements): List of software required to run the quickstarts.

* [Configure Maven](#mavenconfiguration): How to configure the Maven repository for use by the quickstarts.

* [Run the Quickstarts](#runningquickstarts): General instructions for building, deploying, and running the quickstarts.

* [Run the Arquillian Tests](#arquilliantests): How to run the Arquillian tests provided by some of the quickstarts.

* [Optional Components](#optionalcomponents): How to install and configure optional components required by some of the quickstarts.


<a id="availableQuickstarts"></a>

Available Quickstarts 
---------------------

The following is a list of the currently available quickstarts. The table lists each quickstart name, the technologies it demonstrates, gives a brief description of the quickstart, and the level of experience required to set it up. For more detailed information about a quickstart, click on the quickstart name.

Some quickstarts are designed to enhance or extend other quickstarts. These are noted in the **Prerequisites** column. If a quickstart lists prerequisites, those must be installed or deployed before working with the quickstart.

Quickstarts with tutorials in the [Get Started Developing Applications](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/guide/Introduction/ "Get Started Developing Applications") are noted with two asterisks ( ** ) following the quickstart name. 

[TOC-quickstart]

<a id="suggestedApproach"></a>
Suggested Approach to the Quickstarts 
--------------------------------------

We suggest you approach the quickstarts as follows:

* Regardless of your level of expertise, we suggest you start with the **helloworld** quickstart. It is the simplest example and is an easy way to prove your server is configured and started correctly.
* If you are a beginner or new to JBoss, start with the quickstarts labeled **Beginner**, then try those marked as **Intermediate**. When you are comfortable with those, move on to the **Advanced** quickstarts.
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

Maven configuration is dependent on whether you are using the JBoss Enterprise Application Platform 6 product version of the quickstarts or the JBoss AS7 community version of the quickstarts.

<a id="eap6mavenconfig"></a>
### Configure Maven for JBoss Enterprise Application Platform 6 

If you are using the JBoss Enterprise Application Platform 6 distribution, you need to download and configure the Maven repository.

1. Download the JBoss Enterprise Application Platform 6 Maven repository distribution ZIP and unzip it into a directory of your choice.

2. Modify the `example-settings.xml` file located in the root of your quickstarts folder. 
    * Replace all instances of `path/to/jboss-eap/repo` within `file:///path/to/jboss-eap/repo` with the fully qualified path to the Maven repository you unzipped in the previous step.
    * Be sure to use 3 forward slashes after `file:`: 2 for the protocol and 1 for the fully qualified path. For example:

            file:///home/username/Quickstarts/jboss-eap-6.0-quickstarts
3. Configure the Maven user settings. 
    * Look for the `settings.xml` file in the `${user.home}/.m2/` directory. For example:

            For Linux or Mac:   ~/.m2/settings.xml
            For Windows: \Documents and Settings\USER_NAME\.m2\settings.xml or \Users\USER_NAME\.m2\settings.xml
    * If you have an existing `settings.xml` file, modify it with the configuration information from the `example-settings.xml` file.
    * If there is no `settings.xml` file, copy the modified `example-settings.xml` file to the `m2` directory for your operating system and rename it to `settings.xml`.


<a id="as7mavenconfig"></a>
### Configure Maven for JBoss AS 7

If you are using the JBoss AS 7 Quickstart distribution, the community artifacts are available in the Maven central repository so no additional configuration is needed.

### Maven Profiles

Profiles are used by Maven to customize the build environment. The `pom.xml` in the root of the quickstart directory defines the following profiles:

* The `default` profile defines the list of modules or quickstarts that require nothing but JBoss Enterprise Application Platform or JBoss AS .
* The `requires-postgres` profile lists the quickstarts that require PostgreSQL to be running when the quickstart is deployed.
* The `complex-dependency` profile lists quickstarts that require manual configuration that can not be automated.
* The `requires-full` profile lists quickstarts the require you start the server using the full profile.
* The `requires-xts` profile lists quickstarts the require you start the server using the xts profile.
* The `non-maven` profile lists quickstarts that do not require Maven, for example, quickstarts that depend on deployment of other quickstarts or those that use other Frameworks such as Forge.


<a id="runningquickstarts"></a>
Run the Quickstarts 
-------------------

The root folder of each individual quickstart contains a README file with specific details on how to build and run the example. In most cases you do the following:

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

See the README file in each individual quickstart folder for specific details and information on how to run and access the example.

#### Build the Quickstart Archive

In some cases, you may want to build the application to test for compile errors or view the contents of the archive. 

1. Open a command line and navigate to the root directory of the quickstart you want to build.
2. Use this command if you only want to build the archive, but not deploy it:

            mvn clean package

#### Build and Deploy the Quickstart Archive

1. Make sure you [start the JBoss Server](#startjboss) as described in the README.
2. Open a command line and navigate to the root directory of the quickstart you want to run.
3. Use this command to build and deploy the archive:

            mvn clean package jboss-as:deploy

#### Undeploy an Archive

The command to undeploy the quickstart is simply: 

        mvn jboss-as:undeploy
 
<a id="verifyall"></a>
### Verify the Quickstarts Build with One Command
-------------------------

You can verify the quickstarts build using one command. However, quickstarts that have complex dependencies must be skipped. For example, the _jax-rs-client_ quickstart is a RESTEasy client that depends on the deployment of the _helloworld-rs_ quickstart. As noted above, the root `pom.xml` file defines a `complex-dependencies` profile to exclude these quickstarts from the root build process. 

To build the quickstarts:

1. Do not start the server.
2. Open a command line and navigate to the root directory of the quickstarts.
3. Use this command to build the quickstarts that do not have complex dependencies:

            mvn clean install '-Pdefault,!complex-dependencies'


<a id="undeployall"></a>
### Undeploy the Deployed Quickstarts with One Command
-------------------------

To undeploy the quickstarts from the root of the quickstart folder, you must pass the argument `-fae` (fail at end) on the command line. This allows the command to continue past quickstarts that fail due to complex dependencies and quickstarts that only have Arquillian tests and do not deploy archives to the server.

You can undeploy quickstarts using the following procedure:

1. Start the server.
2. Open a command line and navigate to the root directory of the quickstarts.
3. Use this command to undeploy any deployed quickstarts:

            mvn jboss-as:undeploy -fae

To undeploy any quickstarts that fail due to complex dependencies, follow the undeploy procedure described in the quickstart's README file.


<a id="arquilliantests"></a>
### Run the Arquillian Tests 
-------------------------

Some of the quickstarts provide Arquillian tests. By default, these tests are configured to be skipped, as Arquillian tests require the use of a container. 

You can run these tests using either a remote or managed container. The quickstart README should tell you what you should expect to see in the console output and server log when you run the test.

<a id="testremote"></a>

1. Test the quickstart on a Remote Server
    * A remote container requires you start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server before running the test. [Start the JBoss Server](#startjboss) as described in the quickstart README file.
    * Run the test goal with the following profile activated:

            mvn clean test -Parq-jbossas-remote 
<a id="testmanaged"></a>

2. Test the quickstart on Managed Server

    _Note: This test requires that your server is not running. Arquillian will start the container for you, however, you must first let it know where to find the remote JBoss container._
    * Open the test/resources/arquillian.xml file located in the quickstart directory. 
    * Find the configuration for the remote JBoss container. It should look like this:

            <!-- Example configuration for a remote JBoss Enterprise Application Platform 6 or AS 7 instance -->
            <container qualifier="jboss" default="true">
                <!-- By default, arquillian will use the JBOSS_HOME environment variable.  Alternatively, the configuration below can be uncommented. -->
                <!--<configuration> -->
                <!--<property name="jbossHome">/path/to/jboss/as</property> -->
                <!--</configuration> -->
            </container>
    * Remove the comments from the `<configuration>` elements.

            <!-- Example configuration for a remote JBoss Enterprise Application Platform 6 or AS 7 instance -->
            <container qualifier="jboss" default="true">
                <!-- By default, arquillian will use the JBOSS_HOME environment variable.  Alternatively, the configuration below can be uncommented. -->
                <configuration>
                    <property name="jbossHome">/path/to/jboss/as</property>
                </configuration>
            </container>
    * Find the "jbossHome" property and replace the "/path/to/jboss/as" value with the actual path to your JBoss Enterprise Application Platform 6 or JBoss AS 7 server.
    * Run the test goal with the following profile activated:

            mvn clean test -Parq-jbossas-managed

<a id="useeclipse"></a>
Use JBoss Developer Studio or Eclipse to Run the Quickstarts
-------------------------------------
You can also deploy the quickstarts from Eclipse using JBoss tools. For more information on how to set up Maven and the JBoss tools, refer to the [JBoss Enterprise Application Platform 6 Development Guide](https://access.redhat.com/knowledge/docs/JBoss_Enterprise_Application_Platform/) or [Get Started Developing Applications](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/guide/Introduction/ "Get Started Developing Applications").


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
5. When prompted, enter the the Username and Passord. If the quickstart README specifies a Username and Password, enter them here. Otherwise, use the default Username `quickstartUser` and Password `quickstartPwd1!`.
 
        Username : quickstartUser
        Password : quickstartPwd1!
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

The following are the steps to install and start PostgreSQL on Mac OS X. Note that this guide covers only 'One click installer' option.

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
    * Server Groups -> Servers (1) -> PostgreSQL 9.1 (localhost:5432)
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

You can configure the driver using the JBoss CLI or by manually editing the configuration file.

##### Configure the Driver Using the JBoss CLI

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        [standalone@localhost:9999 /] /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)


##### Configure the Driver Manually

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

_NOTE_: The Byteman scripts only work in JTA mode. They do not work in JTS mode. If you have configured the server for a JTS quickstart, you must follow the instructions to [Remove the JTS Configuration from the JBoss server](jts/README.md#remove-jts-configuration) before making the following changes. Otherwise Byteman will not halt the server. 

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


