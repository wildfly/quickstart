kitchensink-rf: Example Using Multiple Java EE 6 Technologies with RichFaces
============================================================================
Author: Pete Muir

What is it?
-----------

This is your project! It's a sample, deployable Maven 3 project to help you
get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss Enterprise Application Platform 6. This 
project is setup to allow you to create a compliant Java EE 6 application 
using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. It includes
a persistence unit and some sample persistence and transaction code to help 
you get your feet wet with database access in enterprise Java.

This application builds on top of the standard JSF approach, by incorporating the RichFaces project to provide a set of
components, allowing for a rich user experience.  RichFaces builds on top of the JSF standard, and is a fully portable
solution compatible with all JSF implementations.

The kitchensink quickstart is built using Vanilla JSF for it's front end.  With this kitchensink-rf quickstart, we build
on top of the JSF user interface, augmenting it with RichFaces JSF components and capabilities.  Some key points to
make note of while running the application:

*   Ajax push: This application makes use of ajax push.  When a member is created in one browser, the member list is
    updated in **all** open browsers.

    Try this yourself, by opening two different browsers, create a new member in one browser, and watch for the list to
    be updated in both browsers.

*   Ajax: All page updates are made with an ajax call, increasing the page responsiveness, and leading to a more native
    **feeling** application.

*   Client-side validation: By simply nesting a <rich:validator /> tag in the input elements, we wire them with
    RichFaces client-side validation capabilities.  The inputs are validated locally using javascript, without requiring
    a round-trip to the server.

*   Popups: Click the view link next to a member in the member list to view a popup with the member details.  To close
    the popup, click the "X" in the top right-hand corner, or click anywhere on the background mask.

*   Mobile support: view the application form a webkit powered browser on a mobile device to try out the mobile version
    of the application.  Alternatively, view the mobile version on your desktop by navigating to the url:
    <http://localhost:8080/jboss-as-kitchensink-rf/mobile/>

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better, and the RichFaces library.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. 
The following instructions target JBoss AS 7, but they also apply to JBoss Enterprise Application Platform 6.
 
With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------
 
First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6). To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-kitchensink-rf.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-kitchensink-rf/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.
 
Running the Arquillian tests
============================

By default, tests are configured to be skipped. The reason is that the sample
test is an Arquillian test, which requires the use of a container. You can
activate this test by selecting one of the container configuration provided 
for JBoss AS 7 (remote).

To run the test in JBoss AS 7, first start a JBoss AS 7 instance. Then, run the
test goal with the following profile activated:

    mvn clean test -Parq-jbossas-remote

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the 
JBoss AS 7 <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
