package com.blntsoft.salesforcenow.card;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.blntsoft.salesforcenow.R;
import com.blntsoft.salesforcenow.SalesforceNowApp;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by baolongnt on 11/19/13.
 */
public abstract class CardFragment
        extends Fragment
        implements AdapterView.OnItemClickListener {

    protected RestClient restClient;
    protected View rootView;
    protected ListView listView;
    protected String soql;
    protected String sosl;

    protected abstract ArrayAdapter getArrayAdapter();

    public void onResume(RestClient restClient) {
        this.restClient = restClient;

        Log.d(SalesforceNowApp.LOG_TAG, soql);

        try {
            final View spinner = rootView.findViewById(R.id.spinner);
            final View noResultFound = rootView.findViewById(R.id.no_result_found_text);
            spinner.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            noResultFound.setVisibility(View.GONE);

            RestRequest restRequest;
            if (sosl != null) {
                restRequest = RestRequest.getRequestForSearch(getString(R.string.api_version), sosl);
            }
            else {
                restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);
            }
            restClient.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                @Override
                public void onSuccess(RestRequest request, RestResponse result) {
                    try {
                        JSONArray records = null;
                        if (sosl != null) {
                            records = result.asJSONArray();
                        } else {
                            records = result.asJSONObject().getJSONArray("records");
                        }

                        if (records.length() > 0) {
                            for (int i=0; i<records.length(); i++) {
                                JSONObject json = records.getJSONObject(i);
                                getArrayAdapter().add(json);
                            }

                            listView.setVisibility(View.VISIBLE);
                            noResultFound.setVisibility(View.GONE);
                        }
                        else {
                            listView.setVisibility(View.GONE);
                            noResultFound.setVisibility(View.VISIBLE);
                        }

                        spinner.setVisibility(View.GONE);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            JSONObject json = (JSONObject)getArrayAdapter().getItem(position);
            String recordId = json.getString("Id");
            String baseUrl = restClient.getClientInfo().instanceUrl.toString();
            String url = baseUrl + "/" + recordId;
            Log.d(SalesforceNowApp.LOG_TAG, url);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(webIntent);
        }
        catch (JSONException e) {
            Log.e(SalesforceNowApp.LOG_TAG, null, e);
        }
        catch (ActivityNotFoundException e) {
            Log.e(SalesforceNowApp.LOG_TAG, null, e);
        }
    }

    public String getSoql() {
        return soql;
    }

    public void setSoql(String soql) {
        this.soql = soql;
    }

    public String getSosl() {
        return sosl;
    }

    public void setSosl(String sosl) {
        this.sosl = sosl;
    }
}
