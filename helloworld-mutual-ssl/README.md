# helloworld-mutual-ssl: ${product.name} Mutual SSL(two-way) Configuration Example

Author: Giriraj Sharma, Stefan Guilhen  
Level: Intermediate  
Technologies: Mutual SSL, Undertow  
Summary: The `helloworld-mutual-ssl` quickstart is a basic example that demonstrates mutual SSL configuration in ${product.name}  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

Mutual SSL provides the same security as SSL, with the addition of authentication and non-repudiation of the client authentication, using digital signatures. When mutual authentication is used, the server requests the client to provide a certificate in addition to the server certificate issued to the client. Mutual authentication requires an extra round trip each time for client certificate exchange. In addition, the client must buy and maintain a digital certificate.

This quickstart shows how to configure ${product.name} to enable TLS/SSL configuration for the new ${product.name} `undertow` subsystem and enable mutual (two-way) SSL authentication.

Before you run this example, you must create certificates and configure the server to use two-way SSL.

## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Set Up Client and Server Keystores Using Java Keytool

1.  Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   ${jboss.home.name}/standalone/configuration
        For Windows: ${jboss.home.name}\standalone\configuration
2. Create a certificate for your server using the following command:

        $>keytool -genkey -keyalg RSA -keystore server.keystore -storepass secret -keypass secret -validity 365

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

3. Create the client certificate, which is used to authenticate against the server when accessing a resource through SSL.

        $>keytool -genkey -keystore client.keystore -storepass secret -validity 365 -keyalg RSA -keysize 2048 -storetype pkcs12

        What is your first and last name?
            [Unknown]:  quickstartUser
        What is the name of your organizational unit?
            [Unknown]:  Sales
        What is the name of your organization?
            [Unknown]:  My Company
        What is the name of your City or Locality?
            [Unknown]:  Sao Paulo
        What is the name of your State or Province?
            [Unknown]:  Sao Paulo
        What is the two-letter country code for this unit?
            [Unknown]:  BR
        Is CN=quickstartUser, OU=Sales, O=My Company, L=Sao Paulo, ST=Sao Paulo, C=BR correct?
            [no]:  yes

4. Export the client certificate and create a truststore by importing this certificate:

        $>keytool -exportcert -keystore client.keystore  -storetype pkcs12 -storepass secret -keypass secret -file client.crt
        $>keytool -import -file client.crt -alias quickstartUser -keystore client.truststore -storepass secret

        Owner: CN=quickstartUser, OU=Sales, O=My Company, L=Sao Paulo, ST=Sao Paulo, C=BR
        Issuer: CN=quickstartUser, OU=Sales, O=My Company, L=Sao Paulo, ST=Sao Paulo, C=BR
        Serial number: 7fd95ce4
        Valid from: Mon Jul 24 16:14:03 BRT 2017 until: Tue Jul 24 16:14:03 BRT 2018
        Certificate fingerprints:
	         MD5:  87:41:C5:CC:E6:79:91:F0:9D:90:AD:9E:DD:57:81:80
	         SHA1: 55:35:CA:B0:DC:DD:4F:E6:B8:9F:45:4B:4B:98:93:B5:3B:7C:55:84
	         SHA256: 0A:FC:93:B6:25:5A:74:42:B8:A1:C6:5F:69:88:72:7F:27:A9:81:B0:17:0C:F1:AF:3D:DE:B7:E5:F1:69:66:4B
	         Signature algorithm name: SHA256withRSA
	         Version: 3

        Extensions:

        #1: ObjectId: 2.5.29.14 Criticality=false
        SubjectKeyIdentifier [
        KeyIdentifier [
        0000: 95 84 BE C6 32 BB 2B 13   4C 7F 5D D4 C4 C8 22 12  ....2.+.L.]...".
        0010: CB 09 39 09                                        ..9.
        ]
        ]

        Trust this certificate? [no]:  yes
        Certificate was added to keystore

5. Export client certificate to pkcs12 format

        $>keytool -importkeystore -srckeystore client.keystore -srcstorepass secret -destkeystore clientCert.p12 -srcstoretype PKCS12 -deststoretype PKCS12 -deststorepass secret

6. The certificates and keystores are now properly configured.

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

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `key-store`s were added to the `elytron` subsystem:

        <key-store name="qsKeyStore">
            <credential-reference clear-text="secret"/>
            <implementation type="JKS"/>
            <file path="server.keystore" relative-to="jboss.server.config.dir"/>
        </key-store>
        <key-store name="qsTrustStore">
            <credential-reference clear-text="secret"/>
            <implementation type="JKS"/>
            <file path="client.truststore" relative-to="jboss.server.config.dir"/>
        </key-store>

2. The following `key-manager` was added to the `elytron` subsystem:

        <key-managers>
            <key-manager name="qsKeyManager" key-store="qsKeyStore">
                <credential-reference clear-text="secret"/>
            </key-manager>
        </key-managers>

3. The following `trust-manager` was added to the `elytron` subsystem:

        <trust-managers>
            <trust-manager name="qsTrustManager" key-store="qsTrustStore"/>
        </trust-managers>

4. The following `ssl-context` was added to the `elytron` subsystem:

        <server-ssl-contexts>
            <server-ssl-context name="qsSSLContext" protocols="TLSv1.2" need-client-auth="true" key-manager="qsKeyManager" trust-manager="qsTrustManager"/>
        </server-ssl-contexts>

5. The `https-listener` in the `undertow` subsystem was changed to reference the `qsSSLContext` `ssl-context`:

        <https-listener name="https" socket-binding="https" ssl-context="qsSSLContext" enable-http2="true"/>

## Test the Server SSL Configuration

To test the SSL configuration, access: <https://localhost:8443>

If it is configured correctly, you should be asked to trust the server certificate.

## Import the Client Certificate into Your Browser

Before you access the application, you must import the *clientCert.p12*, which holds the client certificate, into your browser.

### Import the Client Certificate into Google Chrome

1. Click the Chrome menu icon (3 dots) in the upper right on the browser toolbar and choose 'Settings'. This takes you to <chrome://settings/>.
2. Scroll to the bottom of the page and click on the 'Advanced' link to reveal the advanced settings.
3. Search for the 'Manage Certificates' line under 'Privacy and security' and then click on it.
4. In the 'Manage certificates' screen, select the 'Your Certificates' tab and click on the 'Import' button.
5. Select the `clientCert.p12` file. You will be prompted to enter the  password: `secret`.
6. The client certificate is now installed in the Google Chrome browser.

### Import the Client Certificate into Mozilla Firefox

1. Click the 'Edit' menu item on the browser menu and choose 'Preferences'.
2. A new window will open. Select the 'Advanced' icon and after that the 'Certificates' tab.
3. On the 'Certificates' tab, mark the option 'Ask me every time' and click the 'View Certificates' button.
4. A new window will open. Select the 'Your Certificates' tab and click the 'Import' button.
5. Select the `clientCert.p12` file. You will be prompted to enter the  password: `secret`.
6. The certificate is now installed in the Mozilla Firefox browser.

## Start the Server

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   /bin/standalone.sh
        For Windows: bin\standalone.bat

## Build and Deploy the Quickstart

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of one of the quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.

## Access the Application

The application will be running at the following URL: <https://localhost:8443/${project.artifactId}/HelloWorld>.

A page displaying the client certificate should be visible:

        Hello World ! Wildfly mutual SSL is configured and client certificate is verified !!

        Client Certificate Pem: MIIDhTCCAm2gAwIBAgIEf9lc5DANBgkqhkiG9w0BAQsFADBzMQswCQYDVQQGEwJCUjESMBAGA1UECBMJU2FvIFBhd
        WxvMRIwEAYDVQQHEwlTYW8gUGF1bG8xEzARBgNVBAoTCk15IENvbXBhbnkxDjAMBgNVBAsTBVNhbGVzMRcwFQYDVQQDEw5xdWlja3N0YXJ0VXNlcj
        AeFw0xNzA3MjQxOTE0MDNaFw0xODA3MjQxOTE0MDNaMHMxCzAJBgNVBAYTAkJSMRIwEAYDVQQIEwlTYW8gUGF1bG8xEjAQBgNVBAcTCVNhbyBQYXV
        sbzETMBEGA1UEChMKTXkgQ29tcGFueTEOMAwGA1UECxMFU2FsZXMxFzAVBgNVBAMTDnF1aWNrc3RhcnRVc2VyMIIBIjANBgkqhkiG9w0BAQEFAAOC
        AQ8AMIIBCgKCAQEAnHwflE8K/ArTPbTeZZEFK+1jtpg9grPSD62GIz/awoIDr6Rf9vCBTpAg4lom62A0BNZDEJKdab/ExNOOBRY+/pOnYlXZTYlDp
        dQQap0E7UP5EfHNZsafgpfILCop2LdTuUbcV7tLKBsthJLJ0ZCoG5QJFble+OPxEbissOvIqHfvUJZi34k9ULteLJc330g0uTuDrLgtoFQ0cbHa4F
        CQ86o85EuRPpFeW6EBA3iYE/tKHSYsK7QSajefX6jZjXoZiUflw97SAGL43ZtvNbrKRywEfsVqDpDurjBg2HI+YahuDz5R1QWTSyTHWMZzcyJYqxj
        XiSf0oK1cUahn6m5t1wIDAQABoyEwHzAdBgNVHQ4EFgQUlYS+xjK7KxNMf13UxMgiEssJOQkwDQYJKoZIhvcNAQELBQADggEBADkp+R6kSNXJNfih
        qbDRp3uFtNMG6OgaYsfC7RtNLMdrhvoLlU7uWzxVCFuifvNlWVRiADBHDCRQU2uNRFW35GQSfHQyok4KoBuKlfBtQ+Xu7c8R0JzxN/rPJPXoCbShz
        DHo1uoz5/dzXZz0EjjWCPJk+LVEhEvH0GcWAp3x3irpNU4hRZLd0XomY0Z4NnUt7VMBNYDOxVxgT9qcLnEaEpIfYULubLLCFHwAga2YgsKzZYLuwM
        aEWK4zhPVFynfnMaOxI67FC2QzhfzERyKqHj47WuwN0xWbS/1gBypS2nUwvItyxaEQG2X5uQY8j8QoY9wcMzIIkP2Mk14gJGHUnA8=

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
    This script reverts the changes made to the `undertow` subsystem and it also removes the `ssl-context`, `key-manager`, `trust-manager` and `key-store`s
from the `elytron` subsystem. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.

## Remove the keystores and certificates created for this quickstart

1. Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   standalone/configuration
        For Windows: standalone\configuration
2. Remove the `clientCert.p12`, `client.crt`, `client.keystore`, `client.truststore` and `server.keystore` files that
were generated for this quickstart.

## Remove the Client Certificate from Your Browser

After you are done with this quickstart, remember to remove the certificate that was imported into your browser.

### Remove the Client Certificate from Google Chrome

1. Click the Chrome menu icon (3 dots) in the upper right on the browser toolbar and choose 'Settings'. This takes you to <chrome://settings/>.
2. Scroll to the bottom of the page and click on the 'Advanced' link to reveal the advanced settings.
3. Search for the 'Manage Certificates' line under 'Privacy and security' and then click on it.
4. In the 'Manage certificates' screen, select the 'Your Certificates' tab and then click on the arrow to the right of the certificate to be removed.
5. The certificate is expanded, displaying the `quickstartUser` entry. Click on the icon (3 dots) to the right of it and then select 'Delete'.
6. Confirm the deletion in the dialog box. The certificate has now been removed from the Google Chrome browser.

### Remove the Client Certificate from Mozilla Firefox

1. Click the 'Edit' menu item on the browser menu and choose 'Preferences'.
2. A new window will open. Select the 'Advanced' icon and after that the 'Certificates' tab.
3. On the 'Certificates' tab click the 'View Certificates' button.
4. A new window will open. Select the 'Your Certificates' tab.
5. Select the `quickstartUser` certificate and click the `Delete` button.
6. The certificate has now been removed from the Mozilla Firefox browser.

## Run the Quickstart in JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to configure the keystores and client certificates as described under [Set Up Client and Server Keystores Using Java Keytool](#set-up-client-and-server-keystores-using-java-keytool).
* Depending on the browser you choose, be sure to either [import the certificate into Chrome](#import-the-client-certificate-into-google-chrome) or [import the certificate into Firefox](#import-the-client-certificate-into-mozilla-firefox).
* Be sure to configure the server by running the JBoss CLI commands as described above under [Configure the ${product.name} Server](#configure-the-server). Stop the server at the end of that step.
* In JBoss Developer Studio, choose *Window* --> *Web Browser*, then select the browser you chose to import the certificate.
* To deploy the application, right-click on the `${project.artifactId}` project and choose `Run As` --> `Run on Server`.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
