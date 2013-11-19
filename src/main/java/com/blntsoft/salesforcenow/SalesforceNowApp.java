package com.blntsoft.salesforcenow;

import android.app.Application;

import com.salesforce.androidsdk.app.SalesforceSDKManager;

/**
 * Application class for our application.
 */
public class SalesforceNowApp extends Application {

    public static final String LOG_TAG = "SalesForceNow";

	@Override
	public void onCreate() {
		super.onCreate();
		SalesforceSDKManager.initNative(getApplicationContext(), new KeyImpl(), MainActivity2.class);

		/*
		 * Un-comment the line below to enable push notifications in this app.
		 * Replace 'pnInterface' with your implementation of 'PushNotificationInterface'.
		 * Add your Google package ID in 'bootonfig.xml', as the value
		 * for the key 'androidPushNotificationClientId'.
		 */
		// SalesforceSDKManager.getInstance().setPushNotificationReceiver(pnInterface);
	}
}
