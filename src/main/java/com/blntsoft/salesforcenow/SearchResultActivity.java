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

            Intent intent = getIntent();
            String searchString = intent.getStringExtra(SEARCH_STRING_EXTRA);
            ArrayList<String> searchScopeList = intent.getStringArrayListExtra(SEARCH_SCOPE_EXTRA);

            if (searchString != null && !searchString.equals("")) {

                String sosl = getSearchSOSL(searchString, searchScopeList);
                Log.d(TAG, "SOSL: " + sosl);

                ArrayList<String> result = new ArrayList<String>();
                final RestRequest request = RestRequest.getRequestForSearch("v29.0", sosl);

                client.sendAsync(request, new RestClient.AsyncRequestCallback() {
                    @Override
                    public void onSuccess(RestRequest request, RestResponse response) {
                        try {

                            JSONArray jsonResult = response.asJSONArray();
                            List<String> nameList = new ArrayList<String>();
                            Log.d("AsyncSOSLRequestCallback", "Count: " + jsonResult.length());
                            if (jsonResult.length() > 0) {
                                for (int i = 0; i < jsonResult.length(); i++) {
                                    JSONObject jsonObject = jsonResult.getJSONObject(i);
                                    String id = jsonObject.getString("Id");
                                    String name = jsonObject.getString("Name");
                                    nameList.add(name);
                                }
                            } else nameList.add("No result found !");

                            ListView l = (ListView) findViewById(R.id.listView);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchResultActivity.this, android.R.layout.simple_list_item_1, nameList.toArray(new String[0]));
                            l.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e("AsyncSOSLRequestCallback", null, e);
                        } catch (IOException e) {
                            Log.e("AsyncSOSLRequestCallback", null, e);
                        }

                    }

                    @Override
                    public void onError(Exception exception) {

                    }
                });

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

    private String getSearchSOSL(String searchString, ArrayList<String> searchScopeList) {
        StringBuilder sosl = new StringBuilder();
        sosl.append("FIND {*").append(searchString).append("*} IN ALL FIELDS ");
        if (searchScopeList.size() > 0) {
            sosl.append("RETURNING ");
            for (int i = 0; i < searchScopeList.size(); i++) {
                sosl.append(searchScopeList.get(i)).append("(id,name)");
                if (i != searchScopeList.size() - 1) {
                    sosl.append(", ");
                } else sosl.append(" ");
            }
        }
        sosl.append("LIMIT ").append(LIMIT);
        return sosl.toString();
    }



}
