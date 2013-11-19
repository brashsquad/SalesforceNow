package com.blntsoft.salesforcenow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blntsoft.salesforcenow.service.SpeechRecognizerService;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by baolongnt on 11/18/13.
 */
public class SearchResultActivity extends SalesforceActivity {

    public static String SEARCH_STRING_EXTRA = "Search String";
    public static String SEARCH_SCOPE_EXTRA = "Search Scope";

    private static final int LIMIT = 20;

    private static final String TAG = "SearchResultActivity";

    private RestClient client;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_layout);

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

        try {
            Intent i = getIntent();
            String searchString = i.getStringExtra(SEARCH_STRING_EXTRA);
            String searchScope = i.getStringExtra(SEARCH_SCOPE_EXTRA);

            if (searchString != null && !searchString.equals("")) {
                String sosl = getSearchSOSL(searchString, searchScope);

                Log.d(TAG, "SOSL: " + sosl);

                final Map<String, String> nameById = new HashMap<String, String>();
                RestRequest request = RestRequest.getRequestForSearch("v29.0", sosl);
                client.sendAsync(request, new RestClient.AsyncRequestCallback() {

                    @Override
                    public void onSuccess(RestRequest request, RestResponse response) {
                        try {

                            JSONArray result = response.asJSONArray();
                            Log.d(TAG, "Count Result: " + result.length());
                            ArrayList<String> resultList = new ArrayList<String>();
                            if (result.length() > 0) {
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject object = result.getJSONObject(i);
                                    String id = object.getString("Id");
                                    String name = object.getString("Name");
                                    Log.d(TAG, "Id: " + id);
                                    Log.d(TAG, "Name: " + name);
                                    resultList.add(name);
                                    /*JSONObject a = object.geeById.put(idtJSONObject("attributes");
                                    for (Iterator j = a.keys(); j.hasNext();) {
                                        String key = j.next().toString();
                                        String value = object.getString(key);
                                        Log.d(TAG, key + " : " + value);
                                    }*/
                                }
                            } else resultList.add("No result found !");

                            ListView l = (ListView) findViewById(R.id.listView);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchResultActivity.this, android.R.layout.simple_list_item_1, resultList.toArray(new String[0]));
                            l.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e(TAG, null, e);
                        } catch (IOException e) {
                            Log.e(TAG, null, e);
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.d(TAG, "Error");
                    }
                });

                if (nameById.size() > 0) {
                    Log.d(TAG, "MAP has " + nameById.size());
                } else Log.d(TAG, "MAP has " + nameById.size());
            }
            else {
                //NOTHING TO SEARCH
            }

        }
        catch (IOException e) {
            Log.e(SalesforceNowApp.LOG_TAG, null, e);
        }

    }

    private String getSearchAllSOSL(String searchString) {
        return this.getSearchSOSL(searchString, null);
    }

    private String getSearchSOSL(String searchString, String searchScope) {
        StringBuilder sosl = new StringBuilder();
        sosl.append("FIND {*").append(searchString).append("*} IN ALL FIELDS ");
        if (searchScope != null && !searchScope.equals("")) {
            sosl.append("RETURNING ").append(searchScope).append("(id,name) ");
        }
        sosl.append("LIMIT ").append(LIMIT);
        return sosl.toString();
    }



}
