Releasing
=========

1. Make sure you have credentials set up to deploy to `jboss-releases-repository` in your `settings.xml`, and have access to rsync files to `filemgmt.jboss.org/download_htdocs/jbossas`
2. Release the archetypes
3. Regenerate the quickstart based on archetypes

        ./dist/release-utils.sh -r

4. Release

        dist/release.sh -s <old snapshot version> -r <release version>

5. Log in to the Nexus interface, and close / release 
        
6. Update the jboss.org/jbossas/downloads magnolia page
