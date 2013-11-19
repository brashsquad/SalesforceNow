package com.blntsoft.salesforcenow;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.blntsoft.salesforcenow.service.SpeechActivationService;
import com.blntsoft.salesforcenow.service.SpeechRecognizerService;
import com.blntsoft.salesforcenow.util.SystemUiHider;
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
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == VOICE_EVENT_ID
                && resultCode == RESULT_OK) {
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String s : matches) {
                        Toast.makeText(MainActivity2.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Intent searchIntent = new Intent(this, SearchActivity.class);
            searchIntent.putExtra(SearchActivity.SEARCH_STRING_EXTRA, matches.get(0));
            startActivity(searchIntent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * Specific
     */

    public void onVoiceButtonClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_now_hint));
        startActivityForResult(intent, VOICE_EVENT_ID);
    }

    public void onServiceButtonClick(View v) {

        Intent i = SpeechActivationService.makeStartServiceIntent(this,
                null);
        this.startService(i);
        Log.d("SalesforceNow", "started service for ");
    }

}
