package com.blntsoft.salesforcenow.card;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blntsoft.salesforcenow.R;
import com.blntsoft.salesforcenow.SalesforceNowApp;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by baolongnt on 11/19/13.
 */
public class ContactCardFragment
        extends CardFragment {

    private ArrayAdapter<String> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView)inflater.inflate(R.layout.contact_card_fragment, container, false);
        return listView;
    }

    @Override
    public void onResume() {
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setAdapter(listAdapter);

        super.onResume();
    }

    @Override
    protected String getSoql() {
        return "SELECT Id, Name FROM Contact ORDER BY LastModifiedDate DESC LIMIT 5";
    }

    @Override
    protected ArrayAdapter getArrayAdapter() {
        return listAdapter;
    }

}