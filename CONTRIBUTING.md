Quickstarts Contributing Guide
==============================

Purpose of the quickstarts
--------------------------

- To demonstrate Java EE 6 technologies

- To provide developers with working examples and instructions that are easy to follow .

- To allow examples to be copied by developers and used as the basis for their own projects.


Basic Steps
-----------

To contribute to the quickstarts, fork the quickstart repository to your own Git, clone your fork, commit your work on topic branches, and make pull requests. 

If you don't have the Git client (`git`), get it from: <http://git-scm.com/>

Here are the steps in detail:

1. [Fork](https://github.com/jboss-jdf/jboss-as-quickstart/fork_select) the project. This creates a the project in your own Git.

2. Clone your fork. This creates a directory in your local file system.

        git clone git@github.com:<your-username>/jboss-as-quickstart.git

3. Add the remote `upstream` repository.

        git remote add upstream git@github.com:jboss-jdf/jboss-as-quickstart.git

4. Get the latest files from the `upstream` repository.

        git fetch upstream

5. Create a new topic branch to contain your features, changes, or fixes.

        git checkout -b <topic-branch-name> upstream/master

6. Contribute new code or make changes to existing files. Make sure that you follow the General Guidelines below.

7. Commit your changes to your local topic branch. You must use `git add filename` for every file you create or change.

        git add <changed-filename>
        git commit -m `Description of change...`

8. Push your local topic branch to your github forked repository. This will create a branch on your Git fork repository with the same name as your local topic branch name.

        git push origin HEAD            

9. Browse to the <topic-branch-name> branch on your forked Git repository and [open a Pull Request](http://help.github.com/send-pull-requests/). Give it a clear title and description.


General Guidelines
------------------

* The sample project should be formatted using the JBoss AS profiles found at http://github.com/jboss/ide-config/tree/master/

 - Code should be well documented with good comments. Please add an author tag (@author) to credit yourself for writing the code.
 - You should use readable variable names to make it easy for users to read the code.

* The package must be *org.jboss.as.quickstarts*

* The quickstart project or folder name should match the quickstart name. Each sample project should have a unique name, allowing easy identification by users and developers.

* If you create a quickstart that uses a database table, make sure the name you use for the table is unique across all quickstarts. 

* The project must follow the structure used by existing quickstarts such as [numberguess](https://github.com/jboss-jdf/jboss-as-quickstart/tree/master/numberguess). A good starting point would be to copy the  `numberguess` project.

* The sample project should be importable into JBoss Developer Studio/JBoss Tools and be deployable from there.

* Maven POMs must be used. No other build system is allowed unless the purpose of the quickstart is to show another build system in use. If using Maven it should:

 - Not inherit from another POM
 - Maven POMs must use the Java EE spec BOM/POM imports
 - The POMs must be commented, with a comment each item in the POM
 - Import the various BOMs, either directly from a project, or from [JBoss BOMs](http://www.jboss.org/jdf/stack/stacks/), to determine version numbers. You should aim to have no dependencies declared directly. If you do, work with the jdf team to get them added to a BOM.
 - Use the JBoss AS Maven Plugin to deploy the example

* The sample project must contain a `README.md` file using the `template/README.md` file as a guideline

* Don't forget to update the `pom.xml` in the quickstart root directory. Add your quickstart to the 'modules' section.

* The project must target Java 6

 - CDI should be used as the programming model
 - Avoid using a web.xml if possible. Use faces-config.xml to activate JSF if needed.
 - Any tests should use Arquillian.


Kitchensink variants
--------------------

  There are multiple quickstarts based on the kitchensink example.  Each showcases different technologies and techniques including pure EE6, JSF, HTML5, and GWT.  

  If you wish to contribute a kitchensink variant is it important that you follow the look and feel of the original so that useful comparisons can be made.  This does not mean that variants can not expand, and showcase additional functionality.  Multiple variants already do that.  These include mobile interfaces, push updates, and more.

  Below are rules for the *look and feel* of the variants:

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

