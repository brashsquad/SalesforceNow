package com.blntsoft.salesforcenow.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

import com.blntsoft.salesforcenow.SalesforceNowApp;
import com.blntsoft.salesforcenow.SearchActivity;

import root.gast.speech.activation.SpeechActivationListener;
import root.gast.speech.activation.SpeechActivator;
import root.gast.speech.activation.WordActivator;

/**
 * Derived from root.gast.speech.activation.SpeechActivationService
 * See https://github.com/gast-lib/gast-lib/blob/master/library/src/root/gast/speech/activation/SpeechActivationService.java
 */
public class SpeechActivationService extends Service implements
        SpeechActivationListener
{
    private static final String TAG = SalesforceNowApp.LOG_TAG;

    public static final String ACTIVATION_TYPE_INTENT_KEY =
            "ACTIVATION_TYPE_INTENT_KEY";

    /**
     * send this when external code wants the Service to stop
     */
    public static final String ACTIVATION_STOP_INTENT_KEY =
            "ACTIVATION_STOP_INTENT_KEY";

    private boolean isStarted;

    private SpeechActivator activator;

    @Override
    public void onCreate()
    {
        super.onCreate();
        isStarted = false;
    }

    public static Intent makeStartServiceIntent(Context context,
                                                String activationType)
    {
        Intent i = new Intent(context, SpeechActivationService.class);
        i.putExtra(ACTIVATION_TYPE_INTENT_KEY, activationType);
        return i;
    }

    public static Intent makeServiceStopIntent(Context context)
    {
        Intent i = new Intent(context, SpeechActivationService.class);
        i.putExtra(ACTIVATION_STOP_INTENT_KEY, true);
        return i;
    }

    /**
     * stop or start an activator based on the activator type and if an
     * activator is currently running
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent != null)
        {
            if (intent.hasExtra(ACTIVATION_STOP_INTENT_KEY))
            {
                Log.d(TAG, "stop service intent");
                activated(false);
            }
            else
            {
                if (isStarted)
                {
                    // the activator is currently started
                    // if the intent is requesting a new activator
                    // stop the current activator and start
                    // the new one
                    if (isDifferentType(intent))
                    {
                        Log.d(TAG, "is different type");
                        stopActivator();
                        startDetecting(intent);
                    }
                    else
                    {
                        Log.d(TAG, "already started this type");
                    }
                }
                else
                {
                    // activator not started, start it
                    startDetecting(intent);
                }
            }
        }

        // restart in case the Service gets canceled
        return START_REDELIVER_INTENT;
    }

    private void startDetecting(Intent intent)
    {
        Log.d(TAG, "extras: " + intent.getExtras().toString());
        if (activator == null)
        {
            Log.d(TAG, "null activator");
        }

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

        activator = getRequestedActivator(intent);
        Log.d(TAG, "started: " + activator.getClass().getSimpleName());
        isStarted = true;
        activator.detectActivation();
    }

    private SpeechActivator getRequestedActivator(Intent intent)
    {
        //Hack: We only listen for a single word instead for the phrase "ok Salesforce"
        String[] targetWords = new String[]{"okay", "sales", "force", "ok", "salesforce"};
        SpeechActivator speechActivator = new WordActivator(this, this, targetWords);

        return speechActivator;
    }

    /**
     * determine if the intent contains an activator type
     * that is different than the currently running type
     */
    private boolean isDifferentType(Intent intent)
    {
        boolean different = false;
        if (activator == null)
        {
            return true;
        }
        else
        {
            SpeechActivator possibleOther = getRequestedActivator(intent);
            different = !(possibleOther.getClass().getName().
                    equals(activator.getClass().getName()));
        }
        return different;
    }

    @Override
    public void activated(boolean success)
    {
        // make sure the activator is stopped before doing anything else
        stopActivator();

        Intent searchIntent = new Intent(this, SearchActivity.class);
        searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(searchIntent);

        // always stop after receive an activation
        stopSelf();
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "On destroy");
        super.onDestroy();

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);

        stopActivator();
        stopForeground(true);
    }

    private void stopActivator()
    {
        if (activator != null)
        {
            Log.d(TAG, "stopped: " + activator.getClass().getSimpleName());
            activator.stop();
            isStarted = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}