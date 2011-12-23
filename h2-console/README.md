jboss-as-h2-console
===================

What is it?
-----------

JBoss AS bundles H2 as an in memory, in process database. H2 is written in Java so
can run on any platform JBoss AS runs.

This is quickstart shows you how to use the H2 console with JBoss AS. It reuses the
jboss-as-greeter quickstart as a GUI for entering data.


System requirements
-------------------

All you need to follow these instructions is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better, and the jboss-as-greeter quickstart (normally located in `../greeter`).

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. 
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

Note: Unlike most of the quickstarts, you will require JBoss AS 7.1.x or later to use this quickstart.

Finally, you'll need the H2 Console. You can download it from <http://www.h2database.com/html/download.html>. We recommend using the platform independent zip.
 
With the prerequisites out of the way, you're ready to build and deploy.

Deploying the sample jboss-as-greeter application
-------------------------------------------------
 
First you need to start JBoss AS 7 (or EAP 6). To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal. Assuming the jboss-as-greeter application is in `../greeter`:

    mvn package -f ../greeter/pom.xml

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy -f ../greeter/pom.xml

This will deploy `target/jboss-as-greeter.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-greeter/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy -f ../greeter/pom.xml

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
 
You can read more about the greeter application in the README.md for that project.

Deploying the H2 Console
------------------------

This quickstart comes bundled with a version of the H2 Console built for JBoss AS 7 (the changes to
the stock console are discussed below). Deploy the console by copying the `h2console.war` to the `$JBOSS_HOME/standalone/deployments` directory. You can use the console by visiting <http://localhost:8080/h2console>.

You need to enter the JDBC URL, and credentials. To access the "test" database that the greeter quickstart uses, use these details

* JDBC URL `jdbc:h2:mem:test;DB_CLOSE_DELAY=-1`
* User Name `sa`
* Password `sa`

Now, hit the Test Connection button, and make sure you can connect. If you can, go ahead and click Connect.

Now, let's take a look at the data added by the greeter application. Run the following SQL command

    select * from users;

You should see the two seed users, and any you have added using the greeter application

Changes to the H2 Console for JBoss AS 7
----------------------------------------

In order to make the H2 console run on JBoss AS 7 we had to add a dependency on the H2 module from JBoss AS (added to the META-INF/MANIFEST.MF), and remove the H2 libraries from the war. The rebuilt console is provided in this quickstart.
