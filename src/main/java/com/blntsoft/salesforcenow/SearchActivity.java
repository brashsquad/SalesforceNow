package com.blntsoft.salesforcenow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by baolongnt on 11/18/13.
 */
public class SearchActivity extends Activity {

    /*
     * Constants
     */

    private static final int VOICE_EVENT_ID             = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_now_hint));
        startActivityForResult(intent, VOICE_EVENT_ID);
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
                        Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Intent searchIntent = new Intent(this, SearchResultActivity.class);
            searchIntent.putExtra(SearchResultActivity.SEARCH_STRING_EXTRA, matches.get(0));
            startActivity(searchIntent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
