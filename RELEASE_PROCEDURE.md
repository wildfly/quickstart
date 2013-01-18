Quickstarts Release Procedure
==============================

Testing the quickstarts
-----------------------

  Most of the quickstarts require JBoss Enterprise Application Platform or JBoss AS only in standalone mode. Some require the "standalone-full" profile, some require XTS, some require Postgres and some require other quickstarts to be deployed. Profiles are used in the root POM to separate out these groups, allowing you to test the quickstarts easily. For example, to run those that require only standalone mode:

      mvn clean install jboss-as:deploy jboss-as:undeploy -Parq-jbossas-remote -P-requires-postgres,-requires-full,-complex-dependencies,-requires-xts

  Or, to run those only those quickstarts that require the full profile

      mvn clean install jboss-as:deploy jboss-as:undeploy -Parq-jbossas-remote -P-requires-postgres,-default,-complex-dependencies,-requires-xts

  And so on.

Quickstarts in other repositories
---------------------------------

  If the quickstarts are stored in another repository, you may wish to merge them in from there, do this:

  1. Add the other repo as a remote
      
         git remote add -f <other repo> <other repo url>

  2. Merge from the tag in the other repo that you wish to use. It is important to use a tag, to make tracking of history easier. We use a recursive merge strategy, always preferring changes from the other repo, in effect overwriting what we have locally.

         git merge <tag> -s recursive -Xtheirs -m "Merge <Other repository name> '<Tag>'"

  3. Review and push to upstream

         git push upstream HEAD:master

Rendering Markdown
------------------

  The quickstarts use Redcarpet to process the markdown, the same processor used by GitHub. This builds on the basic markdown syntax, adding support for tables, code highlighting, relaxed code blocks etc). We add a couple of custom piece of markup - \[TOC\] which allows a table of contents, based on headings, to be added to any file, and [Quickstart-TOC], which adds in a table listing the quickstarts.

  To render the quickstarts README's you will need, a working Ruby and Python install, with the various gems and eggs set up. 

  To setup the environment you need to follow these steps. *Certify to use the correct versions*.

1. Install Ruby *1.9.X*

    For RHEL you can use this [spec](https://github.com/lnxchk/ruby-1.9.3-rpm)

2. Install Ruby GEMs

        gem install redcarpet nokogiri pygments.rb

Then just run

        ./dist/release-utils.sh -m

  To render all markdown files to HTML.

Publishing builds to Maven
--------------------------

  1. You must have gpg set up and your key registered, as described at <http://www.sonatype.com/people/2010/01/how-to-generate-pgp-signatures-with-maven/>
  2. You must provide a property `gpg.passphrase` in your `settings.xml` in the `release` profile e.g.

          <profile>
                <id>release</id>
                <properties>
                    <gpg.passphrase>myPassPhrase</gpg.passphrase>
                </properties>
          </profile>
  3. You must have a JBoss Nexus account, configured with the server id in `settings.xml` with the id `jboss-releases-repository` e.g.

          <server>
              <id>jboss-releases-repository</id>
              <username>myUserName</username>
              <password>myPassword</password>
          </server>

  4. Add `org.sonatype.plugins` plugin group to your `settings.xml` so nexus plugin can be available for publishing scripts.

          <pluginGroups>
              <pluginGroup>org.sonatype.plugins</pluginGroup>
          </pluginGroups>

Release Procedure
-----------------

  1. Make sure you have access to rsync files to `filemgmt.jboss.org/download_htdocs/jbossas`
  2. Release the archetypes
  3. Regenerate the quickstart based on archetypes
         
         dist/release-utils.sh -r

  4. Release
         
         dist/release.sh -s <old snapshot version> -r <release version>

     This will update the version number, commit and tag, build the distro zip and upload it to <download.jboss.org>. Then it will reset the version number back to the snapshot version number.
