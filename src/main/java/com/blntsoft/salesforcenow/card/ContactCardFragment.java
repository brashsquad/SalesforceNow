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
import com.blntsoft.salesforcenow.adapter.ContactAdapter;
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

    private ArrayAdapter<JSONObject> listAdapter;

    public ContactCardFragment() {
        soql = "SELECT Id, Name, Title, Phone, Email, Account.Name, BillingCity, BillingCountry, BillingPostalCode, BillingState, BillingStreet FROM Contact ORDER BY LastViewedDate DESC LIMIT 5";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contact_card_fragment, container, false);
        listView = (ListView)rootView.findViewById(R.id.contact_list);
        listView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        listAdapter = new ContactAdapter(getActivity());
        listView.setAdapter(listAdapter);

        super.onResume();
    }

    @Override
    protected ArrayAdapter getArrayAdapter() {
        return listAdapter;
    }

}