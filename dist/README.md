Distribution
============

This is a Maven assembly to create a zip distro of the docs. To run, make sure all the docs are build

    mvn -f ../guides/pom.xml clean install

And then run the

    mvn clean install

You will find a zip in `target/jboss-as-docs-<version>.zip`

The `README.md` in `src/main/assembly` will be included in the finished zip.
