package com.easyoffer.android.easyofferapp.data;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.util.Log;

public class RefresherService extends IntentService {
    private static final String TAG = "RefresherService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.easyoffer.android.easyofferapp.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.easyoffer.android.easyofferapp.intent.extra.REFRESHING";

    public RefresherService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return ;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

       // stop refreshing when screen has loaded values // how to check?
        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
