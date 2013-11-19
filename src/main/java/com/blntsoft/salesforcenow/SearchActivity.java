package com.blntsoft.salesforcenow;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        getActionBar().hide();

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
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String s : matches) {
                        Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

            String voiceResult = matches.get(0);
            List<String> wordList = Arrays.asList(voiceResult.split(" "));

            String command = wordList.get(0);
            if (command.equals("open")) {
                Toast.makeText(SearchActivity.this, command, Toast.LENGTH_SHORT).show();
            } else if (command.equals("new")) {
                Toast.makeText(SearchActivity.this, command, Toast.LENGTH_SHORT).show();
            } else { //search

                StringBuilder searchString = new StringBuilder();
                StringBuilder searchScope = new StringBuilder();
                if (wordList.contains("in")) {
                    Log.d("SearchActivity", "SearchIn");
                    int index = wordList.indexOf("in");
                    for (int i = 1; i < index; i++) {  // skip command word
                        searchString.append(wordList.get(i)).append(" ");
                    }

                    //"In" keyword should not be the last element
                    if (index < wordList.size()) {
                        for (int i = index + 1; i < wordList.size(); i++) {
                            searchScope.append(wordList.get(i)).append(" ");
                        }
                    }

                } else {
                    Log.d("SearchActivity", "SearchAll");
                    for (int i = 1; i < wordList.size(); i++) { // skip command word
                        searchString.append(wordList.get(i)).append(" ");
                    }
                }

                Log.d("SearchActivity", "searchString: " + searchString.toString().trim());
                Log.d("SearchActivity", "searchScope: " + searchScope.toString().trim());

                Intent searchIntent = new Intent(this, SearchResultActivity.class);
                searchIntent.putExtra(SearchResultActivity.SEARCH_STRING_EXTRA, searchString.toString().trim());
                searchIntent.putExtra(SearchResultActivity.SEARCH_SCOPE_EXTRA, searchScope.toString().trim());
                startActivity(searchIntent);
            }

            //TODO: If "New xxx" launch Salesforce1 instead of our SOSL search screen
            //but Salesforce1 does not like 'new' URLs
            /*
            //New account
            String url = "https://na15.salesforce.com/001/e";

            //Account: Burlington Textiles Corp of America
            //String url = "https://na15.salesforce.com/001i000000UQbTt";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            */

        }

        finish();

        super.onActivityResult(requestCode, resultCode, data);
    }

}
