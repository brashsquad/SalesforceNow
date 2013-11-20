package com.blntsoft.salesforcenow.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
        TextView subtitle;
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
            viewHolder.subtitle = (TextView) convertView.findViewById(R.id.type_name);
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

            viewHolder.subtitle.setText(subtitle);
            JSONObject json = getItem(position);
            StringBuilder builder = new StringBuilder();
            builder.append(!json.isNull("BillingStreet")?json.getString("BillingStreet")+",":"");
            builder.append(!json.isNull("BillingCity")?json.getString("BillingCity")+",":"");
            builder.append(!json.isNull("BillingState")?json.getString("BillingState")+",":"");
            builder.append(!json.isNull("BillingPostalCode")?json.getString("BillingPostalCode")+",":"");
            builder.append(!json.isNull("BillingCountry")?json.getString("BillingCountry")+",":"");
            final String address = builder.toString();
            if ("".equals(address)) {
                viewHolder.map.setVisibility(View.GONE);
            }
            else {
                viewHolder.map.setVisibility(View.VISIBLE);

                viewHolder.map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(mapIntent);
                        }
                        catch (ActivityNotFoundException e) {
                            Log.e(SalesforceNowApp.LOG_TAG, null, e);
                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_open_map, address), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            String tmpWebsite = json.isNull("Website")?"":json.getString("Website");
            if (!tmpWebsite.equals("")
                && tmpWebsite.indexOf("http://") == -1) {
                tmpWebsite = "http://" + tmpWebsite;
            }
            final String website = tmpWebsite;
            if ("".equals(website)) {
                viewHolder.web.setVisibility(View.INVISIBLE);
            }
            else {
                viewHolder.web.setVisibility(View.VISIBLE);
                viewHolder.web.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(webIntent);
                        }
                        catch (ActivityNotFoundException e) {
                            Log.e(SalesforceNowApp.LOG_TAG, null, e);
                            Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_open_website, website), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            final String phone = json.isNull("Phone")?"":json.getString("Phone");
            if ("".equals(phone)) {
                viewHolder.call.setVisibility(View.INVISIBLE);
            }
            else {
                viewHolder.call.setVisibility(View.VISIBLE);
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
            }
        } catch (JSONException e) {
            Log.e("AccountAdapter", null, e);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
