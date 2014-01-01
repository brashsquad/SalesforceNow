package com.blntsoft.salesforcenow.card;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.blntsoft.salesforcenow.R;
import com.blntsoft.salesforcenow.SalesforceNowApp;
import com.blntsoft.salesforcenow.SearchActivity;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baolongnt on 11/19/13.
 */
public abstract class CardFragment
        extends Fragment
        implements AdapterView.OnItemClickListener {

    protected RestClient restClient;
    protected View rootView;
    protected ListView listView;
    protected ImageView plusImage;
    protected String soql = null;
    protected String sosl = null;

    protected abstract ArrayAdapter getArrayAdapter();
    protected abstract String getType();

    public void onResume(RestClient restClient) {
        this.restClient = restClient;
        try {
            final View spinner = rootView.findViewById(R.id.spinner);
            final View noResultFound = rootView.findViewById(R.id.no_result_found_text);
            spinner.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            noResultFound.setVisibility(View.GONE);

            RestRequest restRequest;
            if (sosl != null) {
                Log.d(SalesforceNowApp.LOG_TAG, sosl);
                restRequest = RestRequest.getRequestForSearch(getString(R.string.api_version), sosl);
                populateList(restRequest);
            }
            else if (soql != null) {
                Log.d(SalesforceNowApp.LOG_TAG, soql);
                restRequest = RestRequest.getRequestForQuery(getString(R.string.api_version), soql);
                populateList(restRequest);
            }
            else {
                StringBuilder path = new StringBuilder("/services/data/" + getString(R.string.api_version) + "/recent");
                path.append("?limit=50");
                restRequest = new RestRequest(RestRequest.RestMethod.GET, path.toString(), null);
                restClient.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {
                    @Override
                    public void onSuccess(RestRequest request, RestResponse response) {
                        Log.d(SalesforceNowApp.LOG_TAG, response.toString());
                        List<String> idList = new ArrayList<String>();
                        try {
                            JSONArray records = response.asJSONArray();
                            for (int i=0; i<records.length(); i++) {
                                JSONObject json = records.getJSONObject(i);
                                JSONObject attributes = json.getJSONObject("attributes");
                                if (getType().equalsIgnoreCase(attributes.getString("type"))) {
                                    idList.add(json.getString("Id"));
                                    if (idList.size() == 5) {
                                        break;
                                    }
                                }
                            }
                            StringBuilder idListStr = new StringBuilder();
                            for (int i=0; i<idList.size(); i++) {
                                idListStr.append('\'').append(idList.get(i)).append('\'');
                                if (i != idList.size()-1) {

                                    idListStr.append(", ");
                                }
                            }
                            soql = "SELECT " + SearchActivity.fieldsCsvByType.get(getType()) + " FROM " + getType() + " WHERE ID IN (" + idListStr.toString() + ") ORDER BY LastViewedDate DESC";
                            Log.d(SalesforceNowApp.LOG_TAG, soql);
                            populateList(RestRequest.getRequestForQuery(getString(R.string.api_version), soql ));
                        }
                        catch (Exception e) {
                            onError(e);
                        }
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, exception);
                    }
                });
            }
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

    private void populateList(RestRequest restRequest) {
        final View spinner = rootView.findViewById(R.id.spinner);
        final View noResultFound = rootView.findViewById(R.id.no_result_found_text);

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

                        rootView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        noResultFound.setVisibility(View.GONE);
                    }
                    else {
                        rootView.setVisibility(View.GONE);
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


}
