# spring-resteasy: Example Using Resteasy Spring Integration

Author: Weinan Li <l.weinan@gmail.com>, Paul Gier <pgier@redhat.com>  
Level: Beginner  
Technologies: Resteasy, Spring  
Summary: The `spring-resteasy` quickstart demonstrates how to package and deploy a web application that includes resteasy-spring integration.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `spring-resteasy` quickstart demonstrates how to package and deploy a web application, which includes resteasy-spring integration, in
${product.name.full}.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This deploys the `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL:  <http://localhost:8080/${project.artifactId}/>.

That will provide links to the following URLs that demonstrate various path and parameter configurations.

* [${project.artifactId}/hello?name=yourname](http://localhost:8080/${project.artifactId}/hello?name=yourname)
* [${project.artifactId}/basic](http://localhost:8080/${project.artifactId}/basic)
* [${project.artifactId}/queryParam?param=query](http://localhost:8080/${project.artifactId}/queryParam?param=query)
* [${project.artifactId}/matrixParam;param=matrix](http://localhost:8080/${project.artifactId}/matrixParam;param=matrix)
* [${project.artifactId}/uriParam/789](http://localhost:8080/${project.artifactId}/uriParam/789)

And the same set as above but using the `locating` path.

* [${project.artifactId}/locating/hello?name=yourname](http://localhost:8080/${project.artifactId}/locating/hello?name=yourname)
* [${project.artifactId}/locating/basic](http://localhost:8080/${project.artifactId}/locating/basic)
* [${project.artifactId}/locating/queryParam?param=query](http://localhost:8080/${project.artifactId}/locating/queryParam?param=query)
* [${project.artifactId}/locating/matrixParam;param=matrix](http://localhost:8080/${project.artifactId}/locating/matrixParam;param=matrix)
* [${project.artifactId}/locating/uriParam/789](http://localhost:8080/${project.artifactId}/locating/uriParam/789)


## Run the Tests

1. Make sure you have **started** the ${product.name} server as described above and **deployed** the quickstart.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn install -Prest-test

4. You should see the following output:

        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running org.jboss.as.quickstarts.resteasyspring.test.ResteasySpringTest
        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.211 sec

        Results :

        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------



## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy
