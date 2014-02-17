servlet-security:  Using Java EE Declarative Security to Control Access to Servlet 3
====================
Author: Sherif F. Makary, Pedro Igor
Level: Intermediate
Technologies: Servlet, Security
Summary: Demonstrates how to use Java EE declarative security to control access to Servlet 3
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This example demonstrates the use of Java EE declarative security to control access to Servlets and Security in JBoss WildFly.

When you deploy this example, two users are automatically created for you: user `quickstartUser` with password `quickstartPwd1!` and user `guest` with password `guest`. This data is located in the `src/main/resources/import.sql` file. 

This quickstart takes the following steps to implement Servlet security:

1. Define a security domain in the `standalone.xml` configuration file using the Database JAAS LoginModule.
2. Add an application user with access rights to the application

        User Name: quickstartUser
        Password: quickstartPwd1!
        Role: quickstarts
3. Add another user with no access rights to the application.

        User Name: guest
        Password: guestPwd1!
        Role: notauthorized
4. Add a security domain reference to `WEB-INF/jboss-web.xml`.
5. Add a security constraint to the `WEB-INF/web.xml` .
6. Add a security annotation to the EJB declaration.

Please note the allowed user role `quickstarts` in the annotation -`@RolesAllowed`- is the same as the user role defined in step 2.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add the Application Users
----------------

Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| quickstarts |
| guest | ApplicationRealm | guestPwd1! | notauthorized |

The first application user has access rights to the application. The second application user is not authorized to access the application.

For an example of how to use the add-user utility, see instructions in the root README file located here: [Add User](../README.md#addapplicationuser).


Define a Security Domain Using the Database JAAS Login Module
---------------

This quickstart authenticates users using a simple database setup. The datasource configuration is located in the `/src/main/webapp/WEB-INF/servlet-security-quickstart-ds.xml` file. You can configure the security domain by running the  `configure-security-domain.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss WildFly Server.
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Configure the Security Domain by Running the JBoss CLI Script

1. Start the JBoss WildFly Server by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh 
        For Windows:  JBOSS_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
This script adds the `servlet-security-quickstart` domain to the `security` subsystem in the server configuration and configures authentication access. You should see the following result when you run the script:

        #1 /subsystem=security/security-domain=servlet-security-quickstart:add(cache-type=default)
        #2 /subsystem=security/security-domain=servlet-security-quickstart/authentication=classic:add(login-modules=[{"code"=>"Database", "flag"=>"required", "module-options"=>[("dsJndiName"=>"java:jboss/datasources/ServletSecurityDS"),("principalsQuery"=>"SELECT PASSWORD FROM USERS WHERE USERNAME = ?"), ("rolesQuery"=>"SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?")]}])
        The batch executed successfully.
        {"outcome" => "success"}

### Configure the Security Domain Using the JBoss CLI Interactively

1. Start the JBoss WildFly Server with the web profile by typing the following:

        For Linux:  JBOSS_HOME/bin/standalone.sh 
        For Windows:  JBOSS_HOME\bin\standalone.bat 
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type each of the following commands. After each one, you should see a response with the first line `"outcome" => "success"`.

        /subsystem=security/security-domain=servlet-security-quickstart/:add(cache-type=default)

        /subsystem=security/security-domain=servlet-security-quickstart/authentication=classic:add(login-modules=[{"code"=>"Database", "flag"=>"required", "module-options"=>[("dsJndiName"=>"java:jboss/datasources/ServletSecurityDS"),("principalsQuery"=>"SELECT PASSWORD FROM USERS WHERE USERNAME = ?"), ("rolesQuery"=>"SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?")]}])

        /:reload


### Configure the Security Domain by Manually Editing the Server Configuration File

1.  If it is running, stop the JBoss WildFly Server.
2.  Backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`
3.  Open the `JBOSS_HOME/standalone/configuration/standalone.xml` file in an editor and locate the subsystem `urn:jboss:domain:security`. 
4.  Add the following XML to the :

      	<security-domain name="servlet-security-quickstart" cache-type="default">
    	      <authentication>
          	    <login-module code="Database" flag="required">
            	      <module-option name="dsJndiName" value="java:jboss/datasources/ServletSecurityDS"/>
                    <module-option name="principalsQuery" value="SELECT PASSWORD FROM USERS WHERE USERNAME = ?"/>
                    <module-option name="rolesQuery" value="SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?"/>
                </login-module>
            </authentication>
        </security-domain>
    
Please note that the security domain name `servlet-security-quickstart` must match the one defined in the `/src/main/webapp/WEB-INF/jboss-web.xml` file.


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


<a id="buildanddeploy"></a>
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-servlet-security.war` to the running instance of the server.

<a id="accesstheapp"></a>
Access the Application 
---------------------

The application will be running at the following URL <http://localhost:8080/wildfly-servlet-security/>.

When you access the application, you should get a browser login challenge. 

Log in using the username `quickstartUser` and password `quickstartPwd1!`. The browser will display the following security info:

    Successfully called Secured Servlet

    Principal : quickstartUser
    Remote User : quickstartUser
    Authentication Type : BASIC

Now close the browser. Open a new browser and log in with username `guest` and password `guestPwd1!`. The browser will display the following error:

        HTTP Status 403 - Access to the requested resource has been denied

        type Status report
        message Access to the requested resource has been denied
        description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Remove the Security Domain Configuration
----------------------------

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the JBoss WildFly Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-security-domain.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 /subsystem=security/security-domain=quickstart-domain:remove
        The batch executed successfully.
        {"outcome" => "success"}


### Remove the Security Domain Configuration Manually
1. If it is running, stop the JBoss WildFly Server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.




Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc
