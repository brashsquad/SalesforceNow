package com.blntsoft.salesforcenow;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blntsoft.salesforcenow.service.SpeechActivationService;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see com.blntsoft.salesforcenow.util.SystemUiHider
 */
public class MainActivity2 extends SalesforceActivity {

    /*
     * Constants
     */

    private static final int VOICE_EVENT_ID             = 1;

    /*
     * Members
     */

    private RestClient client;
    private View rootView;

    /*
     * Activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout_2);

        rootView = findViewById(R.id.root);
    }

    @Override
    public void onResume() {
        // Hide everything until we are logged in
        rootView.setVisibility(View.INVISIBLE);

        super.onResume();
    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;

        // Show everything
        rootView.setVisibility(View.VISIBLE);

        Intent i = SpeechActivationService.makeStartServiceIntent(this, null);
        this.startService(i);
    }

    @Override
    public void onPause() {
        Intent i = SpeechActivationService.makeServiceStopIntent(this);
        this.stopService(i);

        super.onPause();
    }


    /*
     * Specific
     */

    public void onVoiceButtonClick(View v) {
        Intent i = SpeechActivationService.makeServiceStopIntent(this);
        this.stopService(i);

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void onServiceButtonClick(View v) {
        Intent i = SpeechActivationService.makeStartServiceIntent(this, null);
        this.startService(i);
        Log.d("SalesforceNow", "started service for ");
    }

}
