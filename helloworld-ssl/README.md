# helloworld-ssl: ${product.name} Server Side SSL Configuration Example

Author: Giriraj Sharma, Stefan Guilhen  
Level: Beginner  
Technologies: SSL, Undertow  
Summary: The `helloworld-ssl` quickstart is a basic example that demonstrates server side SSL configuration in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

This `helloworld-ssl` quickstart demonstrates the configuration of *SSL* in *${product.name.full}*.

This quickstart shows how to configure ${product.name} to enable TLS/SSL configuration for the new `undertow` web subsystem.

Before you run this example, you must create certificates and configure the server to use SSL.


## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Generate a Keystore and Self-signed Certificate

1. Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   standalone/configuration
        For Windows: standalone\configuration
2. Create a certificate for your server using the following command:

        $>keytool -genkey -alias mycert -keyalg RSA -sigalg MD5withRSA -keystore server.keystore -storepass secret -keypass secret -validity 9999

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

## Configure the Server

These steps assume you are running the server in standalone mode and using the default `standalone.xml` supplied with the distribution.

You configure the SSL context by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-ssl.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Back up the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-ssl.cli` file in the root of this quickstart directory. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-ssl.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-ssl.cli
    You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

5. Stop the ${product.name} server.

Now you're ready to connect to the SSL port of your instance https://localhost:8443/. Note, that you get the privacy error as the server certificate is self-signed. If you need to use a fully signed certificate you mostly get a PEM file from the Certificate Authority. In such a case, you need to import the PEM into the keystore.

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `key-store` was added to the `elytron` subsystem:

        <key-stores>
            <key-store name="qsKeyStore">
                <credential-reference clear-text="secret"/>
                <implementation type="JKS"/>
                <file path="server.keystore" relative-to="jboss.server.config.dir"/>
            </key-store>
        </key-stores>

2. The following `key-manager` was added to the `elytron` subsystem:

        <key-managers>
            <key-manager name="qsKeyManager" algorithm="SunX509" key-store="qsKeyStore">
                <credential-reference clear-text="secret"/>
            </key-manager>
        </key-managers>

3. The following `ssl-context` was added to the `elytron` subsystem:

        <server-ssl-contexts>
            <server-ssl-context name="qsSSLContext" protocols="TLSv1.2" key-manager="qsKeyManager"/>
        </server-ssl-contexts>

4. The `https-listener` in the `undertow` subsystem was changed to reference the `qsSSLContext` `ssl-context`:

        <https-listener name="https" socket-binding="https" ssl-context="qsSSLContext" enable-http2="true"/>

## Test the Server SSL Configuration

To test the SSL configuration, access: <https://localhost:8443>

## Start the Server

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

## Restore the Server Configuration

You can restore the original server configuration by running the  `restore-configuration.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Restore the Server Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-configuration.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-configuration.cli
    This script reverts the changes made to the `undertow` subsystem and it also removes the `ssl-context`, `key-manager`
    and `key-store` from the `elytron` subsystem. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.

## Remove the keystore created for this quickstart

1. Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   standalone/configuration
        For Windows: standalone\configuration
2. Remove the keystore generated for this quickstart.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to configure the server by running the JBoss CLI commands as described above under [Configure the ${product.name} Server](#configure-the-server). Stop the server at the end of that step.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources        
