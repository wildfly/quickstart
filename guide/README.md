Note to Editors
===============

Please honor the formatting that has been applied to these documents so that it is easy to track the changes that are
made between commits. The formatting is described using VIM modelines, which appear in a comment before the final end
tag in each document:

    <!--
    vim:et:ts=3:sw=3:tw=120
    -->

They describe the following rules:

* Expand tabs to spaces
* indent size: 3 spaces
* max line length: 120

Also, each node generation should be indented from its parent.

Generally, the text in paragraphs should appear on separate lines from the `<para>` tags, but there are some cases when
this formatting rule has been relaxed.

Please use the following two entity codes for inserting dashes:

    &#8211 - ndash
    &#8212 - mdash

The DocType transformation stylesheet does not handle `&ndash`, `&mdash`, `&#150;` or `&#151;` properly.

Bug in IcedTea
==============

There appears to be a bug in IcedTea on Ubuntu that causes the docbook transformation to fail when inserting an image
into the document (there are several in the reference guide). See this bug for more details.

<http://bugs.debian.org/cgi-bin/bugreport.cgi?bug=447951>

Please use the Sun Java JDK if you encounter this problem.

    vim:et:ts=3:sw=3:tw=120

Guide to Translation Build Scripts
==================================

Translation Workflow
--------------------

1. Author modifies documentation, checks in DocBook XML source.
2. At some point in the lifecycle, a documentation freeze is announced.
3. Import job is run (eg from Hudson).  (See script 2 below: `flies_import_source`.)
4. Translators can begin translating at <https://translate.jboss.org/flies/>.
5. Draft builds are run nightly or more often (Hudson?).  (See script 3 below: `flies_draft_build`)
6. If author changes any XML, go back to step 3.
7. Translations declared "final"
8. Documentation release build is run.  (See script 4 below: `flies_export_translations`)

Configuration for build machine
-------------------------------
Create the file `~/.config/flies.ini` like this:

<pre>
[servers]
jboss.url = https://translate.jboss.org/flies/
jboss.username = your_jboss_username
jboss.key = your_API_key_from_Flies_Profile_page
</pre>

NB: Your key can be obtained by logging in to [Flies](https://translate.jboss.org/flies/), 
visiting the [Profile page](https://translate.jboss.org/flies/profile/view) and 
clicking "generate API key" at the bottom.

The Scripts
-----------

1. Import job (run after documentation freeze):  
 This script will update the POT files in `src/main/docbook/pot` and
 push them to Flies for translation.  
 `src/main/scripts/flies_import_source`

2. Build docs with latest translations (probably run nightly):  
 This script will fetch the latest translations to temporary files 
 in `target/draft` and build the documentation for review purposes.  
 `src/main/scripts/flies_draft_build`

3. Documentation release build:  
 This script will fetch the latest translations so that the release 
 build can be run.  
 `src/main/scripts/flies_export_translations`  
 `git checkin src/main/docbook`  
 _run existing ant build_

