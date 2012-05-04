Releasing
=========

1. Ensure you have Ruby 1.9, RubyGems and the gems redcarpet (version 2), and pygments.rb set up and ready to run. This is used to process the markdown files and add syntax highlighting.
2. Make sure you have Bash 3 or newer
3. Make sure you have access to rsync files to `filemgmt.jboss.org/download_htdocs/jbossas`
4. Release the archetypes
5. Regenerate the quickstart based on archetypes

        ./dist/release-utils.sh -r

6. Release

        dist/release.sh -s <old snapshot version> -r <release version>

