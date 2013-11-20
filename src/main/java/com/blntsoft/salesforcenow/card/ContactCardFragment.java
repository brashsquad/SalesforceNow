package com.blntsoft.salesforcenow.card;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.blntsoft.salesforcenow.R;
import com.blntsoft.salesforcenow.SalesforceNowApp;
import com.blntsoft.salesforcenow.SearchActivity;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contact_card_fragment, container, false);

        listView = (ListView)rootView.findViewById(R.id.contact_list);
        listView.setOnItemClickListener(this);

        plusImage = (ImageView)rootView.findViewById(R.id.add_action);
        plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = SearchActivity.createUrlByType.get(getType());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(webIntent);
            }
        });

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

    @Override
    protected String getType() {
        return "contact";
    }

}