How to run the example
======================

CarMart is a simple web application that uses Infinispan instead of a relational database.
Users can list cars, add new cars or remove them from the CarMart. Information about each car
is stored in a cache. The application also shows cache statistics like stores, hits, retrievals, etc.

The CarMart quickstart can work in two modes: "library" and "client-server". In library mode, 
all libraries (jar files) are bundled with the application and deployed into the server. Caches are
configured programatically and run in the same JVM as the web application. In client-server mode, 
the web application bundles only HotRod client and communicates with a remote JBoss Data Grid (JDG) server. 
The JDG server is configured via standalone.xml configuration file.


Building and deploying to JBoss AS 7
------------------------------------

0) Obtain JDG distribution with productized Infinispan libraries (library distribution)

1) Install libraries from the bundle into your local maven repository

    `mvn initialize -Pinit-repo -Ddatagrid.dist=/home/anyuser/jboss-datagrid-library-6.0.0.ER4-redhat-1`
    
2) Start JBoss AS 7 where your application will run

    `$JBOSS_HOME/bin/standalone.sh`

3) Build the application

    `mvn clean package -Plibrary-jbossas`

4) Deploy the application via jboss-as Maven plugin

    `mvn jboss-as:deploy -Plibrary-jbossas`

5) Go to http://localhost:8080/carmart-quickstart

6) Undeploy the application

    `mvn jboss-as:undeploy -Plibrary-jbossas`


Building and deploying to Tomcat 7
----------------------------------

0) Obtain JDG distribution with productized Infinispan libraries (library distribution)

1) Install libraries from the bundle into your local maven repository

    `mvn initialize -Pinit-repo -Ddatagrid.dist=/home/anyuser/jboss-datagrid-library-6.0.0.ER4-redhat-1`

2) This build assumes you will be running Tomcat 7 in its default
   configuration, with a hostname of localhost and port 8080. Before starting
   Tomcat, add the following lines to `conf/tomcat-users.xml` to allow the Maven
   Tomcat plugin to access the manager application:

    <role rolename="manager-script"/>
    <user username="admin" password="" roles="manager-script"/>
    
3) Start Tomcat 7

    `$CATALINA_HOME/bin/catalina.sh start`

4) Build the application

    `mvn clean package -Plibrary-tomcat`

5) Add a `<server>` element into your Maven settings.xml with `<id>` equal to `tomcat` and correct credentials:

    `<server>
         <id>tomcat</id>
         <username>admin</username>
         <password></password>
     </server>`

6) Deploy the application via tomcat Maven plugin

    `mvn tomcat:deploy -Plibrary-tomcat`

7) Go to http://localhost:8080/carmart-quickstart

8) Undeploy the application

    `mvn tomcat:undeploy -Plibrary-tomcat`


Building and starting the application in client-server mode (using HotRod client)
---------------------------------------------------------------------------------

0) Obtain JDG server distribution

1) Add the following configuration to your `$JDG_HOME/standalone/configuration/standalone.xml` to configure
   remote datagrid

    `<paths>
        <path name="temp" path="/tmp"/>
     </paths>`
    
    ...right after `</system-properties>` tag

    `<local-cache name="carcache" start="EAGER" batching="false" indexing="NONE">
        <locking isolation="REPEATABLE_READ" striping="false" acquire-timeout="20000" concurrency-level="500"/>
        <eviction strategy="LIRS" max-entries="4"/>
        <file-store relative-to="temp" path="carstore" passivation="false"/>
     </local-cache>`
    
    ...into infinispan sybsystem
   
2) Start the JDG server (this server is supposed to run on test1 address)
    
    `$JDG_HOME/bin/standalone.sh`

3) Start JBoss AS 7 into which you want to deploy your application

    `$JBOSS_HOME/bin/standalone.sh`

4) Edit src/main/resources/META-INF/JDG.properties file and specify address of the JDG server

    datagrid.address=test1

5) Build the application in the example's directory:

    `mvn clean package -Premote`

6) Deploy the application

    `mvn jboss-as:deploy -Premote`

7) Go to http://localhost:8080/carmart-quickstart

8) Undeploy the application

    `mvn jboss-as:undeploy -Premote`

NOTE: The application must be deployed into JBoss AS7, not JDG, since JDG does not support deploying applications. 
