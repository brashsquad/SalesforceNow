package com.blntsoft.salesforcenow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blntsoft.salesforcenow.service.SpeechRecognizerService;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import java.io.IOException;

/**
 * Created by baolongnt on 11/18/13.
 */
public class SearchResultActivity extends SalesforceActivity {

    public static String SEARCH_STRING_EXTRA        = "Search String";

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
            if (searchString != null
                        && !searchString.equals("")) {
                RestRequest request = RestRequest.getRequestForSearch("v29.0", searchString);
                client.sendAsync(request, new RestClient.AsyncRequestCallback() {
                    @Override
                    public void onSuccess(RestRequest request, RestResponse response) {

                    }

                    @Override
                    public void onError(Exception exception) {

                    }
                });
            }
            else {

            }
        }
        catch (IOException e) {
            Log.e(SalesforceNowApp.LOG_TAG, null, e);
        }

    }



}
