
Description
===========

Salesforce Now is the perfect companion to Salesforce1 on Android.

Enhance your mobile Salesforce user experience with split seconds CRM information access, minimum number of clicks to actions and voice input action and search.

Salesforce Now tightly integrates with both your Android mobile device and Salesforce1. Salesforce Now can be launched as an assist app just like Google Now and presents the users with lists of most recently viewed records. Voice input allows the user not only to search and retrieve data faster but also to perform actions in fewer clicks. Basic actions like web browsing, phone call, email & mapping are integrated with the device while actions on the records like new & edit are integrated with the Salesforce1 app iself!

This alpha version of the app only supports the Account, Contact & Opportunity objects and the New and Edit record actions. We are building the code to be generic so that it will support any object and many more record actions.


Demo
====

[https://www.youtube.com/watch?v=Y0EK6DMfbUw](YouTube video)


Build The App
=============

Requirements
------------

* Android Build Tools 18.0.1
* Android SDK 11
* Android SDK 17
* [Salesforce Android SDK 2.1](https://github.com/forcedotcom/SalesforceMobileSDK-Android)


Steps
-----

* Generate a 'local.properties' file with the 'sdk.dir' property pointing to your Android SDK installation. You create the file manually or run 'android update project -p .'
* Copy build-SalesforceSDK.gradle' into the Salesforce Android SDK folder as 'build.gradle'
````
    Ex: cp build-SalesforceSDK.gradle ../SalesforceMobileSDK-Android/native/SalesforceSDK
````
* Update in line 2 of 'settings.gradle' with the path to your Salesforce Android SDK
````
    Ex: project(':SalesforceSDK').projectDir = new File('../SalesforceMobileSDK-Android/native/SalesforceSDK')
````
* Run 'gradlew build' (Linux/MacOSX) or 'gradle.bat build' (Windows). You can alternatively also load the project into Android Studio.
* The APK will be in 'build/apk'
 
