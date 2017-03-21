# logging: Example That Sets Up Different Logging Levels

Author: Joel Tosi  
Level: Intermediate  
Technologies: Logging  
Summary: The `logging` quickstart demonstrates how to configure different logging levels in ${product.name}. It also includes an asynchronous logging example.  
Prerequisites: None  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `logging` quickstart demonstrates how to set up and log different levels of information in ${product.name.full}. An example of asynchronous logging is also included in the configuration examples.

This quickstart contains just one class file and one JSP file. When you access the application, it fires off the logging information.

To better visualize how the logging configuration works, you first deploy and access the application before configuring the logs and view the resulting log files. Then you configure the logs, redeploy and access the application, and look at the log files again to see the differences.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Start the Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the ${product.name} server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This deploys `target/jboss-logging.war` to the running instance of the server.


## Access the Application

The application is running at the following URL: <http://localhost:8080/${project.artifactId}/>.

You will see the following message in the server console:

        FATAL [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS A FATAL MESSAGE
        ERROR [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS AN ERROR MESSAGE
        WARN  [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS A WARNING MESSAGE
        INFO  [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS AN INFO MESSAGE
        ERROR [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS AN ERROR MESSAGE
        FATAL [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS A FATAL MESSAGE
        INFO  [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS AN INFO MESSAGE
        WARN  [org.jboss.as.quickstarts.logging.LoggingExample] (default task-1) THIS IS A WARNING MESSAGE


## Check the Server Logs

The log files are located in the `${jboss.home.name}/standalone/log` log directory. At this point you should see the following log files.

        * `server.log` - This is the standard log produced by the application server. By default, it contains all the server log messages, including the server startup messages.
        * `gc.log.0.current` - This is a garbage collection log. It contains garbage collection activity and can be used for diagnostic purposes. This log can be ignored as it is not used in this quickstart.


## Configure the Server

You configure server logging by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-logging.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-logging.cli` file in the root of this quickstart directory. This script configures the logging subsytem in the server configuration file. It configures the periodic rotating file handlers and the async handlers, creates the logger for our quickstart class and sets the level to TRACE, and assigns the async handlers for our quickstart class.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-logging.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-logging.cli
You should see the following result when you run the script:

        The batch executed successfully


## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

The following XML was added to the `logging` subsystem.

        <async-handler name="TRACE_QS_ASYNC">
            <level name="TRACE"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_TRACE"/>
            </subhandlers>
        </async-handler>
        <async-handler name="DEBUG_QS_ASYNC">
            <level name="DEBUG"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_DEBUG"/>
            </subhandlers>
        </async-handler>
        <async-handler name="INFO_QS_ASYNC">
            <level name="INFO"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_INFO"/>
            </subhandlers>
        </async-handler>
        <async-handler name="WARN_QS_ASYNC">
            <level name="WARN"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_WARN"/>
            </subhandlers>
        </async-handler>
        <async-handler name="ERROR_QS_ASYNC">
            <level name="ERROR"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_ERROR"/>
            </subhandlers>
        </async-handler>
        <async-handler name="FATAL_QS_ASYNC">
            <level name="FATAL"/>
            <queue-length value="1024"/>
            <overflow-action value="block"/>
            <subhandlers>
                <handler name="FILE_QS_FATAL"/>
            </subhandlers>
        </async-handler>
        ...
        <periodic-rotating-file-handler name="FILE_QS_TRACE">
            <file relative-to="jboss.server.log.dir" path="quickstart.trace.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_DEBUG">
            <file relative-to="jboss.server.log.dir" path="quickstart.debug.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_INFO">
            <file relative-to="jboss.server.log.dir" path="quickstart.info.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_WARN">
            <file relative-to="jboss.server.log.dir" path="quickstart.warn.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_ERROR">
            <file relative-to="jboss.server.log.dir" path="quickstart.error.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        <periodic-rotating-file-handler name="FILE_QS_FATAL">
            <file relative-to="jboss.server.log.dir" path="quickstart.fatal.log"/>
            <suffix value=".yyyy.MM.dd"/>
        </periodic-rotating-file-handler>
        ...
        <logger category="org.jboss.as.quickstarts.logging">
            <level name="TRACE"/>
            <handlers>
                <handler name="TRACE_QS_ASYNC"/>
                <handler name="DEBUG_QS_ASYNC"/>
                <handler name="INFO_QS_ASYNC"/>
                <handler name="WARN_QS_ASYNC"/>
                <handler name="ERROR_QS_ASYNC"/>
                <handler name="FATAL_QS_ASYNC"/>
            </handlers>
        </logger>


## Test the New Logging Configuration

1. If your server is not started, then [Start the ${product.name} server](#start-the-server).
2. [Build and deploy the quickstart](#build-and-deploy-the-quickstart).
3. [Access the application](#access-the-application).


## Recheck the Server Logs

The log files are located in the `${jboss.home.name}/standalone/log` log directory. You should now see 8 log files.

* The following logs are the standard log files produced by the application server:
    * `server.log` - The standard log produced by the application server.
    * `gc.log.0.current` - The garbage collection log can be ignored as it is not used in this quickstart.


* The following logs are produced by the quickstart. They are listed below in hierarchical order from the largest file containing the most messages to the smallest file containing the least messages.
    * `quickstart.trace.log`
    * `quickstart.debug.log`
    * `quickstart.info.log`
    * `quickstart.warn.log`
    * `quickstart.error.log`
    * `quickstart.fatal.log`

The following describes what happens when you access this quickstart:

1. The application class file fires off logs of the various types (INFO, DEBUG, TRACE, WARN, ERROR, FATAL).  Each log message goes to a different file, as defined in the `standalone.xml` file.  Also notice in the `standalone.xml` that the application package defines its own log level.
2. The class file demonstrates the usage of *log guards*.  *Log guards* are a development best practice.  Simply put, instead of just writing out logs, we wrap the log writes in a check for that log level being enabled. While this may seem like overhead, that boolean check is more efficient than relying on the underlying framework to do the check at write time.
3. Finally, the class file logs various levels, each to its own file as configured in `standalone.xml`.  Note that log levels are hierarchical.  When set, all log levels above the specified level are logged as well.
4. Common uses of the 6 log levels are outlined below. You should use the level that makes the most sense in your environment.

        FATAL - Used to track critical system failures.  When this log message is written, it is writing application error that has caused service to cease.  This is the most narrow logging.  
        ERROR - Used to track application errors that may cause one request to fail (not a service ceasement).
        WARN - This is setting is used in most production environments.  At this level, all *WARN*, *ERROR*, and *FATAL* messages are written.  Use this level message  as a predictive measure for possible forthcoming issues.  
        INFO - Usually only used in a development environment.  This provides any information - state transition, object values, etc
        DEBUG - Turned on in any environment when a problem is occuring.  The information captured may be throughput, communication, object values, etc.
        TRACE - Turned on in any environment where you are trying to follow an execution path, for optimization or debugging.  This is the most broad logging level and all messages are written.


5. To view log file differences for different logging levels, change the level for the "org.jboss.as.quickstarts.logging" logger
from TRACE to DEBUG, INFO, WARN, ERROR, or FATAL, then access the application.

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Remove the Logging Configuration

You can remove the logging configuration by running the  `remove-logging.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Remove the Logging Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=remove-logging.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=remove-logging.cli
This script removes the log and file handlers from the `logging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully


### Remove the Logging Configuration Manually
1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to configure ${product.name} server logging as described above under [Configure the ${product.name} Server](#configure-the-server). Stop the server at the end of that step.
* Be sure to [Remove the Logging Configuration](#remove-the-logging-configuration) when you have completed testing this quickstart.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
