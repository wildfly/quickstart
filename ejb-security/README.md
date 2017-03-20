# ejb-security:  Using Java EE Declarative Security to Control Access

Author: Sherif F. Makary  
Level: Intermediate  
Technologies: EJB, Security  
Summary: The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in ${product.name.full}.

This quickstart takes the following steps to implement EJB security:

1. Define the security domain. This can be done either in the `security` subsytem of the `standalone.xml` configuration file or in the `WEB-INF/jboss-web.xml` configuration file. This quickstart uses the `other` security domain, which is provided by default in the `standalone.xml` file.

        <security-domain name="other" cache-type="default">
            <authentication>
                <login-module code="Remoting" flag="optional">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
                <login-module code="RealmDirect" flag="required">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
        </security-domain>

2. Add the `@SecurityDomain("other")` security annotation to the EJB declaration to tell the EJB container to apply authorization to this EJB.
3. Add the `@RolesAllowed({ "guest" })` annotation to the EJB declaration to authorize access only to users with `guest` role access rights.
4. Add the `@RolesAllowed({ "guest" })` annotation to the Servlet declaration to authorize access only to users with `guest` role access rights.
5. Add a `<login-config>` security constraint to the `WEB-INF/web.xml` file to force the login prompt.
6. Add an application user with `guest` role access rights to the EJB. This quickstart defines a user `quickstartUser` with password `quickstartPwd1!` in the `guest` role. The `guest` role matches the allowed user role defined in the `@RolesAllowed` annotation in the EJB.
7. Add a second user that has no `guest` role access rights.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Add the Application Users

Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |
| user1 | ApplicationRealm | password1! | app-user |

The first application user has access rights to the application. The second application user is not authorized to access the application.

To add the application users, open a command prompt and type the following commands:

        For Linux:        
          ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          ${jboss.home.name}/bin/add-user.sh -a -u 'user1' -p 'password1!' -g 'app-user'

        For Windows:
          ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          ${jboss.home.name}\bin\add-user.bat -a -u 'user1' -p 'password1!' -g 'app-user'

If you prefer, you can use the add-user utility interactively.
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


## Start the Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

When you access the application, you are presented with a browser login challenge.

1. If you attempt to login with a user name and password combination that has not been added to the server, the login challenge will be redisplayed.
2. When you login successfully using `quickstartUser`/`quickstartPwd1!`, the browser displays the following security info:

        Successfully called Secured EJB

        Principal : quickstartUser
        Remote User : quickstartUser
        Authentication Type : BASIC

3. Now close and reopen the brower session and access the application using the `user1`/`password1!` credentials. In this case, the Servlet, which only allows the `guest` role, restricts the access and you get a security exception similar to the following:

        HTTP Status 403 - Access to the requested resource has been denied

        type Status report
        message Access to the requested resource has been denied
        description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.

4. Next, change the EJB (SecuredEJB.java) to a different role, for example, `@RolesAllowed({ "other-role" })`. Do not modify the `guest` role in the Servlet (SecuredEJBServlet.java). Build and redeploy the quickstart, then close and reopen the browser and login using `quickstartUser`/`quickstartPwd1!`. This time the Servlet will allow the `guest` access, but the EJB, which only allows the role `other-role`, will throw an EJBAccessException:

        HTTP Status 500

        message
        description  The server encountered an internal error () that prevented it from fulfilling this request.
        exception
        javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public java.lang.String org.jboss.as.quickstarts.ejb_security.SecuredEJB.getSecurityInfo() of bean: SecuredEJB is not allowed



## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to [Add the Application Users](#add-the-application-users) as described above.
* To deploy the server project, right-click on the `${project.artifactId}` project and choose `Run As` --> `Run on Server`.
* You are presented with a browser login challenge. Enter the credentials as described above to access and test the running application.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
