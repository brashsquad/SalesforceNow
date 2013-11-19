package com.blntsoft.salesforcenow.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blntsoft.salesforcenow.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arnaud on 11/19/13.
 */
public class AccountAdapter extends ArrayAdapter<JSONObject> {

    private static class ViewHolder {
        TextView name ;
        TextView type;
    }

    public AccountAdapter(Context context) {
        super(context, R.layout.account_item_layout);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object'
        try {
            viewHolder.name.setText(object.getString("Name"));
            viewHolder.type.setText(object.getString("Type"));
        } catch (JSONException e) {
            Log.e("AccountAdapter", null, e);
        }

        // Return the completed view to render on screen
        return convertView;
    }

}
