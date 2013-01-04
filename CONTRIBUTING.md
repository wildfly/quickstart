Quickstarts Contributing Guide
-------------------------------

Quickstarts are very focused - they demonstrate on a single API (e.g. JAX-RS) or a single use case (write a web-app with CRUD)

To contribute with Quickstarts, clone your own fork instead of cloning the main Quickstarts repository, commit your work on topic branches and make pull requests. In detail:

1. [Fork](http://help.github.com/fork-a-repo/) the project.

2. Clone your fork (`git@github.com:<your-username>/jboss-as-quickstart.git`).

3. Add an `upstream` remote (`git remote add upstream
   git@github.com:jboss-jdf/jboss-as-quickstart.git`).

4. Get the latest changes from upstream (e.g. `git pull upstream master`).

5. Create a new topic branch to contain your feature, change, or fix (`git
   checkout -b <topic-branch-name>`).

6. Make sure that your changes follow the General Guide Lines.


7. Commit your changes to your topic branch.

8. Push your topic branch up to your fork (`git push origin
   <topic-branch-name>`).

9. [Open a Pull Request](http://help.github.com/send-pull-requests/) with a
   clear title and description.

If you don't have the Git client (`git`), get it from:

http://git-scm.com/


General Guidelines
------------------

1. The sample project should be formatted using the JBoss AS profiles found at http://github.com/jboss/ide-config/tree/master/

2. The package must be *org.jboss.as.quickstarts*

3. The quickstart project or folder name should match the quickstart name. Each sample project should have a unique name, allowing easy identification by users and developers.

4. The project must follow the structure used by existing quickstarts such as [numberguess](https://github.com/jboss-jdf/jboss-as-quickstart/tree/master/numberguess). A good starting point would be to copy the  `numberguess` project.

5. The sample project should be importable into JBoss Developer Studio/JBoss Tools and be deployable from there.

6. A sample project should have a simple build that the user can quickly understand. If using maven it should:

 - Not inherit from another POM
 - Import the various BOMs, either directly from a project, or from [JBoss BOMs](http://www.jboss.org/jdf/stack/stacks/), to determine version numbers. You should aim to have no dependencies declared directly. If you do, work with the jdf team to get them added to a BOM.
 - Use the JBoss AS Maven Plugin to deploy the example

7. The sample project should contain a `README.md` file using the `template/README.md` file as a guideline

8. Don't forget to update the `pom.xml` in the quickstart root directory. Add your quickstart to the 'modules' section.

Quickstarts in other repositories
----------------------------------

  If your quickstarts are stored in another repository, you may wish to merge them in from there, rather than contribute them from source. If you plan to do this, discuss first with the JBoss AS Quickstarts team, as they will want to review all commits to *your* repo going forward.

  To do this

  1. Add the other repo as a remote
      
         git remote add -f <other repo name> <other repo url>

  2. Merge from the tag in the other repo that you wish to use. It is important to use a tag, to make tracking of history easier. We use a recursive merge strategy, always preferring changes from the other repo, in effect overwriting what we have locally.

         git merge <tag> -s recursive -Xtheirs -m "Merge <Other repository name> '<Tag>'"

  3. Review and push to upstream

         git push upstream HEAD:master

Kitchensink variants
--------------------

  There are multiple quickstarts based on the kitchensink example.  Each showcases different technologies and techniques including pure EE6, JSF, HTML5, and GWT.  

  If you wish to contribute a kitchensink variant is it important that you follow the look and feel of the original so that useful comparisons can be made.  This does not mean that variants can not expand, and showcase additional functionality.  Multiple variants already do that.  These include mobile interfaces, push updates, and more.

  Below are rules for the l&f of the variants:

  * Follow the primary layout, style, and graphics of the original.
  * Projects can have 3-4 lines directly under the AS/EAP banner in the middle section to describe what makes this variant different.  
     * How projects use that space is up to them, but options include plain text, bullet points, etc....  
  * Projects can have their logo in the left side of the banner.  
    * The sidebar area can contain a section with links to the related projects, wiki, tutorials, etc...  
       * This should be below any AS/EAP link areas.

    If appropriate for the technology the application should expose RESTful endpoints following the example of the original kitchensink quickstart.  This should also include the RESTful links in the member table.

License Information and Contributor Agreement
---------------------------------------------

  JBoss Developer Framework is licensed under the Apache License 2.0, as we believe it is one of the most permissive Open Source license. This allows developers to easily make use of the code samples in JBoss Developer Framework. 

  There is no need to sign a contributor agreement to contribute to JBoss Developer Framework. You just need to explicitly license any contribution under the AL 2.0. If you add any new files to JBoss Developer Framework, make sure to add the correct header.

  ### Java

      /*
       * JBoss, Home of Professional Open Source
       * Copyright <Year>, Red Hat, Inc. and/or its affiliates, and individual
       * contributors by the @authors tag. See the copyright.txt in the 
       * distribution for a full listing of individual contributors.
       *
       * Licensed under the Apache License, Version 2.0 (the "License");
       * you may not use this file except in compliance with the License.
       * You may obtain a copy of the License at
       * http://www.apache.org/licenses/LICENSE-2.0
       * Unless required by applicable law or agreed to in writing, software
       * distributed under the License is distributed on an "AS IS" BASIS,  
       * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       * See the License for the specific language governing permissions and
       * limitations under the License.
       */

  ### XML

      <!--
       JBoss, Home of Professional Open Source
       Copyright <Year>, Red Hat, Inc. and/or its affiliates, and individual
       contributors by the @authors tag. See the copyright.txt in the 
       distribution for a full listing of individual contributors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,  
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
       -->

  ### Properties files

       # JBoss, Home of Professional Open Source
       # Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
       # contributors by the @authors tag. See the copyright.txt in the 
       # distribution for a full listing of individual contributors.
       #
       # Licensed under the Apache License, Version 2.0 (the "License");
       # you may not use this file except in compliance with the License.
       # You may obtain a copy of the License at
       # http://www.apache.org/licenses/LICENSE-2.0
       # Unless required by applicable law or agreed to in writing, software
       # distributed under the License is distributed on an "AS IS" BASIS,  
       # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       # See the License for the specific language governing permissions and
       # limitations under the License.

Release procedures
------------------

1. Make sure you have access to rsync files to `filemgmt.jboss.org/download_htdocs/jbossas`

2. Release the archetypes

3. Regenerate the quickstart based on archetypes
         
        dist/release-utils.sh -r

4. Release
         
        dist/release.sh -s <old snapshot version> -r <release version>

This will update the version number, commit and tag, build the distro zip and upload it to <download.jboss.org>. Then it will reset the version number back to the snapshot version number.
