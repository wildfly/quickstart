helloworld-ssl: Wildfly SSL configuration example
==================================================
Author: Giriraj Sharma

Level: Beginner

Technologies: SSL, Undertow, Wildfly

Summary: Basic example that demonstrates SSL configuration in wildlfy.

What is it?
-----------

This example demonstrates the configuration of *SSL* in *JBoss Enterprise Application Platform 6* or *WildFly*.

This quickstart shows how to configure wildfly to enable TLS/SSL configuration for the new wildfly web-subsystem Undertow.
Before you run this example, you must create certificates and configure the server to use SSL and https listener.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.


Generate a keystore and self-signed certificate 
-----------------------------------------------

1.  Open a command line and navigate to the JBoss server `configuration` directory:

        For Linux:   JBOSS_HOME/standalone/configuration
        For Windows: JBOSS_HOME\standalone\configuration
2. Create a certificate for your server using the following command:

        $>keytool -genkey -alias mycert -keyalg RSA -sigalg MD5withRSA -keystore my.keystore -storepass secret -keypass secret -validity 9999

        What is your first and last name?
           [Unknown]:  localhost
        What is the name of your organizational unit?
           [Unknown]:  wildfly
        What is the name of your organization?
           [Unknown]:  jboss
        What is the name of your City or Locality?
           [Unknown]:  Raleigh
        What is the name of your State or Province?
           [Unknown]:  Carolina
        What is the two-letter country code for this unit?
           [Unknown]:  US
        Is CN=localhost, OU=wildfly, O=jboss, L=Raleigh, ST=Carolina, C=US correct?
           [no]:  yes

   Make sure to put your desired "hostname" into the "first and last name" field, otherwise you might run into issues while permanently accepting this certificate as an exception in some browsers. Chrome doesn't have an issue with that though.

Configure The Additional WildFly Security Realm
-----------------------------------------------

The next step is to configure the new keystore as a server identity for ssl in the WildFly security-realms section of the standalone.xml (if you're using -ha or other versions, edit those). Make sure to backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`

        <management>
            <security-realms>
                <security-realm name="UndertowRealm">
                <server-identities>
                    <ssl>
                        <keystore path="my.keystore" relative-to="jboss.server.config.dir" keystore-password="secret" alias="mycert" key-password="secret"/>
                    </ssl>
                </server-identities>
            </security-realm>
            ...
            ...
        </management>

Configure Undertow Subsystem for SSL
------------------------------------

If you're running with the default-server, add the https-listener to the undertow subsystem:

        <subsystem xmlns="urn:jboss:domain:undertow:2.0">
            <server name="default-server">
                <https-listener name="https" socket-binding="https" security-realm="UndertowRealm"/>
                ...
                ...
            </server>
            ...
            ...
        </subsystem>

That's it, now you're ready to connect to the ssl port of your instance https://localhost:8443/. Note, that you get the privacy error as the server certificate is self signed. If you need to use a fully signed certificate you mostly get a PEM file from the Certificate Authority. In such a case, you need to import the PEM into the keystore.

Test the Server SSL Configuration
---------------------------------

To test the SSL configuration, access: <https://localhost:8443>

Start JBoss Enterprise Application Platform 6 or WildFly with the Web Profile
------------------------------------------------------------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the Wildfly Server as described above.
2. Open a command line and navigate to the root directory of one of the quickstart.
3. Type this command to build and deploy the archive:

        For EAP 6:     mvn clean package jboss-as:deploy
        For WildFly:   mvn -Pwildfly clean package wildfly:deploy

4. This will deploy `target/wildfly-helloworld-ssl.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <https://localhost:8443/wildfly-helloworld-ssl>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        For EAP 6:     mvn jboss-as:undeploy
        For WildFly:   mvn -Pwildfly wildfly:undeploy


Remove the SSL Configuration
----------------------------

1. If the server is running, stop the JBoss Enterprise Application Platform 6 or WildFly Server.
2. Replace the `WILDFLY_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools.


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
        
