# WildFly Application Server (WildFly) Quickstarts

Summary: The quickstarts demonstrate Java EE 7 and a few additional technologies from the JBoss stack. They provide small, specific, working examples that can be used as a reference for your own project.


## Introduction

These quickstarts run on WildFly Application Server 11 or later. We recommend using the WildFly ZIP file. This version uses the correct dependencies and ensures you test and compile against your runtime environment.

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

* [Available Quickstarts](#available-quickstarts): List of the available quickstarts and details about each one.

* [Suggested Approach to the Quickstarts](#suggested-approach-to-the-quickstarts): A suggested approach on how to work with the quickstarts.

* [System Requirements](#system-requirements): List of software required to run the quickstarts.

* [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts): How to configure the Maven repository for use by the quickstarts.

* [Run the Quickstarts](#run-the-quickstarts): General instructions for building, deploying, and running the quickstarts.

* [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests): How to run the Arquillian tests provided by some of the quickstarts.

* [Optional Components](#optional-components): How to install and configure optional components required by some of the quickstarts.

* [Contributing Guide](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONTRIBUTING.md#jboss-developer-contributing-guide): This document contains information targeted for developers who want to contribute to JBoss developer projects.

## Use of WILDFLY_HOME and JBOSS_HOME Variables

The quickstart README files use the *replaceable* value `WILDFLY_HOME` to denote the path to the WildFly installation. When you encounter this value in a README file, be sure to replace it with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_WILDFLY_HOME.md#use-of-eap_home-and-jboss_home-variables).


## Available Quickstarts

All available quickstarts can be found here: <http://www.jboss.org/developer-materials/#!formats=jbossdeveloper_quickstart>. You can filter by the quickstart name, the product, and the technologies demonstrated by the quickstart. You can also limit the results based on skill level and date published. The resulting page provides a brief description of each matching quickstart, the skill level, and the technologies used. Click on the quickstart to see more detailed information about how to run it. Some quickstarts require deployment of other quickstarts. This information is noted in the `Prerequisites` section of the quickstart README file.

_Note_: Some of these quickstarts use the H2 database included with WildFly. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!


| *Quickstart Name* | *Demonstrated Technologies* | *Description* | *Experience Level Required* | *Prerequisites* |
| --- | --- | --- | --- | --- |
| [app-client](app-client/README.md) |EJB, EAR, AppClient | The `app-client` quickstart demonstrates how to code and package a client app and use the WildFly client container to start the client Main program. | Intermediate |  |
| [batch-processing](batch-processing/README.md) |CDI, Batch 1.0, JSF | The `batch-processing` quickstart shows how to use chunk oriented batch jobs to import a file to a database. | Intermediate |  |
| [bean-validation](bean-validation/README.md) |CDI, JPA, BV | The `bean-validation` quickstart provides Arquillian tests to demonstrate how to use CDI, JPA, and Bean Validation. | Beginner |  |
| [bean-validation-custom-constraint](bean-validation-custom-constraint/README.md) |CDI, JPA, BV | The `bean-validation-custom-constraint` quickstart demonstrates how to use the Bean Validation API to define custom constraints and validators. | Beginner |  |
| [bmt](bmt/README.md) |EJB, BMT | The `bmt` quickstart demonstrates Bean-Managed Transactions (BMT), showing how to manually manage transaction demarcation while accessing JPA entities. | Intermediate |  |
| [cdi-alternative](cdi-alternative/README.md) |CDI, Servlet, JSP | The `cdi-alternative` quickstart demonstrates how to create a bean that can be implemented for different purposes without changing the source code. | Intermediate |  |
| [cdi-decorator](cdi-decorator/README.md) |CDI, JSF | The `cdi-decorator` quickstart demonstrates the use of a CDI Decorator to intercept bean methods and modify the business logic. | Intermediate |  |
| [cdi-injection](cdi-injection/README.md) |CDI, JSF | The `cdi-injection` quickstart demonstrates the use of *CDI Injection and Qualifiers* in WildFly with a JSF front-end client. | Beginner |  |
| [cdi-interceptors](cdi-interceptors/README.md) |JPA, JSF, EJB | The `cdi-interceptors` quickstart demonstrates how to use CDI interceptors for cross-cutting concerns such as logging and simple auditing. | Intermediate |  |
| [cdi-portable-extension](cdi-portable-extension/README.md) |CDI | The `cdi-portable-extension` quickstart demonstrates a simple CDI Portable Extension that uses SPI classes to inject beans with data from an XML file. | Intermediate |  |
| [cdi-stereotype](cdi-stereotype/README.md) |JPA, JSF, EJB | The `cdi-stereotype` quickstart demonstrates how to apply CDI stereotypes to beans to encapsulate CDI interceptor bindings and CDI alternatives. | Intermediate |  |
| [cdi-veto](cdi-veto/README.md) |CDI | The `cdi-veto` quickstart is a simple CDI Portable Extension that uses SPI classes to show how to remove beans and inject JPA entities into an application. | Intermediate |  |
| [cmt](cmt/README.md) |EJB, CMT, JMS | The `cmt` quickstart demonstrates Container-Managed Transactions (CMT), showing how to use transactions managed by the container. | Intermediate |  |
| [contacts-jquerymobile](contacts-jquerymobile/README.md) |jQuery Mobile, jQuery, JavaScript, HTML5, REST | The `contacts-jquerymobile` quickstart demonstrates a Java EE 7 mobile database application using HTML5, jQuery Mobile, JAX-RS, JPA, and REST. | Beginner |  |
| [deltaspike-authorization](deltaspike-authorization/README.md) |JSF, CDI, Deltaspike | Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike | Beginner |  |
| [deltaspike-beanbuilder](deltaspike-beanbuilder/README.md) |CDI, DeltaSpike | Shows how to create new beans using DeltaSpike utilities. | Advanced |  |
| [deltaspike-projectstage](deltaspike-projectstage/README.md) |JSF, CDI, Deltaspike | Demonstrate usage of DeltaSpike project stage and shows usage of a conditional @Exclude | Beginner |  |
| [ejb-asynchronous](ejb-asynchronous/README.md) |Asynchronous EJB | The `ejb-asynchronous` quickstart demonstrates the behavior of asynchronous EJB invocations by a deployed EJB and a remote client and how to handle errors. | Advanced |  |
| [ejb-in-ear](ejb-in-ear/README.md) |EJB, EAR | The `ejb-in-ear` quickstart demonstrates how to deploy an EAR archive that contains a *JSF* WAR and an *EJB* JAR. | Intermediate |  |
| [ejb-in-war](ejb-in-war/README.md) |EJB, JSF, WAR | The `ejb-in-war` quickstart demonstrates how to package an *EJB* bean in a WAR archive and deploy it to WildFly. Arquillian tests are also provided. | Intermediate |  |
| [ejb-multi-server](ejb-multi-server/README.md) |EJB, EAR | The `ejb-multi-server` quickstart shows how to communicate between multiple applications deployed to different servers using an EJB to log the invocation. | Advanced |  |
| [ejb-remote](ejb-remote/README.md) |EJB, JNDI | The `ejb-remote` quickstart uses *EJB* and *JNDI* to demonstrate how to access an EJB, deployed to WildFly, from a remote Java client application. | Intermediate |  |
| [ejb-security](ejb-security/README.md) |EJB, Security | The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in WildFly. | Intermediate |  |
| [ejb-security-context-propagation](ejb-security-context-propagation/README.md) |EJB, Security | The `ejb-security-context-propagation` quickstart demonstrates how the security context can be propagated to a remote EJB using a remote outbound connection configuration | Advanced |  |
| [ejb-security-interceptors](ejb-security-interceptors/README.md) |EJB, Security | The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call. | Advanced |  |
| [ejb-security-jaas](ejb-security-jaas/README.md) |EJB, Security | The `ejb-security-jaas` quickstart demonstrates how legacy `JAAS` security domains can be used in conjunction with `Elytron` | Intermediate |  |
| [ejb-security-programmatic-auth](ejb-security-programmatic-auth/README.md) |EJB, Security | The `ejb-security-programmatic-auth` quickstart demonstrates how to programmatically setup different identities when | Intermediate |  |
| [ejb-throws-exception](ejb-throws-exception/README.md) |EJB, EAR | The `ejb-throws-exception` quickstart demonstrates how to throw and handle Exceptions across JARs in an EAR. | Intermediate |  |
| [ejb-timer](ejb-timer/README.md) |EJB Timer | The `ejb-timer` quickstart demonstrates how to use the EJB timer service `@Schedule` and `@Timeout` annotations with WildFly. | Beginner |  |
| [forge-from-scratch](forge-from-scratch/README.md) |Forge | The `forge-from-scratch` quickstart demonstrates how *JBoss Forge* can generate a Java EE (JPA, EJB, JAX-RS, JSF) web-enabled database application. | Intermediate |  |
| [greeter](greeter/README.md) |CDI, JSF, JPA, EJB, JTA | The `greeter` quickstart demonstrates the use of *CDI*, *JPA*, *JTA*, *EJB* and *JSF* in WildFly. | Beginner |  |
| [h2-console](h2-console/README.md) |H2 | The `h2-console` quickstart demonstrates how to use the H2 Console that is bundled with and built specifically for WildFly. | Beginner |  |
| [ha-singleton-deployment](ha-singleton-deployment/README.md) |EJB, Singleton Deployments, Clustering | The `ha-singleton-deployment` quickstart demonstrates the recommended way to deploy any service packaged in an application archive as a cluster-wide singleton. | Advanced |  |
| [ha-singleton-service](ha-singleton-service/README.md) |MSC, Singleton Service, Clustering | The `ha-singleton-service` quickstart demonstrates how to deploy a cluster-wide singleton MSC service. | Advanced |  |
| [helloworld](helloworld/README.md) |CDI, Servlet | The `helloworld` quickstart demonstrates the use of *CDI* and *Servlet 3* and is a good starting point to verify WildFly is configured correctly. | Beginner |  |
| [helloworld-classfiletransformer](helloworld-classfiletransformer/README.md) | | null | null |  |
| [helloworld-html5](helloworld-html5/README.md) |CDI, JAX-RS, HTML5 | The `helloworld-html5` quickstart demonstrates the use of *CDI 1.2* and *JAX-RS 2.0* using the HTML5 architecture and RESTful services on the backend. | Beginner |  |
| [helloworld-jms](helloworld-jms/README.md) |JMS | The `helloworld-jms` quickstart demonstrates the use of external JMS clients with WildFly. | Intermediate |  |
| [helloworld-mbean](helloworld-mbean/README.md) |CDI, JMX, MBean | The `helloworld-mbean` quickstart demonstrates the use of *CDI* and *MBean* in WildFly and includes JConsole instructions and Arquillian tests. | Intermediate |  |
| [helloworld-mdb](helloworld-mdb/README.md) |JMS, EJB, MDB | The `helloworld-mdb` quickstart uses *JMS* and *EJB Message-Driven Bean* (MDB) to create and deploy JMS topic and queue resources in WildFly. | Intermediate |  |
| [helloworld-mdb-propertysubstitution](helloworld-mdb-propertysubstitution/README.md) |JMS, EJB, MDB | The `helloworld-mdb-propertysubstitution` quickstart demonstrates the use of *JMS* and *EJB MDB*, enabling property substitution with annotations. | Intermediate |  |
| [helloworld-mutual-ssl](helloworld-mutual-ssl/README.md) |Mutual SSL, Undertow | The `helloworld-mutual-ssl` quickstart is a basic example that demonstrates mutual SSL configuration in WildFly | Intermediate |  |
| [helloworld-mutual-ssl-secured](helloworld-mutual-ssl-secured/README.md) |Mutual SSL, Security, Undertow | The `helloworld-mutual-ssl-secured` quickstart demonstrates securing a Web application using client mutual SSL authentication and role-based access control | Intermediate |  |
| [helloworld-rf](helloworld-rf/README.md) |CDI, JSF, RichFaces | Similar to the helloworld quickstart, but with a JSF front end | Beginner |  |
| [helloworld-rs](helloworld-rs/README.md) |CDI, JAX-RS | The `helloworld-rs` quickstart demonstrates a simple Hello World application, bundled and deployed as a WAR, that uses *JAX-RS* to say Hello. | Intermediate |  |
| [helloworld-singleton](helloworld-singleton/README.md) |EJB, Singleton | The `helloworld-singleton` quickstart demonstrates an *EJB Singleton Bean* that is instantiated once and maintains state for the life of the session. | Beginner |  |
| [helloworld-ssl](helloworld-ssl/README.md) |SSL, Undertow | The `helloworld-ssl` quickstart is a basic example that demonstrates server side SSL configuration in WildFly. | Beginner |  |
| [helloworld-ws](helloworld-ws/README.md) |JAX-WS | The `helloworld-ws` quickstart demonstrates a simple Hello World application, bundled and deployed as a WAR, that uses *JAX-WS* to say Hello. | Beginner |  |
| [hibernate](hibernate/README.md) |Hibernate | The `hibernate` quickstart demonstrates how to use Hibernate ORM 5 API over JPA, using Hibernate-Core and Hibernate Bean Validation, and EJB. | Intermediate |  |
| [hibernate4](hibernate4/README.md) |Hibernate 4 | This quickstart performs the same functions as the _hibernate5_ quickstart, but uses Hibernate 4 for database access. Compare this quickstart to the _hibernate5_ quickstart to see the changes needed to run with Hibernate 5. | Intermediate |  |
| [inter-app](inter-app/README.md) |EJB, CDI, JSF | The `inter-app` quickstart shows you how to use a shared API JAR and an EJB to provide inter-application communication between two WAR deployments. | Advanced |  |
| [jaxrs-client](jaxrs-client/README.md) |JAX-RS | The `jaxrs-client` quickstart demonstrates JAX-RS Client API, which interacts with a JAX-RS Web service that runs on WildFly. | Beginner |  |
| [jaxws-addressing](jaxws-addressing/README.md) |JAX-WS | The `jaxws-addressing` quickstart is a working example of the web service using WS-Addressing. | Beginner |  |
| [jaxws-ejb](jaxws-ejb/README.md) |JAX-WS | The `jaxws-ejb` quickstart is a working example of the web service endpoint created from an EJB. | Beginner |  |
| [jaxws-pojo](jaxws-pojo/README.md) |JAX-WS | The `jaxws-pojo` quickstart is a working example of the web service endpoint created from a POJO. | Beginner |  |
| [jaxws-retail](jaxws-retail/README.md) |JAX-WS | The `jaxws-retail` quickstart is a working example of a simple web service endpoint. | Beginner |  |
| [jsonp](jsonp/README.md) |CDI, JSF, JSON-P | The `jsonp` quickstart demonstrates how to use the JSON-P API to produce object-based structures and then parse and consume them as stream-based JSON strings. | Beginner |  |
| [jta-crash-rec](jta-crash-rec/README.md) |JTA, Crash Recovery | The `jta-crash-rec` quickstart uses JTA and Byteman to show how to code distributed (XA) transactions in order to preserve ACID properties on server crash. | Advanced |  |
| [jts](jts/README.md) |JTS, EJB, JMS | The `jts` quickstart shows how to use JTS to perform distributed transactions across multiple containers, fulfilling the properties of an ACID transaction. | Intermediate | cmt |
| [jts-distributed-crash-rec](jts-distributed-crash-rec/README.md) |JTS, Crash Recovery | The `jts-distributed-crash-rec` quickstart uses JTS and Byteman to demonstrate distributed crash recovery across multiple application servers. | Advanced | jts |
| [kitchensink](kitchensink/README.md) |CDI, JSF, JPA, EJB, JAX-RS, BV | The `kitchensink` quickstart demonstrates a Java EE 7 web-enabled database application using JSF, CDI, EJB, JPA, and Bean Validation. | Intermediate |  |
| [kitchensink-angularjs](kitchensink-angularjs/README.md) |AngularJS, CDI, JPA, EJB, JPA, JAX-RS, BV | The `kitchensink-angularjs` quickstart demonstrates a Java EE 7 application using AngularJS with JAX-RS, CDI, EJB, JPA, and Bean Validation. | Intermediate |  |
| [kitchensink-ear](kitchensink-ear/README.md) |CDI, JSF, JPA, EJB, JAX-RS, BV, EAR | The `kitchensink-ear` quickstart demonstrates web-enabled database application, using JSF, CDI, EJB, JPA, and Bean Validation, packaged as an EAR. | Intermediate |  |
| [kitchensink-html5-mobile](kitchensink-html5-mobile/README.md) |CDI, HTML5, REST | The `kitchensink-html5-mobile` quickstart is based on `kitchensink`, but uses HTML5 and jQuery Mobile, making it suitable for mobile and tablet computers. | Beginner |  |
| [kitchensink-jsp](kitchensink-jsp/README.md) |JSP, JSTL, CDI, JPA, EJB, JAX-RS, BV | The `kitchensink-jsp` quickstart demonstrates how to use JSP, JSTL, CDI, EJB, JPA, and Bean Validation in WildFly. | Intermediate |  |
| [kitchensink-ml](kitchensink-ml/README.md) |CDI, JSF, JPA, EJB, JAX-RS, BV, i18n, l10n | The `kitchensink-ml` quickstart demonstrates a localized Java EE 7 compliant application using JSF, CDI, EJB, JPA, and Bean Validation. | Intermediate |  |
| [kitchensink-ml-ear](kitchensink-ml-ear/README.md) |CDI, JSF, JPA, EJB, JAX-RS, BV, EAR, i18n, l10n | The `kitchensink-ml-ear` quickstart demonstrates a localized database application, using JSF, CDI, EJB, JPA, and Bean Validation, packaged as an EAR. | Intermediate |  |
| [kitchensink-utjs-angularjs](kitchensink-utjs-angularjs/README.md) |Undertow.js, Angular.js | Based on kitchensink, but uses a Angular for the front end and Undertow.js for the back end. | Intermediate |  |
| [kitchensink-utjs-mustache](kitchensink-utjs-mustache/README.md) |Undertow.js, Mustache | Based on kitchensink, but uses Mustache for the front end and Undertow.js for the back end. | Intermediate |  |
| [logging](logging/README.md) |Logging | The `logging` quickstart demonstrates how to configure different logging levels in WildFly. It also includes an asynchronous logging example. | Intermediate | None |
| [logging-tools](logging-tools/README.md) |JBoss Logging Tools | The `logging-tools` quickstart shows how to use JBoss Logging Tools to create internationalized loggers, exceptions, and messages and localize them. | Beginner |  |
| [mail](mail/README.md) |JavaMail, CDI, JSF | The `mail` quickstart demonstrates how to send email using CDI and JSF and the default Mail provider that ships with WildFly. | Beginner |  |
| [managed-executor-service](managed-executor-service/README.md) |EE Concurrency Utilities, JAX-RS, JAX-RS Client API | The `managed-executor-service` quickstart demonstrates how Java EE applications can submit tasks for asynchronous execution. | Beginner |  |
| [messaging-clustering](messaging-clustering/README.md) |JMS, MDB | The `messaging-clustering` quickstart does not contain any code and instead uses the `helloworld-mdb` quickstart to demonstrate clustering using ActiveMQ Messaging. | Intermediate | helloworld-mdb |
| [messaging-clustering-singleton](messaging-clustering-singleton/README.md) |JMS, MDB, Clustering | The `messaging-clustering-singleton` quickstart uses a JMS topic and a queue to demonstrate clustering using ActiveMQMessaging with MDB singleton configuration (only one node in the cluster will be active) | Advanced |  |
| [numberguess](numberguess/README.md) |CDI, JSF | The `numberguess` quickstart demonstrates the use of *CDI*  (Contexts and Dependency Injection) and *JSF* (JavaServer Faces) in WildFly. | Beginner |  |
| [payment-cdi-event](payment-cdi-event/README.md) |CDI, JSF | The `payment-cdi-event` quickstart demonstrates how to create credit and debit *CDI Events* in WildFly, using a JSF front-end client. | Beginner |  |
| [resteasy-jaxrs-client](resteasy-jaxrs-client/README.md) |JAX-RS, CDI | The `resteasy-jaxrs-client` quickstart demonstrates an external JAX-RS RestEasy client, which interacts with a JAX-RS Web service that uses *CDI* and *JAX-RS*. | Intermediate | helloworld-rs |
| [servlet-async](servlet-async/README.md) |Asynchronous Servlet, CDI, EJB | The `servlet-async` quickstart demonstrates how to use asynchronous servlets to detach long-running tasks and free up the request processing thread. | Intermediate |  |
| [servlet-filterlistener](servlet-filterlistener/README.md) |Servlet Filter, Servlet Listener | The `servlet-filterlistener` quickstart demonstrates how to use Servlet filters and listeners in an application. | Intermediate |  |
| [servlet-security](servlet-security/README.md) |Servlet, Security | The `servlet-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and Security in WildFly. | Intermediate |  |
| [shopping-cart](shopping-cart/README.md) |SFSB EJB | The `shopping-cart` quickstart demonstrates how to deploy and run a simple Java EE 7 shopping cart application that uses a stateful session bean (SFSB). | Intermediate |  |
| [shrinkwrap-resolver](shrinkwrap-resolver/README.md) |CDI, Arquillian, Shrinkwrap | The `shrinkwrap-resolver` quickstart demonstrates three common use cases for ShrinkWrap Resolver in WildFly Application Server. | Intermediate |  |
| [spring-greeter](spring-greeter/README.md) |Spring MVC, JSP, JPA | The `spring-greeter` quickstart is based on the `greeter` quickstart, but differs in that it uses Spring MVC for Mapping GET and POST requests. | Beginner |  |
| [spring-kitchensink-asyncrequestmapping](spring-kitchensink-asyncrequestmapping/README.md) |JSP, JPA, JSON, Spring, JUnit | The `spring-kitchensink-asyncrequestmapping` quickstart showcases the use of asynchronous requests is an example using JSP, JPA and Spring 4.x. | Intermediate |  |
| [spring-kitchensink-basic](spring-kitchensink-basic/README.md) |JSP, JPA, JSON, Spring, JUnit | The `spring-kitchensink-basic` quickstart is an example of a Java EE 7 application using JSP, JPA and Spring 4.x. | Intermediate |  |
| [spring-kitchensink-controlleradvice](spring-kitchensink-controlleradvice/README.md) |JSP, JPA, JSON, Spring, JUnit | The `spring-kitchensink-controlleradvice` quickstart showcases Spring 4.x's `@ControllerAdvice`, which was introduced in Spring 3.2. | Intermediate |  |
| [spring-kitchensink-matrixvariables](spring-kitchensink-matrixvariables/README.md) |JSP, JPA, JSON, Spring, JUnit | The `spring-kitchensink-matrixvariables` quickstart showcases Spring 4.x's support for **Matrix Variables** in URLs that was introduced in Spring 3.2. | Intermediate |  |
| [spring-kitchensink-springmvctest](spring-kitchensink-springmvctest/README.md) |JSP, JPA, JSON, Spring, JUnit | The  `spring-kitchensink-springmvctest` quickstart demonstrates how to create an MVC application using JSP, JPA and Spring 4.x. | Intermediate |  |
| [spring-petclinic](spring-petclinic/README.md) |JPA, Junit, JMX, Spring MVC Annotations, AOP, Spring Data, JSP, webjars, Dandellion | The `spring-petclinic` quickstart shows how to run the Spring PetClinic Application in WildFly using the WildFly BOMs. | Advanced |  |
| [spring-resteasy](spring-resteasy/README.md) |Resteasy, Spring | The `spring-resteasy` quickstart demonstrates how to package and deploy a web application that includes resteasy-spring integration. | Beginner |  |
| [tasks](tasks/README.md) |JPA, Arquillian | The `tasks` quickstart includes a persistence unit and sample persistence code to demonstrate how to use JPA for database access in WildFly. | Intermediate |  |
| [tasks-jsf](tasks-jsf/README.md) |JSF, JPA | The `tasks-jsf` quickstart demonstrates how to use JPA persistence with JSF as the view layer. | Intermediate | tasks |
| [tasks-rs](tasks-rs/README.md) |JPA, JAX-RS | The `tasks-rs` quickstart demonstrates how to implement a JAX-RS service that uses JPA persistence. | Intermediate | tasks |
| [temperature-converter](temperature-converter/README.md) |CDI, JSF, SLSB EJB | The `temperature-converter` quickstart does temperature conversion using an *EJB Stateless Session Bean* (SLSB), *CDI*, and a *JSF* front-end client. | Beginner |  |
| [thread-racing](thread-racing/README.md) |Batch, CDI, EE Concurrency, JAX-RS, JMS, JPA, JSON, Web Sockets | A thread racing web application that demonstrates technologies introduced or updated in the latest Java EE specification. | Beginner |  |
| [websocket-client](websocket-client/README.md) |Web Socket, CDI Events, JSON, SSL | Demonstrates use of a Javascript WebSocket client, WebSocket configuration, programmatic binding, and secure WebSocket. | Intermediate |  |
| [websocket-endpoint](websocket-endpoint/README.md) |CDI, WebSocket, JSON-P | Shows how to use WebSockets with JSON to broadcast information to all open WebSocket sessions in WildFly. | Beginner |  |
| [websocket-hello](websocket-hello/README.md) |WebSocket, CDI, JSF | The `websocket-hello` quickstart demonstrates how to create a simple WebSocket application. | Beginner |  |
| [wicket-ear](wicket-ear/README.md) |Apache Wicket, JPA | Demonstrates how to use the Wicket Framework 7.x with the JBoss server using the Wicket Java EE integration, packaged as an EAR | Intermediate |  |
| [wicket-war](wicket-war/README.md) |Apache Wicket, JPA | Demonstrates how to use the Wicket Framework 7.x with the JBoss server using the Wicket Java EE integration packaged as a WAR | Intermediate |  |
| [wsat-simple](wsat-simple/README.md) |WS-AT, JAX-WS | The `wsat-simple` quickstart demonstrates a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service, bundled as a WAR, and deployed to WildFly. | Intermediate |  |
| [wsba-coordinator-completion-simple](wsba-coordinator-completion-simple/README.md) |WS-BA, JAX-WS | The `wsba-coordinator-completion-simple` quickstart deploys a WS-BA (WS Business Activity) enabled JAX-WS Web service WAR (CoordinatorCompletion protocol). | Intermediate |  |
| [wsba-participant-completion-simple](wsba-participant-completion-simple/README.md) |WS-BA, JAX-WS | The `wsba-participant-completion-simple` quickstart deploys a WS-BA (WS Business Activity) enabled JAX-WS Web service WAR (ParticipantCompletion Protocol). | Intermediate |  |
| [xml-dom4j](xml-dom4j/README.md) |DOM4J, Servlet, JSF | The `xml-dom4j` quickstart demonstrates how to use Servlet and JSF to upload an XML file to WildFly and parse it using a 3rd party XML parsing library. | Intermediate |  |
| [xml-jaxp](xml-jaxp/README.md) |JAXP, SAX, DOM, Servlet | The `xml-jaxp` quickstart demonstrates how to use Servlet and JSF to upload an XML file to WildFly and validate and parse it using DOM or SAX. | Intermediate |  |



## Suggested Approach to the Quickstarts

We suggest you approach the quickstarts as follows:

* Regardless of your level of expertise, we suggest you start with the **helloworld** quickstart. It is the simplest example and is an easy way to prove your server is configured and started correctly.
* If you are a beginner or new to JBoss, start with the quickstarts labeled **Beginner**, then try those marked as **Intermediate**. When you are comfortable with those, move on to the **Advanced** quickstarts.
* Some quickstarts are based upon other quickstarts but have expanded capabilities and functionality. If a prerequisite quickstart is listed, be sure to deploy and test it before looking at the expanded version.


## System Requirements

The applications these projects produce are designed to be run on WildFly Application Server 11 or later.

All you need to build these projects is Java 8.0 (Java SDK 1.8) or later and Maven 3.3.1 or later. See [Configure Maven for WildFly 11](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the WildFly distribution ZIP. For information on how to install and run JBoss, see the [WildFly Application Server Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.


## Run the Quickstarts

The root folder of each individual quickstart contains a README file with specific details on how to build and run the example. In most cases you do the following:

* [Start the WildFly Server](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/START_JBOSS_EAP.md#start-the-jboss-eap-server)
* [Build and deploy the quickstarts](#build-and-deploy-the-quickstarts)


### Build and Deploy the Quickstarts

See the README file in each individual quickstart folder for specific details and information on how to run and access the example.


#### Build the Quickstart Archive

In most cases, you can use the following steps to build the application to test for compile errors or to view the contents of the archive. See the specific quickstart README file for complete details.

1. Open a command prompt and navigate to the root directory of the quickstart you want to build.
2. Use this command if you only want to build the archive, but not deploy it:

            mvn clean install

#### Build and Deploy the Quickstart Archive

In most cases, you can use the following steps to build and deploy the application. See the specific quickstart README file for complete details.

1. Make sure you start the WildFly server as described in the quickstart README file.
2. Open a command prompt and navigate to the root directory of the quickstart you want to run.
3. Use this command to build and deploy the archive:

            mvn clean install wildfly:deploy

#### Undeploy an Archive

The command to undeploy the quickstart is simply:

        mvn wildfly:undeploy


### Verify the Quickstarts Build with One Command

You can verify the quickstarts build using one command. However, quickstarts that have complex dependencies must be skipped. For example, the `resteasy-jaxrs-client` quickstart is a RESTEasy client that depends on the deployment of the `helloworld-rs` quickstart. As noted above, the root `pom.xml` file defines a `complex-dependencies` profile to exclude these quickstarts from the root build process.

To build the quickstarts:

1. Do not start the WildFly server.
2. Open a command prompt and navigate to the root directory of the quickstarts.
3. Use this command to build the quickstarts that do not have complex dependencies:

            mvn clean install '-Pdefault,!complex-dependencies'


### Undeploy the Deployed Quickstarts with One Command

To undeploy the quickstarts from the root of the quickstart folder, you must pass the argument `-fae` (fail at end) on the command line. This allows the command to continue past quickstarts that fail due to complex dependencies and quickstarts that only have Arquillian tests and do not deploy archives to the server.

You can undeploy quickstarts using the following procedure:

1. Start the WildFly server.
2. Open a command prompt and navigate to the root directory of the quickstarts.
3. Use this command to undeploy any deployed quickstarts:

            mvn wildfly:undeploy -fae

To undeploy any quickstarts that fail due to complex dependencies, follow the undeploy procedure described in the quickstart's README file.


## Run the Quickstarts in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).


## Optional Components

The following components are needed for only a small subset of the quickstarts. Do not install or configure them unless the quickstart requires it.

* [Create Users Required by the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#create-users-required-by-the-quickstarts): Add a Management or Application user for the quickstarts that run in a secured mode.

* [Configure the PostgreSQL Database for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL_EAP7.md#configure-the-postgresql-database-for-use-with-the-quickstarts): The PostgreSQL database is used for the distributed transaction quickstarts.

* [Configure Byteman for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#configure-byteman-for-use-with-the-quickstarts): This tool is used to demonstrate crash recovery for distributed transaction quickstarts.
