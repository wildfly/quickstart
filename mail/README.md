# mail: E-Mail Example using CDI and JSF

Author: Joel Tosi  
Level: Beginner  
Technologies: JavaMail, CDI, JSF  
Summary: The `mail` quickstart demonstrates how to send email using CDI and JSF and the default Mail provider that ships with WildFly.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

## What is it?

The `mail` quickstart demonstrates sending email with the use of *CDI* (Contexts and Dependency Injection) and *JSF* (JavaServer Faces) in WildFly Application Server.

The mail provider is configured in the `mail` subsystem of the `WILDFLY_HOME/standalone/configuration/standalone.xml` configuration file if you are running a standalone server or in the `WILDFLY_HOME/domain/configuration/domain.xml` configuration file if you are running in a managed domain.

You can use the default mail provider that comes out of the box with WildFly. It uses your local mail relay and the default SMTP port of 25. However, this quickstart demonstrates how to define and use a custom mail provider.

This example is a web application that takes `To`, `From`, `Subject`, and `Message Body` input and sends mail to that address. The front end is a JSF page with a simple POJO backing, leveraging CDI for resource injection.


## System Requirements

The application this project produces is designed to be run on WildFly Application Server 11 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.3.1 or later. See [Configure Maven for WildFly 11](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of WILDFLY_HOME

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_WILDFLY_HOME.md#use-of-eap_home-and-jboss_home-variables).

##  Configure an SMTP Server on Your Local Machine

This quickstart expects that you have an SMTP mail server running on your machine and configured for the default port `localhost:25`.
To configure an SMTP mail server, consult the documentation for your operating system. It is beyond the scope of this quickstart to provide these instructions.

If you do not configure an SMTP mail server on your local machine, you will see the exception `com.sun.mail.util.MailConnectException:  Couldn't connect to host, port: localhost, 25; timeout -1;` when you access the application and attempt to send an email.

## Configure the Server

You configure the custom mail session in WildFly by running Management CLI commands. For your convenience, this quickstart batches the commands into a `configure-mail-session.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the WildFly server.
    * Back up the file: `WILDFLY_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the WildFly server by typing the following:

        For Linux:  WILDFLY_HOME/bin/standalone.sh
        For Windows:  WILDFLY_HOME\bin\standalone.bat
3. Review the `configure-mail-session.cli` file in the root of this quickstart directory. This script creates custom outbound socket binding port for SMTP, POP3, and IMAP. It then creates the custom `MyOtherMail` mail session and configures it to use the custom outbound socket binding ports.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=configure-mail-session.cli
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=configure-mail-session.cli
    You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

5. Stop the WildFly server.


## Review the Modified Server Configuration

After stopping the server, open the `WILDFLY_HOME/standalone/configuration/standalone.xml` file and review the changes.

The following `outbound-socket-binding` groups are added to the `standard-sockets` `<socket-binding-group>` element.

      <socket-binding-group name="standard-sockets" default-interface="public" port-offset="${jboss.socket.binding.port-offset:0}">
        ...
        </outbound-socket-binding>
        <outbound-socket-binding name="my-smtp-binding">
            <remote-destination host="localhost" port="25"/>
        </outbound-socket-binding>
        <outbound-socket-binding name="my-pop3-binding">
            <remote-destination host="localhost" port="110"/>
        </outbound-socket-binding>
        <outbound-socket-binding name="my-imap-binding">
            <remote-destination host="localhost" port="143"/>
        </outbound-socket-binding>
     </socket-binding-group>

The `MyOtherMail` mail session is added to the `mail` subsystem and configured to use the custom outbound socket binding ports.

      <subsystem xmlns="urn:jboss:domain:mail:2.0">
         <mail-session name="default" jndi-name="java:jboss/mail/Default">
            <smtp-server outbound-socket-binding-ref="mail-smtp"/>
         </mail-session>
         <mail-session name="MyOtherMail" jndi-name="java:jboss/mail/MyOtherMail">
            <smtp-server password="pass" username="nobody" tls="true" outbound-socket-binding-ref="my-smtp-binding"/>
            <pop3-server outbound-socket-binding-ref="my-pop3-binding"/>
            <imap-server password="pass" username="nobody" outbound-socket-binding-ref="my-imap-binding"/>
         </mail-session>
      </subsystem>

## Start the Server

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/mail.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/mail/>.

Note: If you see `Error processing request` in the browser when you access the application and attempt to send email, followed by `javax.servlet.ServletException: com.sun.mail.util.MailConnectException: Couldn't connect to host, port: localhost, 25; timeout -1; nested exception is: java.net.ConnectException: Connction refused`, make sure you followed the instructions above to [Configure an SMTP Server on Your Local Machine](#configure-an-smtp-server-on-your-local-machine).

## Undeploy the Archive

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Remove the Mail Configuration

You can remove the mail configuration by running the  `remove-mail-session.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Remove the Custom Mail Configuration by Running the JBoss CLI Script

1. Start the WildFly server by typing the following:

        For Linux:  WILDFLY_HOME/bin/standalone.sh
        For Windows:  WILDFLY_HOME\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=remove-mail-session.cli
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=remove-mail-session.cli
   This script removes the custom `MyOtherMail` session from the `mail` subsystem in the server configuration. file You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required


### Remove the Custom Mail Configuration Manually
1. If it is running, stop the WildFly server.
2. Replace the `WILDFLY_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

_NOTE:_

* Be sure to [Configure an SMTP Server on Your Local Machine](#configure-an-smtp-server-on-your-local-machine).
* Be sure to configure the WildFly custom mail configuration as described above under [Configure the WildFly Server](#configure-the-server). Stop the server at the end of that step.
* To deploy the server project, right-click on the `mail` project and choose `Run As` --> `Run on Server`.  A browser window appears that accesses the running application.
* Be sure to [Remove the Mail Configuration](#remove-the-mail-configuration) when you have completed testing this quickstart.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
