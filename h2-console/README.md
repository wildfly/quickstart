# h2-console: Example Using the H2 Console with ${product.name}

Author: Pete Muir  
Level: Beginner  
Technologies: H2  
Summary: The `h2-console` quickstart demonstrates how to use the H2 Console that is bundled with and built specifically for ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

${product.name.full} bundles H2 as an in-memory, in-process database. H2 is written in Java so it can run on any platform that ${product.name} runs on.

The `h2-console` quickstart comes bundled with a version of the H2 Console built for ${product.name}. To make the H2 console run on ${product.name}, the H2 libraries were removed from the WAR and a dependency on the H2 module was added to the `META-INF/MANIFEST.MF` file. The rebuilt console is provided in the root directory of this quickstart.

This quickstart demonstrates how to use the H2 console with ${product.name.full}. It uses the `greeter` quickstart as a GUI for entering data.

_Note: This quickstart uses the H2 database included with ${product.name.full} ${product.version}. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Prerequisites

This quickstart depends on the deployment of the `greeter` quickstart. Before running this quickstart, see the [greeter README](../greeter/README.md) file for details on how to deploy it.

You can verify the deployment of the `greeter` quickstart by accessing the following URL: <http://localhost:8080/${project.artifactId}/>

When you have completed testing this quickstart, see the [greeter README](../greeter/README.md) file for instructions to undeploy the archive.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Deploy the H2 Console

Deploy the console by copying the `h2console.war` located in the root directory of this quickstart to the `${jboss.home.name}/standalone/deployments` directory.

_Note:_ You will see the following warning in the server log. You can ignore this warning.

    WFLYSRV0019: Deployment "deployment.h2console.war" is using an unsupported module ("com.h2database.h2:main") which may be changed or removed in future versions without notice.

## Access the H2 Console

You can access the console at the following URL:  <http://localhost:8080/${project.artifactId}/>.

You need to enter the JDBC URL, and credentials. To access the `test` database that the `greeter` quickstart uses, enter these details:

* JDBC URL: `jdbc:h2:mem:greeter-quickstart;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1`
* User Name: `sa`
* Password: `sa`

Click on the *Test Connection* button to make sure you can connect. If you can, go ahead and click *Connect*.

## Investigate the H2 Console

Take a look at the data added by the `greeter` application. Run the following SQL command:

        select * from users;

You should see the two users seeded by the `greeter` quickstart, plus any users you added when testing that application.


## Undeploy the Archive

To undeploy this example, simply delete the `h2console.war` from the `${jboss.home.name}/standalone/deployments` directory.

     cd ${jboss.home.name}/standalone/deployments
     rm h2console.war
