How to run examples
===================

Endpoint examples show HotRod, REST and MemCached endpoints connecting to JBoss Data Grid (JDG)
remotely and storing, retrieving and removing data from caches. All of the apps are variations of a simple 
Football Manager which is a console application. It is possible to add a team, players, remove 
all the entities and show the listing of all teams/players. Nothing complex, just showing basic 
operations with the cache.

Building and starting the application
-------------------------------------

1) Alter JDG configuration file (`${JDG_HOME}/standalone/configuration/standalone.xml`) to contain
   the following definitions:
   
* Datasource subsystem definition:

    `<subsystem xmlns="urn:jboss:domain:datasources:1.0">
        <datasources>
            <datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
                <connection-url>jdbc:h2:mem:test;DB_CLOSE_DELAY=-1</connection-url>
                <driver>h2</driver>
                <security>
                    <user-name>sa</user-name>
                    <password>sa</password>
                </security>
            </datasource>
            <drivers>
                <driver name="h2" module="com.h2database.h2">
                    <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
                </driver>
            </drivers>
        </datasources>
    </subsystem>`

* Infinispan subsystem definition:

    `<subsystem xmlns="urn:jboss:domain:infinispan:1.2" default-cache-container="default">
        <cache-container name="default" default-cache="memcachedCache" listener-executor="infinispan-listener" start="EAGER">
            <local-cache 
                name="memcachedCache"
                start="EAGER"
                batching="false"
                indexing="NONE">
                <locking
                    isolation="REPEATABLE_READ"
                    acquire-timeout="20000"
                    concurrency-level="500"
                    striping="false" />
                <transaction mode="NONE" />
                <string-keyed-jdbc-store datasource="java:jboss/datasources/ExampleDS" passivation="false" preload="false" purge="false">
                    <property name="databaseType">H2</property>
                    <string-keyed-table prefix="JDG">
                        <id-column name="id" type="VARCHAR"/>
                        <data-column name="datum" type="BINARY"/>
                        <timestamp-column name="version" type="BIGINT"/>
                    </string-keyed-table>
                </string-keyed-jdbc-store>
            </local-cache>
            <local-cache 
                name="teams"
                start="EAGER"
                batching="false"
                indexing="NONE">
                <locking
                    isolation="REPEATABLE_READ"
                    acquire-timeout="20000"
                    concurrency-level="500"
                    striping="false" />
                <transaction mode="NONE" />
                <string-keyed-jdbc-store datasource="java:jboss/datasources/ExampleDS" passivation="false" preload="false" purge="false">
                    <property name="databaseType">H2</property>
                    <string-keyed-table prefix="JDG">
                        <id-column name="id" type="VARCHAR"/>
                        <data-column name="datum" type="BINARY"/>
                        <timestamp-column name="version" type="BIGINT"/>
                    </string-keyed-table>
                </string-keyed-jdbc-store>
            </local-cache>
        </cache-container>
    </subsystem>`


NOTE: The cache called "teams" will be used by HotRod and REST endpoints; Memcached endpoint works with "memcachedCache" by
      default

2) Each of the submodules (hotrod-endpoint, rest-endpoint, memcached-endpoint) contains a configuration file 
   (`src/main/resources/jdg.properties`). Modify it to point to your JDG installation (default values should be fine
   for most cases)

3) Build the example application in its directory:

    mvn package

NOTE: This uses Maven's shade plugin which will bundle all dependencies into one jar file and so running the application is easier.

4) Start your JDG server instance:

    `${JDG_HOME}/bin/standalone.sh`

5) Run the example application in its directory:

* for hotrod-endpoint: 

    `java -jar target/hotrod-endpoint-quickstart.jar`

* for rest-endpoint:

    `java -jar target/rest-endpoint-quickstart.jar`

* for memcached-endpoint:

    `java -jar target/memcached-endpoint-quickstart.jar`


Using the application
---------------------
Basic usage scenarios can look like this (keyboard shortcuts will be shown to you upon start):

1. add a team
2. add players to the team
3. print all teams and their players
4. remove a player from certain team
5. print again
6. remove a team
7. print again

