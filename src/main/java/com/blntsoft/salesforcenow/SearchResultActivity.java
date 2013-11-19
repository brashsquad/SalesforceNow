package com.blntsoft.salesforcenow;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blntsoft.salesforcenow.adapter.AccountAdapter;
import com.blntsoft.salesforcenow.adapter.ContactAdapter;
import com.blntsoft.salesforcenow.adapter.OpportunityAdapter;
import com.blntsoft.salesforcenow.card.AccountCardFragment;
import com.blntsoft.salesforcenow.card.CardFragment;
import com.blntsoft.salesforcenow.card.ContactCardFragment;
import com.blntsoft.salesforcenow.card.OpportunityCardFragment;
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
    public static String SEARCH_FIELDS_EXTRA = "Search Fields";

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
    }

    private String getSearchSOSL(String searchString, ArrayList<String> searchScopeList, ArrayList<String> fieldsCsv) {
        StringBuilder sosl = new StringBuilder();
        sosl.append("FIND {*").append(searchString).append("*} IN ALL FIELDS ");
        if (searchScopeList.size() > 0) {
            sosl.append("RETURNING ");

            for (int i = 0; i < searchScopeList.size(); i++) {
                sosl.append(searchScopeList.get(i)).append("(").append(fieldsCsv.get(i)).append(")");
                if (i != searchScopeList.size() - 1) {
                    sosl.append(", ");
                } else sosl.append(" ");
            }
        }
        sosl.append("LIMIT ").append(LIMIT);
        return sosl.toString();
    }



}
