ejb-security:  Using JEE Declarative Security to Control Access to EJB 3
====================
Author: Sherif F. Makary

This example demonstrates the use of JEE declarative security to control access to EJB 3 and Security in JBoss AS7 and JBoss Enterprise Application Platform 6.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

The following are the steps required to implement EJB security:

1. The application will use a security domain that is defined in the application server standalone.xml that is called "other"
2. Add a user called "UserA" with password = "password" and belongs to a role called "gooduser" and realm "ApplicationRealm", for more information regarding how to add a user using the "add-user" utility, please refer to the quick starts root readme.md file 
3. A security-domain reference for the "other" security domain is added to /webapp/WEB-INF/jboss-web.xml, please note, jboss-web.xml is used for WAR packaging, if you would like to package the EJB in a JAR you would need to use jboss-ejb.xml instead  
4. A security-constraints is added to the /webapp/WEB-INF/web.xml 
5. Security annotations are added to the EJB declaration
Please note the allowed user role "gooduser" in the annotation -`@RolesAllowed`- is the same as the user role defined in step 2

For more information, refer to the  <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> and find Security --> EJB3 Security.


## Deploying the Quickstart

First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using Windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive:

    mvn clean package


You can now deploy the artifact to JBoss AS by executing the following command:

                mvn jboss-as:deploy

This will deploy `target/jboss-as-ejb-security` to the running instance of JBoss AS.

## Testing the Quickstart

The application will be running at the following URL <http://localhost:8080/jboss-as-ejb-security/>.

When you access the application, you should get a browser login challenge.

After a successful login using admin/admin, the browser will display the following security info:

                Successfully called Secured EJB

                Principal : admin
                Remote User : admin
                Authentication Type : BASIC

Change the role in the quickstart /src/main/webapp/WEB-INF/classes/roles.properties files to 'gooduser1'. 
Rebuild the application using by typing the following command:

                mvn clean package

Re-deploy the application by typing:

                mvn jboss-as:deploy

Refresh the browser, clear the active login, and you should get a security exception similar to the following: 

                HTTP Status 403 - Access to the requested resource has been denied

                type Status report
                message Access to the requested resource has been denied
                description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.
