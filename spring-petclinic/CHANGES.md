CHANGES TO SPRING PETCLINIC EXAMPLE
===================================

ORIGINAL SOURCE
---------------
[Spring PetClinic](<https://github.com/spring-projects/spring-petclinic>)

CHANGES
-------

1. Addition of `webapp/WEB-INF/jboss-deployment-structure.xml`, ensuring no conflict with JBOSS' Hibernate module with 
those packaged by this quickstart. See [JBOSS DEPLOYMENT STRUCTURE](<https://docs.jboss.org/author/display/AS7/Class+Loading+in+AS7#ClassLoadinginAS7-JBossDeploymentStructureFile>) for more details.
2. Configured to use Red Hat ${product.name} BOMs.
3. Regressed the jQuery version to 1.9 as that is what is currently certified on EAP.
4. Add plug-in to deploy directly to running JBoss from mvn build.
6. Add OpenShift support.
7. Add Apache license text.
8. Regress Hibernate core to 4.2.7.SP1 or before to ensure that it runs on EAP 7.
