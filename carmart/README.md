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


Building and starting the application in library mode
-----------------------------------------------------

0) Obtain JDG distribution with productized Infinispan libraries

1) Install libraries from the bundle into your local maven repository

    `mvn initialize -Pinit-repo -Ddatagrid.dist=/path/to/datagrid/distribution`
    
2) Start JBoss AS 7 where your application will be running

    `$JBOSS_HOME/bin/standalone.sh`

3) Build and deploy the application (deployed via a maven plugin connected to the management interface of AS)

    `mvn clean install -Plocal`

4) Go to http://localhost:8080/carmart-quickstart


Building and starting the application in client-server mode (using HotRod client)
---------------------------------------------------------------------------------

0) Obtain JDG distribution with productized Infinispan libraries

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

5) In the example's directory:

    mvn clean install -Premote (deployed via a maven plugin connected to the management interface of AS)

6) Go to http://localhost:8080/carmart-quickstart

NOTE: The application must be deployed into JBoss AS7, not JDG, since JDG does not support deploying applications. 
