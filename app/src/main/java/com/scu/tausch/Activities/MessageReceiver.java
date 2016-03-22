package com.scu.tausch.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Praneet on 3/20/16.
 */
public class MessageReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }
}
