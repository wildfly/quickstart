kitchensink-cordova-ios: Example AeroGear Application Using Hybrid HTML5 + REST with Apache Cordova for iOS
===========================================================================================================
Author: Kris Borchers
Level: Intermediate
Technologies: HTML5, REST, Apache Cordova, iOS
Summary: Based on kitchensink, but uses hybrid HTML5 running as a native iOS application on mobiles

What is it?
-----------

This is a aplication build upon AeroGear’s kitchensink quickstart, which was converted to an Apache Cordova based hybrid application.

What does all of that mean? Basically, this takes our HTML5 + REST / jQuery Mobile based web app and converts it to a native app for iOS. 

System requirements
-------------------

All you need to build this project is Mac OS X Lion (10.7) (or later) and XCode 4.3 or later and the appropriate iOS SDK for your needs.

With the prerequisites out of the way, you're ready to build and deploy.

If you need more detailed instruction to setup a iOS Development Environment with Apache Cordova, you can take a look at [Setting up your development enivronment to use Apache Cordova](http://aerogear.org/docs/guides/CordovaSetup/)

Import the Quickstart Code
--------------------------

First we need to import the existing iOS code to XCode.

1. Open Finder and navigate to '<repo-folder>/kitchensink-cordova-ios/'
2. Right click on *KitchensinkCordova.xcodeproj* and select *Open With XCode*
3. Change the Target in the Scheme menu to KitchensinkCordova and select a device
4. Make sure that your `<KITCHENSINK-CORDOVA>/android/assets/www` is a symbolic link to `../shared/www`


Run and Access the application
------------------------------

If your toolbar is opened (click View->Show Toolbar to show it). Click on *Run* button. This will start the iOS Simulator with this quickstart running on it.


Converting an AeroGear HTML5 + REST Web App to a Hybrid App with Apache Cordova
--------------------------------------------------------------------------------

If you have an existing Web Application based on HTML5 + REST you can follow [this guide](http://aerogear.org/docs/guides/HTML5ToHybridWithCordova)



