package com.blntsoft.salesforcenow.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blntsoft.salesforcenow.R;
import com.blntsoft.salesforcenow.SalesforceNowApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arnaud on 11/19/13.
 */
public class AccountAdapter extends ArrayAdapter<JSONObject> {

    private static class ViewHolder {
        TextView name;
        TextView type;
        ImageView map;
        ImageView web;
        ImageView call;
    }

    public AccountAdapter(Context context) {
        super(context, R.layout.account_item_layout);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        JSONObject object = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.account_item_layout, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.account_name);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type_name);
            viewHolder.map = (ImageView) convertView.findViewById(R.id.account_map);
            viewHolder.web = (ImageView) convertView.findViewById(R.id.account_web);
            viewHolder.call = (ImageView) convertView.findViewById(R.id.account_call);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object'
        try {

            viewHolder.name.setText(!object.isNull("Name") ? object.getString("Name") : "");

            String subtitle;
            if (object.isNull("Type") || object.isNull("Industry")) {
                subtitle = (!object.isNull("Type") ? object.getString("Type") : "") + (!object.isNull("Industry") ? object.getString("Industry") : "");
            } else {
                subtitle = (!object.isNull("Type") ? object.getString("Type") : "") + " . " + (!object.isNull("Industry") ? object.getString("Industry") : "");
            }

            viewHolder.type.setText(subtitle);
            viewHolder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //See http://stackoverflow.com/questions/13057463/passing-information-to-google-map-via-intent
                    //TODO: Get address and clean it a little + URL encode it(?)
                    /*try {
                        String address = "";
                        JSONObject json = getItem(position);
                        address = json.getString("Address");
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(webIntent);
                    } catch (JSONException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                    }
                    catch (ActivityNotFoundException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_map, address), Toast.LENGTH_SHORT).show();
                    }
                    */
                }
            });
            viewHolder.web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String website = "";
                    try {
                        JSONObject json = getItem(position);
                        website = json.getString("Website");
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(webIntent);
                    } catch (JSONException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                    }
                    catch (ActivityNotFoundException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_open_website, website), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            viewHolder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //WARNING: This actually dials the number and does not just bring up the dialer
                    //Dialer: use ACTION_VIEW instead
                    String phone = "";
                    try {
                        JSONObject json = getItem(position);
                        phone = json.getString("Phone");
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(callIntent);
                    }
                    catch (JSONException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                    }
                    catch (ActivityNotFoundException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_dial, phone), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            Log.e("AccountAdapter", null, e);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
