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

You can find the documentation at <https://docs.jboss.org/author/display/AS7/Documentation>.

If you add a quickstart, don't forget to update `dist/src/main/assembly/README.md`.

The 'dist' folder contains Maven scripts to build a zip of the quickstarts.
