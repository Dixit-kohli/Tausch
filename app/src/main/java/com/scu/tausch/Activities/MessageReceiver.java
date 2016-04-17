package com.scu.tausch.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParsePushBroadcastReceiver;
import com.scu.tausch.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Praneet on 3/20/16.
 */
public class MessageReceiver extends ParsePushBroadcastReceiver {

    String message;
    static HomePage context;

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        Bundle extras = intent.getExtras();
        try {
            String jsonData = extras.getString("com.parse.Data");
            JSONObject jsonObject;
            jsonObject = new JSONObject(jsonData);
             message = jsonObject.getString("alert");

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels;


            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView textView = (TextView)inflater.inflate(R.layout.textview_bubble, null);
            textView.setWidth((int)dpWidth);
            textView.setBackgroundColor(Color.parseColor("#4edacf"));
            textView.setText(message);

            LinearLayout layout = ChatFragment.layout;
            if (layout != null) {
                layout.addView(textView);
            }

            // Code to add the time when message is received. I have commented the code - defect 34 from excel sheet
            TextView textViewTS = (TextView) inflater.inflate(R.layout.textview_bubble_timestamp, null);
            Date d  = new Date();
            textViewTS.setText(d.toString());
            textViewTS.setTextColor(Color.WHITE);
            textViewTS.setWidth((int) dpWidth);
            textViewTS.setBackgroundColor(Color.parseColor("#4edacf"));
            //textViewTS.setLayoutParams(params);
            if (layout != null) {
                layout.addView(textViewTS);
            }


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
