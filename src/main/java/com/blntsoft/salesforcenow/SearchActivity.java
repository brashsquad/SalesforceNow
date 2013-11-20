package com.blntsoft.salesforcenow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baolongnt on 11/18/13.
 */
public class SearchActivity extends Activity {

    /*
     * Constants
     */

    private static final int VOICE_EVENT_ID             = 1;

    public static final String SEARCH_QUERY_EXTRA      = "Search Query";

    private static HashMap<String,String> fieldsCsvByType;

    static {

        fieldsCsvByType = new HashMap<String, String>();
        fieldsCsvByType.put("account", "id,name,type");
        fieldsCsvByType.put("contact", "id,name,phone");
        fieldsCsvByType.put("opportunity", "id,name,type");

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_layout);

        getActionBar().hide();

        Intent i = getIntent();
        String searchQuery =  i.getStringExtra(SEARCH_QUERY_EXTRA);
        if (searchQuery != null
                && !"".equals(searchQuery)) {
            processQuery(searchQuery);
        }
        else {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_now_hint));
            startActivityForResult(intent, VOICE_EVENT_ID);
        }
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
            processQuery(voiceResult);
        }
        finish();

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processQuery(String query) {
        Log.d(SalesforceNowApp.LOG_TAG, query);

        List<String> wordList = Arrays.asList(query);

        String command = wordList.get(0);
        if (command.equals("open")) {
            Toast.makeText(SearchActivity.this, command, Toast.LENGTH_SHORT).show();
        } else if (command.equals("new")) {
            Toast.makeText(SearchActivity.this, command, Toast.LENGTH_SHORT).show();
        } else { //search

            StringBuilder searchString = new StringBuilder();
            ArrayList<String> searchScopeList = new ArrayList<String>();
            ArrayList<String> searchFieldsList = new ArrayList<String>();

            Log.d("SearchActivity", "WordList: " + wordList);

            for (int i = 1; i < wordList.size(); i++) {
                String word = wordList.get(i);
                if (this.fieldsCsvByType.containsKey(word.toLowerCase())) {
                    searchScopeList.add(word);
                    searchFieldsList.add(fieldsCsvByType.get(word));
                } else searchString.append(word).append(" ");
            }

            if (searchScopeList.size() == 0) {
                searchScopeList = new ArrayList<String>(this.fieldsCsvByType.keySet());
                searchFieldsList = new ArrayList<String>(this.fieldsCsvByType.values());
            }

            /*if (wordList.contains("in")) {
                Log.d("SearchActivity", "SearchIn");
                int index = wordList.indexOf("in");
                for (int i = 1; i < index; i++) {  // skip command word
                    searchString.append(wordList.get(i)).append(" ");
                }

                //"In" keyword should not be the last element
                if (index < wordList.size()) {
                    for (int i = index + 1; i < wordList.size(); i++) {
                        String scopeObject = wordList.get(i);
                        if (!scopeObject.equals("and") && !scopeObject.equals("or")) {
                            searchScopeList.add(scopeObject);
                        }
                    }
                }

            } else {
                Log.d("SearchActivity", "SearchAll");
                for (int i = 1; i < wordList.size(); i++) { // skip command word
                    searchString.append(wordList.get(i)).append(" ");
                }
            }*/

            String searchStr = searchString.toString().trim();
            Log.d("SearchActivity", "searchString: " + searchStr);
            Log.d("SearchActivity", "searchScope: " + searchScopeList.toString());
            Log.d("SearchActivity", "searchField: " + searchFieldsList.toString());

            if (!searchStr.equals("")) {
                Intent searchIntent = new Intent(this, SearchResultActivity.class);
                searchIntent.putExtra(SearchResultActivity.SEARCH_STRING_EXTRA, searchStr);
                searchIntent.putExtra(SearchResultActivity.SEARCH_SCOPE_EXTRA, searchScopeList);
                searchIntent.putExtra(SearchResultActivity.SEARCH_FIELDS_EXTRA, searchFieldsList);
                startActivity(searchIntent);
            }
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

}
