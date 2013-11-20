package com.blntsoft.salesforcenow;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blntsoft.salesforcenow.card.AccountCardFragment;
import com.blntsoft.salesforcenow.card.CardFragment;
import com.blntsoft.salesforcenow.card.ContactCardFragment;
import com.blntsoft.salesforcenow.card.OpportunityCardFragment;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by baolongnt on 11/18/13.
 */
public class SearchResultActivity extends SalesforceActivity {

    public static String SEARCH_STRING_EXTRA = "Search String";
    public static String SEARCH_SCOPE_EXTRA = "Search Scope";
    public static String SEARCH_FIELDS_EXTRA = "Search Fields";

    private static final int LIMIT = 5;

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

        Intent intent = getIntent();
        String searchString = intent.getStringExtra(SEARCH_STRING_EXTRA);
        ArrayList<String> searchScopeList = intent.getStringArrayListExtra(SEARCH_SCOPE_EXTRA);
        ArrayList<String> searchFieldsCsv = intent.getStringArrayListExtra(SEARCH_FIELDS_EXTRA);

        if (searchString != null && !searchString.equals("")) {

            HashMap<String,String> tagByType = new HashMap<String, String>();
            tagByType.put("account", AccountCardFragment.class.getName());
            tagByType.put("contact", ContactCardFragment.class.getName());
            tagByType.put("opportunity", OpportunityCardFragment.class.getName());

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction trx = fragmentManager.beginTransaction();

            ArrayList<String> registeredFragment = new ArrayList<String>();
             for (int i = 0; i < searchScopeList.size(); i++) {

                String scope = searchScopeList.get(i);
                String fields = searchFieldsCsv.get(i);
                String sosl = getSearchSOSL(searchString, scope, fields);
                Log.d("SOSL", "SOSL: " + sosl);

                CardFragment cardFragment = (CardFragment)fragmentManager.findFragmentByTag(tagByType.get(scope));
                if (cardFragment != null) {
                    cardFragment.setSosl(sosl);
                    cardFragment.onResume(client);

                    registeredFragment.add(scope);
                }

            }

            for (String type : tagByType.keySet()) {
                if (!registeredFragment.contains(type)) {
                    CardFragment cardFragment = (CardFragment)fragmentManager.findFragmentByTag(tagByType.get(type));
                    if (cardFragment != null) trx.remove(cardFragment);
                }
            }

            trx.commit();

        }
        else {
            //NOTHING TO SEARCH
        }

    }

    private String getSearchSOSL(String searchString, String searchScope, String fieldsCsv) {
        StringBuilder sosl = new StringBuilder();
        sosl.append("FIND {*").append(searchString).append("*} IN ALL FIELDS ");
        sosl.append("RETURNING ").append(searchScope).append("(").append(fieldsCsv).append(") ");
        sosl.append("LIMIT ").append(LIMIT);
        return sosl.toString();
    }


}
