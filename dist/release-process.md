Releasing
=========

1. Release the archetypes
2. Regenerate the quickstart based archetypes

        ./dist/release-utils.sh -r

3. Regenerate html readmes from markdown
        
         ./dist/release-utils.sh -m
4. Commit this
5. Update the version numbers

        dist/release-utils.sh -u -o <old snapshot version> -n <release version>

6. Commit this
7. Tag using
        
        git tag -a <release version> -m "Tag <release version>"

8. Reset the version numbers for development

        dist/release-utils.sh -u -n <new snapshot version> -o <release version>

9. Check out the tag

        git checkout <release version>
10. Build the distro

        mvn clean install -f dist/pom.xml
11. Rsync the zip to download.jboss.org

        rsync -Pv --protocol=28 jbossas@download.jboss.org:download_htdocs/jbossas/7.<minor version>/jboss-as-<version>
12. Update the jboss.org/jbossas/downloads magnolia page