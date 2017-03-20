# helloworld-ssl: Wildfly SSL Configuration Example

Author: Giriraj Sharma  
Level: Beginner  
Technologies: SSL, Undertow  
Summary: The `helloworld-ssl` quickstart is a basic example that demonstrates SSL configuration in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

This `helloworld-ssl` quickstart demonstrates the configuration of *SSL* in *${product.name.full}*.

This quickstart shows how to configure ${product.name} to enable TLS/SSL configuration for the new `Undertow` web subsystem.

Before you run this example, you must create certificates and configure the server to use SSL and and `https` listener.


## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.


## Generate a Keystore and Self-signed Certificate

1.  Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   standalone/configuration
        For Windows: standalone\configuration
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

    Make sure to put your desired "hostname" into the "first and last name" field, otherwise you might run into issues while permanently accepting this certificate as an exception in some browsers. Chrome does not have an issue with that though.

## Configure The Additional Server Security Realm

<!-- Add CLI scripts and instructions! -->

The next step is to configure the new keystore as a server identity for `ssl` in the ${product.name} `<security-realms>` section of the `standalone.xml`file. Make sure to backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`

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

## Configure Undertow Subsystem for SSL

If you are running with the default-server, add the `https-listener` to the `undertow` subsystem:

        <subsystem xmlns="urn:jboss:domain:undertow:3.0">
            <server name="default-server">
                <https-listener name="https" socket-binding="https" security-realm="UndertowRealm"/>
                ...
                ...
            </server>
            ...
            ...
        </subsystem>

Now you're ready to connect to the SSL port of your instance https://localhost:8443/. Note, that you get the privacy error as the server certificate is self-signed. If you need to use a fully signed certificate you mostly get a PEM file from the Certificate Authority. In such a case, you need to import the PEM into the keystore.

## Test the Server SSL Configuration

To test the SSL configuration, access: <https://localhost:8443>

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


## Access the Application

The application will be running at the following URL: <https://localhost:8443/${project.artifactId}/>.


## Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Remove the SSL Configuration

1. If the server is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources        
