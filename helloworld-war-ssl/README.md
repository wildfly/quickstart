helloworld-war-ssl: Securing war application with mutual(two-way) SSL configuration over wildfly
=================================================================================================
Author: Giriraj Sharma

Level: Intermediate

Technologies: Mutual SSL, Undertow, Wildfly

Summary: Basic example that demonstrates securing war application via client mutual SSL authentication in wildlfy.

What is it?
-----------

This example demonstrates the configuration of *client mutual SSL authentication* in *JBoss Enterprise Application Platform 6* or *WildFly* to secure a war application.

Mutual SSL provides the same security as SSL, with the addition of authentication and non-repudiation of the client authentication, using digital signatures. When mutual authentication is used the server would request the client to provide a certificate in addition to the server certificate issued to the client. Mutual authentication requires an extra round trip time for client certificate exchange. In addition the client must buy and maintain a digital certificate. We can secure our war application deployed over wildfly with mutual(two-way) client certificate authentication and provide access permissions or privileges to legitimate users.

This quickstart shows how to configure wildfly to enable TLS/SSL configuration for the new wildfly web-subsystem Undertow and enable mutual (two-way) SSL authentication for clients in order to secure a war application with restricted access.
Before we run this example, we must create certificates and configure the server to use SSL, https listener, certificate roles and require client verification.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](http://www.jboss.org/jdf/quickstarts/jboss-as-quickstart/#configure_maven) before testing the quickstarts.

## Setup CA, server and client keys using openSSL

Certificate Authority, server and client keys can be generated either viua traditional openSSL tool or via cross-paltform java keytool.

### Setup CA

First of all we need to set up the Certificate Authority (CA) to issue certificate.

    1. First download OpenSSL and install it.
    2. Set up the directory structure and files required by OpenSSL.
    3. Create a directory ~\OpenSSL\workspace and place the openssl.conf file in the workplace.

    ➜ mkdir -p OpenSSL/workspace
    ➜ workspace cd  OpenSSL/workspace
    ➜ workspace mkdir Keys CSR Certificates
    ➜ workspace touch serial.txt database.txt

4. Generate a key for your Root CA. Execute the below OpenSSL command at workspace where you have openssl configuration file.
    
        openssl genrsa -des3 -out  Keys/RootCA.key 2048

5. This will ask for passphrase for the key, please provide the passphrase and remember it. This will be used later.

6. The next step is to create a self-signed certificate for our CA, this certificate will be used to sign and issue other certificates.
        
        openssl req -config openssl.conf -new -x509 -days 360 -key Keys/RootCA.key -out Certificates/RootCA.crt

7. You will be asked to provide the following information:-

        Country Name (2 letter code)  :US
        State or Province Name (full name) :Carolina
        Locality Name (eg, city) :Raleigh
        Organization Name (eg, company) :Sample Inc
        Organizational Unit Name (eg, section) :Web
        Common Name (eg, your websites domain name) :sample.com
        Email Address :sample@sample.com

8. Export root CA certificate into a keystore
  
        keytool -export -alias server -keystore RootCA.keystore -rfc -file Certificates/RootCA.crt -keypass keypassword -storepass keypassword

9. Export root CA certificate into a truststore
        
        keytool -import -file Certificates/RootCA.crt -keystore RootCA.truststore -keypass keypassword -storepass keypassword

Now we can see our CA’s certificate in the Certificates folder and is ready to sign the certificates.
The server/client certificate pair can be used when an application is trying to access a web service which is configured to authenticate the client application using the client ssl certificates. We can follow steps below to create server and client certificate using OpenSSL

### Create the server and client certificate

1. Create private key for the server.
        
        openssl genrsa -des3 -out Keys/server.key 2048

2. Create CSR for the server.

        openssl req -config openssl.cnf -new -key Keys/server.key -out CSR/server.csr

3. Create server certificate.

        openssl ca -config openssl.cnf -days 360 -in CSR/server.csr -out Certificates/server.crt -keyfile Keys/RootCA.key -cert Certificates/RootCA.crt -policy policy_anything

4. Export server certificate into a keystore
  
        keytool -export -alias server -keystore server.keystore -rfc -file Certificates/server.crt -keypass keypassword -storepass keypassword

5. Create private key for the client.

        openssl genrsa -des3 -out Keys/client.key 2048

6. Create CSR for the client.
        
        openssl req -config openssl.cnf -new -key Keys/client.key -out CSR/client.csr

7. Create client certificate.

        openssl ca -config openssl.cnf -days 360 -in CSR/client.csr -out Certificates/client.crt -keyfile Keys/RootCA.key -cert Certificates/RootCA.crt

8. Finally export the client certificate to pkcs format.

        openssl pkcs12 -export -in Certificates/client.crt -inkey Keys/client.key -certfile Certificates/RootCA.crt -out Certificates/clientCert.p12

## Setup CA, server and client keys using Java Keytool

### Create the server and client certificate

1.  Open a command line and navigate to the JBoss server `configuration` directory:

        For Linux:   JBOSS_HOME/standalone/configuration
        For Windows: JBOSS_HOME\standalone\configuration
2. Create a certificate for your server using the following command:

        keytool -genkey -keyalg RSA -keystore server.keystore -storepass keypassword -validity 365

   You'll be prompted for some additional information, such as your name, organizational unit, and location. Enter any values you prefer.
3. Create the client certificate, which is used to authenticate against the server when accessing a resource through SSL.

         keytool -genkey -keystore client.keystore -storepass keypassword -validity 365 -keyalg RSA -keysize 2048 -storetype pkcs12
4. Export the client certificate and create a truststore by importing this certificate:

        keytool -exportcert -keystore client.keystore  -storetype pkcs12 -storepass keypassword -keypass keypassword -file client.crt
        keytool -import -file client.crt -keystore client.truststore
5. Export client certificate to pkcs12 format

        keytool -importkeystore -srckeystore client.keystore -destkeystore clientCert.p12 -srcstoretype PKCS12 -deststoretype PKCS12 -deststorepass keypassword

6. The certificates and keystores are now properly configured.

Configure Wildlfy for mutual client SSL authentication
------------------------------------------------------

1.  Open a command line and navigate to the JBoss server `configuration` directory:

        For Linux:   JBOSS_HOME/standalone/configuration
        For Windows: JBOSS_HOME\standalone\configuration
        
2.  Copy `RootCA.trustsore` and `server.keystore` (or `server.keystore` and `client.truststore` in case of java keytool) into the JBoss server `configuration` directory.

### Configure The Additional WildFly Security Realm

The next step is to configure the new keystore as a server identity for ssl in the WildFly security-realms section of the standalone.xml (if you're using -ha or other versions, edit those). Make sure to backup the file: `JBOSS_HOME/standalone/configuration/standalone.xml`. Configuring keystore would enable ssl and configuring truststore would restrict ssl access only to those client certificate's which are present in truststore.
 
 In case keys and certificates have been generated using openSSL
 
 `keystore path` can be configured either via `RootCA.keystore` or `server.keystore`. For simplicity, we will use `RootCA.keystore`.
 
        <management>
            <security-realms>
                <security-realm name="UndertowRealm">
                <server-identities>
                    <ssl>
                        <keystore path="RootCA.keystore" relative-to="jboss.server.config.dir" keystore-password="keypassword" key-password="keypassword"/>
                    </ssl>
                </server-identities>
            </security-realm>
            <authentication>
                    <truststore path="RootCA.truststore" relative-to="jboss.server.config.dir" keystore-password="keypassword"/>
                    <local default-user="$local" skip-group-loading="true"/>
                    <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
            </authentication>
            ...
        </management>
        
else In case keys and certificates have been generated using java keytool

        <management>
            <security-realms>
                <security-realm name="UndertowRealm">
                <server-identities>
                    <ssl>
                        <keystore path="server.keystore" relative-to="jboss.server.config.dir" keystore-password="keypassword" key-password="keypassword"/>
                    </ssl>
                </server-identities>
            </security-realm>
            <authentication>
                    <truststore path="client.truststore" relative-to="jboss.server.config.dir" keystore-password="keypassword"/>
                    <local default-user="$local" skip-group-loading="true"/>
                    <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
            </authentication>
            ...
        </management>

###Configure Undertow Subsystem for SSL

If you're running with the default-server, add the https-listener to the undertow subsystem:

        <subsystem xmlns="urn:jboss:domain:undertow:2.0">
            <server name="default-server">
                <https-listener name="https" socket-binding="https" security-realm="UndertowRealm" verify-client="REQUIRED"/>
                ...
                ...
            </server>
            ...
            ...
        </subsystem>

That's it, now we are ready to connect to the ssl port of our instance `https://localhost:8443/`. Note, that we get the privacy error as the server certificate is self signed. If we need to use a fully signed certificate, we mostly get a PEM file from the Certificate Authority. In such a case, we need to import the PEM into the keystore and truststore.

Test the Server SSL Configuration
---------------------------------

To test the SSL configuration, access: `<https://localhost:8443>`

If it is configured correctly, you should be asked to trust the server certificate.

Define a security-domain
------------------------

`For simplicity, let's keep the truststore same for security realm as well as security domain. In other words, mutual client ssl will be successfully enabled and war app will be secured and accessible by a client certificate only if it is present in truststore (security-realm as well as security-domain) and having the DN same as mentioned in 'app-roles.properties' file.` Different client certificates present in security-domain truststore can be assigned different roles on the basis of `app-roles.properties` file.

In order to link the war application with wildfly configuration, define a security-domain using login module (certificateRoles login module)
in wildfly subsystem `urn:jboss:domain:security:*` .

 In case keys and certificates have been generated using openSSL, configure keystore with `RootCA.keystore` and truststore with `RootCA.truststore` else in case keys and certificates have been generated using java keytool configure keystore with `server.keystore` and truststore with `client.truststore` .

        <subsystem xmlns="urn:jboss:domain:security:1.2">
            <security-domains>
                <security-domain name="client_cert_domain" cache-type="default">
                    <authentication>
                        <login-module code="CertificateRoles" flag="required">
                            <module-option name="verifier" value="org.jboss.security.auth.certs.AnyCertVerifier"/>
                            <module-option name="securityDomain" value="client_cert_domain"/>
                            <module-option name="rolesProperties" value="file:${jboss.server.config.dir}/app-roles.properties"/>
                        </login-module>
                    </authentication>
                    <jsse keystore-password="keypassword" keystore-url="file:${jboss.server.config.dir}/RootCA.keystore" truststore-password="keypassword" truststore-url="file:${jboss.server.config.dir}/RootCA.truststore" client-auth="true"/>
                </security-domain>
                ...
                ...
        </subsystem>

Make an app-roles.properities file in wildfly configuration directory with the format
`CERTIFICATE_DN=ROLE_NAME`

The DN of client certificate imported into the browser must be same as `CERTIFICATE_DN` in order to access the application with defined role.
A sample `app-roles.properties` file is like:

	CN\=client,\ OU\=Keycloak,\ O\=JBoss,\ ST\=UP,\ C\=IN=JBossAdmin
    admin=JBossAdmin

Import the Certificate into Your Browser
---------------------------------------

Before you access the application, you must import the *clientCert.p12*, which holds the client certificate, into your browser.

#### Import the Certificate into Google Chrome

1. Click the Chrome menu icon (3 horizontal bars) in the upper right on the browser toolbar and choose 'Settings'. This takes you to <chrome://settings/>.
2. At the bottom of the page, click on the 'Show advanced settings...' link.
3. Find the section 'HTTPS/SSL' and click on the 'Manage certificates...' button.
4. In the 'Certificate manager' dialog box, choose the 'Your Certificates' tab and click the 'Import' button.
5. Select the `clientCert.p12` file. You will be prompted to enter the  password: `keypassword`.
6. The certificate is now installed in the Google Chrome browser.

#### Import the Certificate into Mozilla Firefox

1. Click the 'Edit' menu item on the browser menu and choose 'Preferences'.
2. A new window will open. Select the 'Advanced' icon and after that the 'Certificates' tab.
3. On the 'Certificates' tab, mark the option 'Ask me every time' and click the 'View Certificates' button.
4. A new window will open. Select the 'Your Certificates' tab and click the 'Import' button.
5. Select the `clientCert.p12` file. You will be prompted to enter the  password: `keypassword`.
6. The certificate is now installed in the Mozilla Firefox browser.

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

4. This will deploy `target/wildfly-helloworld-war-ssl.war` to the running instance of the server.
5. In case mutual ssl is configured properly and war app is secured, you will be able to access the application only if the DN of client certificate i.e., `clientCert.p12` is same as the one mentioned in `app-roles.properties` file. It will otherwise result into a `HTTP Status 403` or forbidden error.


Access the application 
---------------------

The application will be running at the following URL: `<https://localhost:8443/wildfly-helloworld-war-ssl>`. 


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
        
