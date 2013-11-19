package com.blntsoft.salesforcenow.card;

import android.app.Fragment;
import android.app.ListFragment;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blntsoft.salesforcenow.R;
import com.blntsoft.salesforcenow.SalesforceNowApp;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by baolongnt on 11/19/13.
 */
public abstract class CardFragment extends Fragment {

    protected RestClient restClient;
    protected ListView listView;

    protected abstract String getSoql();
    protected abstract ArrayAdapter getArrayAdapter();

    public void onResume(RestClient restClient) {
        this.restClient = restClient;

        String soql = getSoql();
        Log.d(SalesforceNowApp.LOG_TAG, soql);

        try {
            RestRequest restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);
            restClient.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, RestResponse result) {
                    try {
                        JSONArray records = result.asJSONObject().getJSONArray("records");
                        for (int i=0; i<records.length(); i++) {
                            JSONObject json = records.getJSONObject(i);
                            getArrayAdapter().add(json.getString("Name"));
                        }
                    } catch (Exception e) {
                        onError(e);
                    }
                }

                @Override
                public void onError(Exception exception) {
                    Log.e(SalesforceNowApp.LOG_TAG, null, exception);
                }
            });
        }
        catch (UnsupportedEncodingException e) {
            Log.e(SalesforceNowApp.LOG_TAG, null, e);
        }
    }


}
