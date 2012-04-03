JBoss AS Quickstarts 
====================

Quickstarts (or examples, or samples) for JBoss AS. There are a number of rules for quickstarts:

* Each quickstart should have a unique name, this enables a user to quickly identify each quickstart
* A quickstart should have a simple build that the user can quickly understand. If using maven it should:
  1. Not inherit from another POM
  2. Import the various BOMs from AS7 APIs to get version numbers
  3. Use the JBoss AS Maven Plugin to deploy the example
* The quickstart should be importable into JBoss Tools and deployable there
* The quickstart should be explained in detail in the associated user guide, including how to deploy
* If you add a quickstart, don't forget to update `dist/src/main/assembly/README.md` and `pom.xml` (the 'modules' section).
* The quickstart should be formatted using the JBoss AS profiles found at <https://github.com/jbossas/jboss-as/tree/master/ide-configs>

You can find the documentation at <https://docs.jboss.org/author/display/AS7/Documentation>.

If you add a quickstart, don't forget to update `README.md`.

The 'dist' folder contains Maven scripts to build a zip of the quickstarts.

The quickstart code is licensed under the Apache License, Version 2.0:
<http://www.apache.org/licenses/LICENSE-2.0.html>

Quickstarts in other repositories
---------------------------------

If your quickstarts are stored in another repository, you may wish to merge them in from there, rather than contribute them from source. If you plan to do this, discuss first with the JBoss AS Quickstarts team, as they will want to review all commits to *your* repo going forward.

To do this

1. Add the other repo as a remote
    
    git remote add -f <other repo> <other repo url>

2. Merge from the tag in the other repo that you wish to use. It is important to use a tag, to make tracking of history easier. We use a recursive merge strategy, always preferring changes from the other repo, in effect overwriting what we have locally.

    git merge <tag> -s recursive -Xtheirs --no-commit

3. The merge is not committed, so any updates to the README.md and parent POM can be made. Having made these, perform the merge. We suggest updating the commit message to "Merge <Other Project Name> '<Tag>'".

    git commit

4. Review and push to upstream

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

Markdown
--------

We use Redcarpet to process the markdown, the same processor used by Guthub. This builds on the basic markdown syntax, adding support for tables, code highlighting, relaxed code blocks etc). We add a custom piece of markup \[TOC\] which allows a table of contents, based on headings, to be added to any file.
