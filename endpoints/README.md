How to run examples
===================

Endpoint examples show HotRod, REST and MemCached endpoints connecting to Enterprise Data Grid (EDG)
remotely and storing, retrieving and removing data from caches. All of the apps are variations of a simple 
Football Manager which is a console application. It is possible to add a team, players, remove 
all the entities and show the listing of all teams/players. Nothing complex, just showing basic 
operations with the cache.

Building and starting the application
-------------------------------------

1) Add the following cache definitions into infinispan-subsystem in 
   `${EDG_HOME}/standalone/configuration/standalone.xml`:
   
   `<local-cache name="memcachedCache" start="EAGER" batching="false" indexing="NONE">
       <locking isolation="REPEATABLE_READ" striping="false" acquire-timeout="20000" concurrency-level="500"/>
   </local-cache>
   <local-cache name="teams" start="EAGER" batching="false" indexing="NONE">
       <locking isolation="REPEATABLE_READ" striping="false" acquire-timeout="20000" concurrency-level="500"/>
   </local-cache>`

NOTE: The cache called "teams" will be used by HotRod and REST endpoints; Memcached endpoint works with "memcachedCache" by
      default

2) Each of the submodules (hotrod-endpoint, rest-endpoint, memcached-endpoint) contains a configuration file 
   (`src/main/resources/edg.properties`). Modify it to point to your EDG installation (default values should be fine
   for most cases)

3) Build the example application in its directory:

    mvn package

NOTE: This uses Maven's shade plugin which will bundle all dependencies into one jar file and so running the application is easier.

4) Start your EDG server instance:

    `${EDG_HOME}/bin/standalone.sh`

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

