Distribution
============

This is a Maven assembly to create a zip distro of the quickstarts. To run 

    mvn clean install

You will find a zip in `target/jboss-as-docs-<version>.zip`

The `README.md` in `src/main/assembly` will be included in the finished zip.


Release
=======

1. Commit the versions changes to POMs
2. Tag
3. Reset the versions to development
4. Check out the tag
5. Deploy 
    mvn clean deploy -DaltDeploymentRepository=jboss-releases-repository::default::https://repository.jboss.org/nexus/service/local/staging/deploy/maven2/
6. Package by running in `dist/`
    mvn clean install
