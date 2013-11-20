How to build the app
====================

* Generate 'local.properties'. Ex: run 'android update project -p .'
* Copy build-SalesforceSDK.gradle into the Salesforce Android SDK folder as build.gradle. Ex: cp build-SalesforceSDK.gradle ../SalesforceMobileSDK-Android/native/SalesforceSDK
* Update l.2 of settings.gradle with the path to the Salesforce Android SDK
Ex: project(':SalesforceSDK').projectDir = new File('../SalesforceMobileSDK-Android/native/SalesforceSDK')
* Run gradlew (Linux/MacOS) or gradle.bat (Windows)


