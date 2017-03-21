# security-vault-askpass: Using Security Vault based on external askpass command to secure Datasource password

Author: Ivo Studensky  
Level: Intermediate  
Technologies: Servlet, Security Vault, Java KeyStore  
Summary: The `security-vault-askpass` quickstart demonstrates how to use the Security Vault to secure a Datasource password.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

This example demonstrates the use of Security Vault to secure Datasource password in ${product.name.full}.

Security Vault is a facility of PicketBox which allows to mask sensitive attributes such as passwords.
With the use of Security Vault any password can be masked by a cipher algorithm whose key is taken from a Java KeyStore.
The Java KeyStore itself is guarded by another password which can either be masked again or taken from an external utility.
This example demonstrates the use of Security Vault provided with the keystore key by an askpass-like external command,
eg. ksshaskpass for KWallet, gnome-ssh-askpass for GNOME Keyring, etc.

This quickstart uses a mock askpass script instead of a real one, see `bin/askpass.sh`.
An example of the external command configuration for ksshaskpass could be as follows:

        {CMD}/usr/bin/ksshaskpass,Enter passphrase for askpass quickstart

This quickstart takes the following steps to set up Security Vault:

1. Create Java KeyStore for Security Vault
2. Initialize Security Vault
3. Define Security Vault in the `standalone.xml` configuration file
4. Add a password in the Vault format to the Datasource definition


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Create the Java Keystore for the Security Vault

You can leverage standard JDK tool to create a KeyStore as follows:

        $ keytool -genkey -alias vault -keyalg RSA -keysize 1024  -keystore vault.keystore
        Enter keystore password: vault22
        Re-enter new password:vault22
        What is your first and last name?
          [Unknown]:  Picketbox vault
        What is the name of your organizational unit?
          [Unknown]:  picketbox
        What is the name of your organization?
          [Unknown]:  JBoss
        What is the name of your City or Locality?
          [Unknown]:  chicago
        What is the name of your State or Province?
          [Unknown]:  il
        What is the two-letter country code for this unit?
          [Unknown]:  us
        Is CN=Picketbox vault, OU=picketbox, O=JBoss, L=chicago, ST=il, C=us correct?
          [no]:  yes

        Enter key password for <vault>
                (RETURN if same as keystore password):

It is important to keep track of the keystore password and the alias. In this example, the keystore password is `vault22` and the alias is `vault`.
If one prefers just one command here is equivalent:

        keytool -genkey -alias vault -keystore vault.keystore -keyalg RSA -keysize 1024 -storepass vault22 -keypass vault22 -dname "CN=Picketbox vault,OU=picketbox,O=JBoss,L=chicago,ST=il,C=us"


## Use the Vault Tool script to store the password into the Vault

This quickstart needs to store the Datasource password (`sa`) to the Vault. This can be done with the following command:

        $ ${jboss.home.name}/bin/vault.sh -k "$PWD/vault.keystore" -p "{CMD}sh,$PWD/bin/askpass.sh,Enter passphrase for askpass quickstart" -e "$PWD" -i 50 -s 12345678 -v vault -b ds_SecurityVaultDS -a password -x sa

        =========================================================================

          JBoss Vault

          ${jboss.home.name}: .../wildfly-8.0.0.Final

          JAVA: .../bin/java

        =========================================================================

        Feb 21, 2014 9:15:15 AM org.picketbox.plugins.vault.PicketBoxSecurityVault init
        INFO: PBOX000361: Default Security Vault Implementation Initialized and Ready
        Secured attribute value has been stored in Vault.
        Please make note of the following:
        ********************************************
        Vault Block:ds_SecurityVaultDS
        Attribute Name:password
        Configuration should be done as follows:
        VAULT::ds_SecurityVaultDS::password::1
        ********************************************
        Vault Configuration in ${product.name} configuration file:
        ********************************************
        ...
        </extensions>
        <vault>
          <vault-option name="KEYSTORE_URL" value=".../quickstart/security-vault-askpass/vault.keystore"/>
          <vault-option name="KEYSTORE_PASSWORD" value="{CMD}sh,.../quickstart/security-vault-askpass/bin/askpass.sh,Enter passphrase for askpass quickstart"/>
          <vault-option name="KEYSTORE_ALIAS" value="vault"/>
          <vault-option name="SALT" value="12345678"/>
          <vault-option name="ITERATION_COUNT" value="50"/>
          <vault-option name="ENC_FILE_DIR" value=".../quickstart/security-vault-askpass/"/>
        </vault><management> ...
        ********************************************
        $

Please note the Vault value of the Datasource password `VAULT::ds_SecurityVaultDS::password::1`, this value must match
the one defined in the `/src/main/webapp/WEB-INF/security-vault-quickstart-ds.xml` file.

There is also an option of interactive session in the Vault Tool script. The interactive session, however, cannot
take an external command to get the KeyStore password. The password has to be manually typed in instead.

For more information use `vault.sh --help`.

## Configure the Server

<!-- Add CLI commands -->

### Configure the Security Vault by Manually Editing the Server Configuration File

1.  If it is running, stop the ${product.name} Server.
2.  Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
3.  Open the `${jboss.home.name}/standalone/configuration/standalone.xml` file in an editor and locate the terminating element `</extensions>` there.
4.  Add the XML snippet generated before to the configuration file:

        <vault>
          <vault-option name="KEYSTORE_URL" value=".../quickstart/security-vault-askpass/vault.keystore"/>
          <vault-option name="KEYSTORE_PASSWORD" value="{CMD}sh,.../quickstart/security-vault-askpass/bin/askpass.sh,Enter passphrase for askpass quickstart"/>
          <vault-option name="KEYSTORE_ALIAS" value="vault"/>
          <vault-option name="SALT" value="12345678"/>
          <vault-option name="ITERATION_COUNT" value="50"/>
          <vault-option name="ENC_FILE_DIR" value=".../quickstart/security-vault-askpass/"/>
        </vault>


## Start the Server with the Web Profile

1. Open a command line and navigate to the root of the ${product.name} server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   bin/standalone.sh
        For Windows: bin\standalone.bat

## Build and Deploy the Quickstart

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/AskpassServlet>.

When you access the application the browser should display the following info:

        Security Vault Askpass Servlet
        The connection to the datasource is valid? true

_NOTE:  The H2 database used in this quickstart is known to not be precisely reliable which applies to its login/authentication facilities as well. If you want to run this quickstart  in a more reliable way you need to switch the database it is running against to a different one. See [Install and Configure the PostgreSQL Database](../README.md#postgresql) for instructions._

## Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Remove the Security Vault Configuration

You can remove the Security Vault configuration by manually restoring the back-up copy of the configuration file.

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file
   or remove the Vault related lines added there before.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure you have [created the Java keystore for the security vault](#create-the-java-keystore-for-the-security-vault).
* Be sure to configure the security domain by running the JBoss CLI commands as described above under [Configure the ${product.name} Server](#configure-the-server). Stop the server at the end of that step.
* Be sure to [Remove the Security Domain Configuration](#remove-the-security-domain-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

      mvn dependency:sources
