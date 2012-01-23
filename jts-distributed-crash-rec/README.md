jts-distributed-crash-rec: JTS and distributed crash recovery 
=============================================================
Author: Tom Jenkinson


Pre-requisites
--------------

Developers should be familiar with the concepts introduced in the following quickstarts:

* _cmt_
* _jts_

IMPORTANT: This quickstart is unusual in that it depends on the application components in the _jts_quickstart. Please follow that quickstart first and then pay particular attention to this file in order to identify steps referred to in the _jts_ quickstart.

What is it?
-----------

This quickstart works through a distributed crash recovery scenario. 

Crash recovery is a key feature provided by an application server and ensures an application's data consistency even in the presence of failure. Providing reliable crash recovery helps qualify the pedigree of an application server, distributed crash recovery even more so.

The quickstart uses the application components from the JTS quickstart. We provide a byteman rule (see below for download details) to inject a halt into the application server at a crucial point in the phase2commit of the transaction. Byteman is used solely to raise an artificial fault, an IDE could simulate this too although it is more complex to use an IDE for this purpose.

Apart from that the quickstart works the same as the JTS quickstart and if this byteman rule is left out, the quickstart _is_ the JTS quickstart.

As an overview, the sequence of events to expect:

1. Configure and start two application servers as below
2. Build and deploy the two application components as below
3. Open a web browser and attempt to invoice two customers as with the JTS quickstart
4. Application server 1 will run through 2PC preparing the resources in AS1 and AS2. Application server 1 will then crash before it can call commit
5. The user is invited to inspect the content of the object store
6. Application server 1 should be restarted, which will run through recover and the "invoices" delivered to the MDBs as happens in the JTS quickstart


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

This quickstart also makes use of Byteman to create a crash within the application server. Byteman can be downloaded from: <http://www.jboss.org/byteman/downloads/>. Once downloaded, extract Byteman to a location on your harddrive.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. 

IMPORTANT: If you are running this quickstart on OpenShift, the Byteman root folder will need its permissions changing as Ruby does not accept world readable folders:

    chmod -R o-rwx byteman-download-2.0.0/


Configuring the application servers
-----------------------------------

If you haven't already done so, follow the instructions in the _jts_ quickstart to configure two application servers.

### Configuring Byteman to crash application server 1

We now need to install a rule into application server 1 that will crash the server when it has prepared the XA resources.

To do this:

1. Backup the file `<APP_SERVER_1_HOME>/bin/standalone.conf` (`standalone.conf.bat` on Windows)
2. Open the file `<APP_SERVER_1_HOME>/bin/standalone.conf` (`standalone.conf.bat` on Windows)
3. Append the following text to the end of the file:
   NOTE: You must replace /home/tom/projects/jbossas/quickstart/jts-distributed-crash-rec with the location where this quickstart resides
   NOTE: You must replace /home/tom/byteman-download-2.0.0 with the location of <BYTEMAN_HOME>

        JAVA_OPTS="-javaagent:/home/user/lib/byteman.jar=script:/home/user/quickstart/jts-distributed-crash-rec/byteman-scripts/failAfterPrepare.btm ${JAVA_OPTS}"
        JAVA_OPTS="-Dorg.jboss.byteman.transform.all -Djboss.modules.system.pkgs=org.jboss.byteman -Dorg.jboss.byteman.verbose=true ${JAVA_OPTS}"

4. IMPORTANT: After you have finished with the quickstart, it is important to restore your backup from step 1 above thereby removing the Byteman configuration from the `standalone.conf[.bat]` file. Otherwise your server will always crash when a transaction commits!

### Starting the servers

Start JBoss AS 7 or EAP 6 with an XA JMS connection factory in it.

For JBoss on Linux:

    <APP_SERVER_1_HOME>/bin/standalone.sh -c standalone-full.xml
    <APP_SERVER_2_HOME>/bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100

or if you are using windows

    <APP_SERVER_1_HOME>\bin\standalone.bat -c standalone-full.xml
    <APP_SERVER_2_HOME>\bin\standalone.bat -c standalone-full.xml -Djboss.socket.binding.port-offset=100

### Building and deploying the application components 

If you haven't done so already, follow the instructions in the _jts_quickstart to deploy two distributed transaction aware application components.


## Testing the application

Visit the application URL <http://localhost:8080/jboss-as-jts-application-component-1/>

When you enter a name and click to "add" that customer, you will see the following in the application server 1 console:

    15:46:55,070 INFO  [org.jboss.ejb.client] (http-localhost-127.0.0.1-8080-1) JBoss EJB Client version 1.0.0.Beta12
    15:46:55,658 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Rule.execute called for Fail 2PC after prepare_0
    15:46:55,665 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) HelperManager.install for helper classorg.jboss.byteman.rule.helper.Helper
    15:46:55,666 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) calling activated() for helper classorg.jboss.byteman.rule.helper.Helper
    15:46:55,666 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Default helper activated
    15:46:55,667 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) calling installed(Fail 2PC after prepare) for helper classorg.jboss.byteman.rule.helper.Helper
    15:46:55,667 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Installed rule using default helper : Fail 2PC after prepare
    15:46:55,668 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) Fail 2PC after prepare execute
    15:46:55,669 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) rule.debug{Fail 2PC after prepare} : !!!killing JVM!!!

NOTE: Until you restart app server 1, you will see several error messages in app server 2, these are to be expected:

    15:46:55,044 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.0.Beta12
    15:49:06,579 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022167: Got TRANSIENT from ORB for tx 0:ffffc0a8013c:-2eb1158b:4f280ce3:1a, unable determine status, will retry later
    15:51:19,103 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022167: Got TRANSIENT from ORB for tx 0:ffffc0a8013c:-2eb1158b:4f280ce3:1a, unable determine status, will retry later
    15:51:19,120 WARN  [com.arjuna.ats.jta] (Periodic Recovery) ARJUNA016005: JTS XARecoveryModule.xaRecovery - failed to recover XAResource. status is $3
    15:53:31,638 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022167: Got TRANSIENT from ORB for tx 0:ffffc0a8013c:-2eb1158b:4f280ce3:1a, unable determine status, will retry later
    15:53:31,644 WARN  [com.arjuna.ats.jta] (Periodic Recovery) ARJUNA016005: JTS XARecoveryModule.xaRecovery - failed to recover XAResource. status is $3

After the server is crashed you should also be able to view the contents of the object store:

    tree server*/standalone/data/tx-object-store

which should display

    server1/standalone/data/tx-object-store
    `-- ShadowNoFileLockStore
        `-- defaultStore
            |-- CosTransactions
            |   `-- XAResourceRecord
            |       `-- 0_ffffc0a8013c_38e104bd_4f280cdb_1d
            |-- Recovery
            |   `-- FactoryContact
            |       |-- 0_ffffc0a8013c_38e104bd_4f280cdb_17
            |       |-- 0_ffffc0a8013c_-671009a_4f280e7e_17
            |       `-- 0_ffffc0a8013c_6d5d82b5_4f280a16_f
            |-- RecoveryCoordinator
            |   `-- 0_ffff52e38d0c_c91_4140398c_0
            `-- StateManager
                `-- BasicAction
                    `-- TwoPhaseCoordinator
                        `-- ArjunaTransactionImple
                            `-- 0_ffffc0a8013c_38e104bd_4f280cdb_19
    server2/standalone/data/tx-object-store
    `-- ShadowNoFileLockStore
        `-- defaultStore
            |-- CosTransactions
            |   `-- XAResourceRecord
            |       `-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_1e
            |-- Recovery
            |   `-- FactoryContact
            |       |-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_18
            |       `-- 0_ffffc0a8013c_4f6459f0_4f280a24_f
            |-- RecoveryCoordinator
            |   `-- 0_ffff52e38d0c_c91_4140398c_0
            `-- StateManager
                `-- BasicAction
                    `-- TwoPhaseCoordinator
                        `-- ArjunaTransactionImple
                            `-- ServerTransaction
                                `-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_1a


You now need to restart server 1 and wait for recovery to complete. Follow the steps above to start the server.

_IMPORTANT:_ By default, the recovery process checks the transactional state every two minutes, therefore it can take a while for recovery to happen. Also recovery for each server will take place at its own recovery interval.

You will know when recovery is complete for server 2 as you will see the following in application-server-2 console:

    12:09:38,697 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.0.Beta11
    12:09:39,204 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-3 (group:HornetQ-client-global-threads-649946595)) Received Message: Created invoice for customer named: Tom


NOTE: You will also get several stack traces in app server 1 console during recovery, these are to be expected as not all resources are available at all stages of recovery.

    15:55:41,706 WARN  [com.arjuna.ats.jts] (Thread-84) ARJUNA022223: ExtendedResourceRecord.topLevelCommit caught exception: org.omg.CORBA.OBJECT_NOT_EXIST: Server-side Exception: unknown oid
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) [:1.6.0_22]
        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57) [:1.6.0_22]
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) [:1.6.0_22]
        at java.lang.reflect.Constructor.newInstance(Constructor.java:532) [:1.6.0_22]
        at org.jacorb.orb.SystemExceptionHelper.read(SystemExceptionHelper.java:223) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.jacorb.orb.ReplyReceiver.getReply(ReplyReceiver.java:319) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.jacorb.orb.Delegate.invoke_internal(Delegate.java:1090) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.jacorb.orb.Delegate.invoke(Delegate.java:957) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.omg.CORBA.portable.ObjectImpl._invoke(ObjectImpl.java:80) [jacorb-2.3.1.jbossorg-1.jar:]
        at com.arjuna.ArjunaOTS._ArjunaSubtranAwareResourceStub.commit(_ArjunaSubtranAwareResourceStub.java:252) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.resources.ExtendedResourceRecord.topLevelCommit(ExtendedResourceRecord.java:502) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2753) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2669) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.arjuna.coordinator.BasicAction.phase2Commit(BasicAction.java:1804) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.RecoveredTransaction.replayPhase2(RecoveredTransaction.java:197) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.TransactionCache.replayPhase2(TransactionCache.java:233) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.CachedRecoveredTransaction.replayPhase2(CachedRecoveredTransaction.java:173) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.RecoveredTransactionReplayer.run(RecoveredTransactionReplayer.java:118) [jbossjts-4.16.1.Final.jar:]

    15:55:55,179 WARN  [com.arjuna.ats.jts] (Periodic Recovery) ARJUNA022223: ExtendedResourceRecord.topLevelCommit caught exception: org.omg.CORBA.OBJECT_NOT_EXIST: Server-side Exception: unknown oid
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) [:1.6.0_22]
        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57) [:1.6.0_22]
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) [:1.6.0_22]
        at java.lang.reflect.Constructor.newInstance(Constructor.java:532) [:1.6.0_22]
        at org.jacorb.orb.SystemExceptionHelper.read(SystemExceptionHelper.java:223) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.jacorb.orb.ReplyReceiver.getReply(ReplyReceiver.java:319) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.jacorb.orb.Delegate.invoke_internal(Delegate.java:1090) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.jacorb.orb.Delegate.invoke(Delegate.java:957) [jacorb-2.3.1.jbossorg-1.jar:]
        at org.omg.CORBA.portable.ObjectImpl._invoke(ObjectImpl.java:80) [jacorb-2.3.1.jbossorg-1.jar:]
        at com.arjuna.ArjunaOTS._ArjunaSubtranAwareResourceStub.commit(_ArjunaSubtranAwareResourceStub.java:252) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.resources.ExtendedResourceRecord.topLevelCommit(ExtendedResourceRecord.java:502) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2753) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.arjuna.coordinator.BasicAction.doCommit(BasicAction.java:2669) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.arjuna.coordinator.BasicAction.phase2Commit(BasicAction.java:1804) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.RecoveredTransaction.replayPhase2(RecoveredTransaction.java:197) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.TransactionCache.replayPhase2(TransactionCache.java:233) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.CachedRecoveredTransaction.replayPhase2(CachedRecoveredTransaction.java:173) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.TransactionRecoveryModule.recoverTransaction(TransactionRecoveryModule.java:217) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.TransactionRecoveryModule.periodicWorkSecondPass(TransactionRecoveryModule.java:161) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.jts.recovery.transactions.TopLevelTransactionRecoveryModule.periodicWorkSecondPass(TopLevelTransactionRecoveryModule.java:81) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.arjuna.recovery.PeriodicRecovery.doWorkInternal(PeriodicRecovery.java:789) [jbossjts-4.16.1.Final.jar:]
        at com.arjuna.ats.internal.arjuna.recovery.PeriodicRecovery.run(PeriodicRecovery.java:371) [jbossjts-4.16.1.Final.jar:]
	
The easiest way to check when app server 1 is recovered is to look in the object store and check that all the records are now cleaned up. The records that should be cleared are the ones in the defaultStore/CosTransactions/XAResourceRecord and defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/ArjunaTransactionImple. 
Records will remain in defaultStore/Recovery/FactoryContact and defaultStore/RecoveryCoordinator and that is to be expected. Run:

    tree server*/standalone/data/tx-object-store

You should see this output:

    server1/standalone/data/tx-object-store
    `-- ShadowNoFileLockStore
        `-- defaultStore
            |-- CosTransactions
            |   `-- XAResourceRecord
            |-- Recovery
            |   `-- FactoryContact
            |       |-- 0_ffffc0a8013c_38e104bd_4f280cdb_17
            |       |-- 0_ffffc0a8013c_-671009a_4f280e7e_17
            |       `-- 0_ffffc0a8013c_6d5d82b5_4f280a16_f
            |-- RecoveryCoordinator
            |   `-- 0_ffff52e38d0c_c91_4140398c_0
            `-- StateManager
                `-- BasicAction
                    `-- TwoPhaseCoordinator
                       `-- ArjunaTransactionImple
    server2/standalone/data/tx-object-store
    `-- ShadowNoFileLockStore
        `-- defaultStore
            |-- CosTransactions
            |   `-- XAResourceRecord
            |-- Recovery
            |   `-- FactoryContact
            |       |-- 0_ffffc0a8013c_-2eb1158b_4f280ce3_18
            |       `-- 0_ffffc0a8013c_4f6459f0_4f280a24_f
            |-- RecoveryCoordinator
            |   `-- 0_ffff52e38d0c_c91_4140398c_0
            `-- StateManager
                `-- BasicAction
                    `-- TwoPhaseCoordinator
                        `-- ArjunaTransactionImple
                            `-- ServerTransaction


After recovery is complete, if you visit the application URL <http://localhost:8080/jboss-as-jts-application-component-1/customers.jsf> then you should see the user that you created is now in the list.

Don't forget to restore your standalone.conf[.bat] file to ensure that the byteman rule which crashes the server is removed or your application server will no longer be able to commit 2PC transactions!
