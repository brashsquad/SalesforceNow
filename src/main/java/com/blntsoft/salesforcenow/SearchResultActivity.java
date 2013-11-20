package com.blntsoft.salesforcenow;

import android.os.Bundle;
import android.view.View;

import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import java.util.ArrayList;

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

        setContentView(R.layout.search_result_layout);

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
