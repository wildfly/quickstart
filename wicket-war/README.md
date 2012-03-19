wicket-war: Wicket Framework used in a WAR.
===========================================

Author: Ondrej Zizka <ozizka@redhat.com>



What is it?
-----------

This is an example of how to use Wicket Framework 1.5 with JBoss AS, leveraging features of Java EE 6, using the Wicket-Stuff Java EE integration.

Features used:

 * Injection of `@PersistenceContext`
 * Injection of a value from `web.xml` using `@Resource`
 * Injection of a stateless session bean using `@EJB`

This is a WAR version.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.



Start the JBoss Server
----------------------

 * Follow the instructions here to [Start the JBoss Server with the _web_ profile](../README.html#startserverweb)


Build and Deploy the Quickstart
-------------------------------

 * Running server is required before deploying.
 * To build and deploy the quickstart, follow the instruction here: [Build and Deploy the Quickstarts](../README.html/#buildanddeploy)


Access the application (For quickstarts that have a UI component)
----------------------

Access the running application in a browser at the following URL:  [http://localhost:8080/jboss-as-wicket-war](http://localhost:8080/jboss-as-wicket-war)

 * You will see a page with a table listing user entities. Initially, this table is empty.
 * By clicking a link, you can add more users.



Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, 
run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc
