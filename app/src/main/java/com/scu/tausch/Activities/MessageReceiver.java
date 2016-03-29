package com.scu.tausch.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParsePushBroadcastReceiver;
import com.scu.tausch.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Praneet on 3/20/16.
 */
public class MessageReceiver extends ParsePushBroadcastReceiver {

    String message;
    static Context context;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        Bundle extras = intent.getExtras();
        String jsonData = extras.getString("com.parse.Data");
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
             message = jsonObject.getString("alert");

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView textView = (TextView)inflater.inflate(R.layout.textview_bubble, null);
            textView.setText(message);
            LinearLayout layout = ChatFragment.layout;

            layout.addView(textView);


        } catch (JSONException e) {
            e.printStackTrace();
        }


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
