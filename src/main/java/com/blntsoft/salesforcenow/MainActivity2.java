package com.blntsoft.salesforcenow;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blntsoft.salesforcenow.card.AccountCardFragment;
import com.blntsoft.salesforcenow.card.CardFragment;
import com.blntsoft.salesforcenow.card.ContactCardFragment;
import com.blntsoft.salesforcenow.card.OpportunityCardFragment;
import com.blntsoft.salesforcenow.service.SpeechActivationService;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see com.blntsoft.salesforcenow.util.SystemUiHider
 */
public class MainActivity2 extends SalesforceActivity {

    /*
     * Constants
     */

    private static final int VOICE_EVENT_ID             = 1;

    /*
     * Members
     */

    private RestClient client;
    private View rootView;
    private EditText searchText;

    /*
     * Activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout_2);

        getActionBar().hide();

        rootView = findViewById(R.id.root);
        searchText = (EditText)findViewById(R.id.ok_salesforce_edit_text);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(MainActivity2.this, SearchActivity.class);
                    intent.putExtra(SearchActivity.SEARCH_QUERY_EXTRA, searchText.getText().toString());
                    startActivity(intent);
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onResume() {
        // Hide everything until we are logged in
        rootView.setVisibility(View.INVISIBLE);

        super.onResume();
    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;

        // Show everything
        rootView.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();

        CardFragment accountCardFragment = (CardFragment)fragmentManager.findFragmentByTag(AccountCardFragment.class.getName());
        accountCardFragment.onResume(client);

        CardFragment contactCardFragment = (CardFragment)fragmentManager.findFragmentByTag(ContactCardFragment.class.getName());
        contactCardFragment.onResume(client);

        CardFragment opportunityCardFragment = (CardFragment)fragmentManager.findFragmentByTag(OpportunityCardFragment.class.getName());
        opportunityCardFragment.onResume(client);

        Intent i = SpeechActivationService.makeStartServiceIntent(this, null);
        this.startService(i);
    }

    @Override
    public void onPause() {
        Intent i = SpeechActivationService.makeServiceStopIntent(this);
        this.stopService(i);

        super.onPause();
    }


    /*
     * Specific
     */

    public void onVoiceButtonClick(View v) {
        Intent i = SpeechActivationService.makeServiceStopIntent(this);
        this.stopService(i);

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void onNewButtonClick(View v) {
        //New account
        String url = "https://na15.salesforce.com/001/e";
        //Account: Burlington Textiles Corp of America
        //String url = "https://na15.salesforce.com/001i000000UQbTt";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void onViewButtonClick(View v) {
        //Account: Burlington Textiles Corp of America
        String url = "https://na15.salesforce.com/001i000000UQbTt";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
