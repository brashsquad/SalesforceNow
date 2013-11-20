Building The App
================

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
* Run 'gradlew' (Linux/MacOSX) or 'gradle.bat' (Windows). You can alternatively also load the project into Android Studio.
* The APK will be in 'build/apk'
