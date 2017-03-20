# helloworld-war-ssl: Securing a WAR application with mutual (two-way) SSL configuration over ${product.name}

Author: Giriraj Sharma  
Level: Intermediate  
Technologies: Mutual SSL, Undertow  
Summary: The `helloworld-war-ssl` quickstart demonstrates securing a WAR application using client mutual SSL authentication in ${product.name}.
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

This example demonstrates the configuration of *client mutual SSL authentication* in ${product.name.full} ${product.version} to secure a war application.

Mutual SSL provides the same security as SSL, with the addition of authentication and non-repudiation of the client authentication, using digital signatures. When mutual authentication is used the server would request the client to provide a certificate in addition to the server certificate issued to the client. Mutual authentication requires an extra round trip time for client certificate exchange. In addition the client must buy and maintain a digital certificate. We can secure our war application deployed over ${product.name} with mutual(two-way) client certificate authentication and provide access permissions or privileges to legitimate users.

This quickstart shows how to configure ${product.name} to enable TLS/SSL configuration for the new ${product.name} Undertow web-subsystem and enable mutual (two-way) SSL authentication for clients in order to secure a WAR application with restricted access.

Before you run this example, you must create certificates and configure the server to use SSL, HTTPS listener, certificate roles, and require client verification.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Set Up Certificate Authority, Server, and Client Keys

Certificate Authority, server and client keys can be generated either using traditional OpenSSL tool or using cross-platform Java Keytool.

### Set Up Certificate Authority, Server, and Client Keys Using OpenSSL

#### Set Up CA

First of all we need to set up the Certificate Authority (CA) to issue certificate.

1. First download OpenSSL and install it.
2. Set up the directory structure and files required by OpenSSL.
3. Create a directory ~\OpenSSL\workspace and place the openssl.conf file in the workplace.

        mkdir -p OpenSSL/workspace
        workspace cd  OpenSSL/workspace
        workspace mkdir Keys CSR Certificates
        workspace touch serial.txt database.txt

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

8. Import root CA certificate into a keystore

        keytool -import -alias server -keystore RootCA.keystore -rfc -file Certificates/RootCA.crt -keypass keypassword -storepass keypassword

9. Import root CA certificate into a truststore

        keytool -import -file Certificates/RootCA.crt -keystore RootCA.truststore -keypass keypassword -storepass keypassword

You can now see your CA’s certificate in the Certificates folder and it is ready to sign the certificates. The server/client certificate pair can be used when an application is trying to access a web service that is configured to authenticate the client application using the client ssl certificates. Follow the steps below to create server and client certificate using OpenSSL.

#### Create the server and client certificate

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

### Set Up CA, Server and Client Keys Using Java Keytool

#### Create the Server and Client Certificate

1.  Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   ${jboss.home.name}/standalone/configuration
        For Windows: ${jboss.home.name}\standalone\configuration
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

## Configure the Server for Mutual Client SSL Authentication

1.  Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   ${jboss.home.name}/standalone/configuration
        For Windows: ${jboss.home.name}\standalone\configuration

2.  Copy `RootCA.trustsore` and `server.keystore` (or `server.keystore` and `client.truststore` in case of java keytool) into the JBoss server `configuration` directory.

## Configure The Additional Security Realm

The next step is to configure the new keystore as a server identity for ssl in the ${product.name} security-realms section of the standalone.xml (if you're using -ha or other versions, edit those). Make sure to backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`. Configuring keystore would enable ssl and configuring truststore would restrict ssl access only to those client certificate's which are present in truststore.

 If the keys and certificates were generated using openSSL, the `keystore path` can be configured either using `RootCA.keystore` or `server.keystore`. For simplicity, you will use `RootCA.keystore`.

        <management>
            <security-realms>
                <security-realm name="UndertowRealm">
                <server-identities>
                    <ssl>
                        <keystore path="RootCA.keystore" relative-to="jboss.server.config.dir" keystore-password="keypassword" key-password="keypassword"/>
                    </ssl>
                </server-identities>
                <authentication>
                    <truststore path="RootCA.truststore" relative-to="jboss.server.config.dir" keystore-password="keypassword"/>
                    <local default-user="$local" skip-group-loading="true"/>
                    <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
                </authentication>
            </security-realm>
            ...
        </management>

If the keys and certificates were generated using Java Keytool:

        <management>
            <security-realms>
                <security-realm name="UndertowRealm">
                <server-identities>
                    <ssl>
                        <keystore path="server.keystore" relative-to="jboss.server.config.dir" keystore-password="keypassword" key-password="keypassword"/>
                    </ssl>
                </server-identities>
                <authentication>
                    <truststore path="client.truststore" relative-to="jboss.server.config.dir" keystore-password="keypassword"/>
                    <local default-user="$local" skip-group-loading="true"/>
                    <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
                </authentication>
            </security-realm>
            ...
        </management>

## Configure Undertow Subsystem for SSL

If you are running with the default-server, add the https-listener to the `undertow` subsystem:

        <subsystem xmlns="urn:jboss:domain:undertow:3.0">
            <server name="default-server">
                <https-listener name="https" socket-binding="https" security-realm="UndertowRealm" verify-client="REQUIRED"/>
                ...
                ...
            </server>
            ...
            ...
        </subsystem>

You are now ready to connect to the SSL port of the instance `https://localhost:8443/`. Note, that you get the privacy error as the server certificate is self-signed. If you need to use a fully signed certificate, you must get a PEM file from the Certificate Authority. In such a case, you need to import the PEM into the keystore and truststore.

## Test the Server SSL Configuration

To test the SSL configuration, access: `<https://localhost:8443>`

If it is configured correctly, you should be asked to trust the server certificate.

## Define a Security Ddomain

For simplicity, keep the `truststore` same for security realm as well as the  security domain. In other words, mutual client SSL will be successfully enabled and the WAR application will be secured and accessible by a client certificate only if it is present in the `truststore` (security-realm as well as security-domain) and having the DN same as mentioned in `app-roles.properties` file. Different client certificates present in security-domain truststore can be assigned different roles on the basis of `app-roles.properties` file.

In order to link the WAR application with ${product.name} configuration, define a security-domain using login module (certificateRoles login module)
in the `urn:jboss:domain:security:*` subsystem.

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

Make an `app-roles.properities` file in ${product.name} configuration directory with the format `CERTIFICATE_DN=ROLE_NAME`.

The DN of client certificate imported into the browser must be same as `CERTIFICATE_DN` in order to access the application with defined role.
A sample `app-roles.properties` file is like:

	CN\=client,\ OU\=Keycloak,\ O\=JBoss,\ ST\=UP,\ C\=IN=JBossAdmin
    admin=JBossAdmin

## Import the Certificate into Your Browser

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

## Start the Server with the Web Profile

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   bin/standalone.sh
        For Windows: bin\standalone.bat

## Build and Deploy the Quickstart

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of one of the quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.
5. In case mutual ssl is configured properly and war app is secured, you will be able to access the application only if the DN of client certificate i.e., `clientCert.p12` is same as the one mentioned in `app-roles.properties` file. It will otherwise result into a `HTTP Status 403` or forbidden error.


## Access the Application

The application will be running at the following URL: `<https://localhost:8443/${project.artifactId}>`.


## Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Remove the SSL Configuration

1. If the server is running, stop the ${product.name}.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources  
