ejb-security:  Using JEE Declarative Security to Control Access to EJB 3
====================
Author: Sherif F. Makary

This example demonstrates the use of JEE declarative security to control access to EJB 3 and Security in JBoss AS7 and JBoss Enterprise Application Platform 6.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

The following are the steps required to implement EJB security:

1. Use the security domain `other` that is defined in the application server's default `standalone.xml`
2. Add the user `user` with password `password` belonging to the role `guest` and realm `ApplicationRealm`. For more information regarding how to add a user using the "add-user" utility, please refer to the README in the root of the quickstarts 
3. A security domain reference for the `other` security domain is added to `WEB-INF/jboss-web.xml`  
4. A security constraint is added to `WEB-INF/web.xml` 
5. Security annotations are added to the EJB declaration

Please note the allowed user role `guest` in the annotation -`@RolesAllowed`- is the same as the user role defined in step 2

For more information, refer to the  [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide).


## Deploying the Quickstart

First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using Windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive:

    mvn clean package


You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy the application to the running instance of JBoss AS.

## Testing the Quickstart

The application will be running at the following URL <http://localhost:8080/jboss-as-ejb-security/>.

When you access the application, you should get a browser login challenge.

After a successful login using `user`/`password`, the browser will display the following security info:

    Successfully called Secured EJB

    Principal : user
    Remote User : user
    Authentication Type : BASIC

Change the role in the quickstart `/src/main/webapp/WEB-INF/classes/roles.properties` files to `notauthorized`. 
Rebuild the application using by typing the following command:

    mvn clean package

Re-deploy the application by typing:

    mvn jboss-as:deploy

Refresh the browser, clear the active login, and you should get a security exception similar to the following: 

    HTTP Status 403 - Access to the requested resource has been denied

    type Status report
    message Access to the requested resource has been denied
    description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.
