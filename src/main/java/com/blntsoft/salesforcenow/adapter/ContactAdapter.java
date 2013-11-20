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
public class ContactAdapter extends ArrayAdapter<JSONObject> {

    private static class ViewHolder {
        TextView name ;
        TextView subtitle;
        ImageView map;
        ImageView email;
        ImageView call;
    }

    public ContactAdapter(Context context) {
        super(context, R.layout.contact_item_layout);
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
            convertView = inflater.inflate(R.layout.contact_item_layout, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.contract_name);
            viewHolder.subtitle = (TextView) convertView.findViewById(R.id.contact_phone);
            viewHolder.map = (ImageView) convertView.findViewById(R.id.contact_map);
            viewHolder.email = (ImageView) convertView.findViewById(R.id.contact_email);
            viewHolder.call = (ImageView) convertView.findViewById(R.id.contact_call);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object'
        try {
            JSONObject account = object.getJSONObject("Account");
            viewHolder.name.setText(!object.isNull("Name") ? object.getString("Name") : "");
            String subtitle;
            if (object.isNull("Title") || account.isNull("Name")) {
                subtitle = (!object.isNull("Title") ? object.getString("Title") : "") + (!account.isNull("Name") ? account.getString("Name") : "");
            } else {
                subtitle = (!object.isNull("Title") ? object.getString("Title") : "") + " . " + (!account.isNull("Name") ? account.getString("Name") : "");
            }
            viewHolder.subtitle.setText(subtitle);
            viewHolder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address = "";
                    try {
                        JSONObject json = getItem(position);
                        StringBuilder builder = new StringBuilder();
                        builder.append(!json.isNull("MailingStreet")?json.getString("MailingStreet")+",":"");
                        builder.append(!json.isNull("MailingCity")?json.getString("MailingCity")+",":"");
                        builder.append(!json.isNull("MailingState")?json.getString("MailingState")+",":"");
                        builder.append(!json.isNull("MailingPostalCode")?json.getString("MailingPostalCode")+",":"");
                        builder.append(!json.isNull("MailingCity")?json.getString("MailingCity")+",":"");
                        address = builder.toString();
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(mapIntent);
                    }
                    catch (JSONException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                    }
                    catch (ActivityNotFoundException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_open_map, address), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            viewHolder.email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = "";
                    try {
                        JSONObject json = getItem(position);
                        email = json.getString("Email");
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(emailIntent);
                    }
                    catch (JSONException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                    }
                    catch (ActivityNotFoundException e) {
                        Log.e(SalesforceNowApp.LOG_TAG, null, e);
                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.unable_to_open_website, email), Toast.LENGTH_SHORT).show();
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
            Log.e("ContactAdapter", null, e);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
