kitchensink-cordova-android: Example AeroGear Application Using Hybrid HTML5 + REST with Apache Cordova for Android
===================================================================================================================
Author: Kris Borchers
Level: Intermediate
Technologies: HTML5, REST, Apache Cordova, Android
Summary: Based on kitchensink, but uses hybrid HTML5 running as a native android application on mobiles

What is it?
-----------

This is a aplication build upon AeroGear’s kitchensink quickstart, which was converted to an Apache Cordova based hybrid application.

What does all of that mean? Basically, this takes our HTML5 + REST / jQuery Mobile based web app and converts it to a native app for Android. 

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, JBDS 5 or Eclipse with [Android Development Tools (ADT) Plugin for the Eclipse IDE](http://developer.android.com/tools/sdk/eclipse-adt.html).

With the prerequisites out of the way, you're ready to build and deploy.

If you need more detailed instruction to setup a Android Development Environment with Apache Cordova, you can take a look at [Setting up your development enivronment to use Apache Cordova](http://aerogear.org/docs/guides/CordovaSetup/)

Import the Quickstart Code
---------------------------

First we need to import the existing Android code to JBDS or Eclipse

1. On Eclipse, click File then import.
2. Select *Existing Android Code Into Workspace* and click *Next*.
3. On Root Directory, click on *Browse...* button and navigate to the root path of the Quickstart on your filesystem.
4. After selecting the Quickstart project, you can click on *Finish* button to start the project import.
5. Make sure that your `<KITCHENSINK-CORDOVA>/android/assets/www` is a symbolic link to `../shared/www`


#### Troubleshooting Fedora/Red Hat Enterprise Linux

If you receive an error _Unable to resolve target 'android-10'_ It means that you don't have the Android API 10 (Android 2.3.3) installed. To solve this you must:

1. Right-click the project in the left pane
2. Select *Properties*
3. On Android options, select the appropriate Android API.

_NOTE: This sample was built for the Android 2.1 SDK for maximum compatibility with current devices but should work on any 2.x or 4.0 SDK. The sample was not tested on the 3.x series._

#### Troubleshooting Windows Operating Systems

As Windows doesn't suppot symbolic links you must copy `shared/www` folder to `<KITCHENSINK-CORDOVA>/android/assets/www`


Start the Emulator and Deploy the application
--------------------------------------------

1. Start the emulator on Eclipse by clicking *Window* and select *AVD Manager*.
2. On Android Virtual Device Manager window, select the appropriate AVD and click on *Start* button.
3. On Lauch Options window click on *Lauch* button.
4. After Emulator started, select your project on Eclipse
5. Click on *Run*, then *Run As* and *Android Application*

Access the application
---------------------

The application will be running on the Emulator.

Converting an AeroGear HTML5 + REST Web App to a Hybrid App with Apache Cordova
--------------------------------------------------------------------------------

If you have an existing Web Application based on HTML5 + REST you can follow [this guide](http://aerogear.org/docs/guides/HTML5ToHybridWithCordova)



