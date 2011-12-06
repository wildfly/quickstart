Distribution
============

This is a Maven assembly to create a zip distro of the quickstarts. To run 

    mvn clean install

You will find a zip in `target/jboss-as-docs-<version>.zip`

The `README.md` in `src/main/assembly` will be included in the finished zip.


Release process for quickstarts
===============================

1. Release the archetypes
2. Regenerate the archetype based quickstarts
 
        ./release-utils.sh -r <version of archetypes>

   This script assumes that the archetypes and quickstarts are version sync'd
3. Commit any changes to the archetype based quickstarts
4. Update versions by running

        ./release-utils.sh -u -o <old snapshot version> -n <release version>

5. Commit the version update
6. Tag
    
        git tag -a <release version> -m "Release <release version"

7. Reset version numbers to snapshots
        
        ./release-utils.sh -u -o <release version> -n <new snapshot version>

8. Checkout the tag
       
        git checkout <release version>
9. Build the dist

        mvn clean install
10. Upload the dist to <http://download.jboss.org>

        rsync -Pv target/jboss-as-docs-<version>.zip jbossas@filemgmt.jboss.org:downloads_htdocs/jbossas/7.<minor version>/<version>/
11. Update jboss.org/jbossas/downloads