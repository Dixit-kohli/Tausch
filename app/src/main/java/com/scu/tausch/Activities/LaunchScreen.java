package com.scu.tausch.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;

import com.parse.ParseUser;
import com.scu.tausch.Misc.Constants;
import com.scu.tausch.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LaunchScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);



        //Showing splash screen.

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

               //Decision based on current status - login or logout.
                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser==null){
                    Intent i = new Intent(LaunchScreen.this, Login.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(LaunchScreen.this, HomePage.class);
                    startActivity(i);
                }




//                SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_PREFS_NAME, Context.MODE_PRIVATE);
//                if (sharedPreferences.getString("isLogin","false").equals("true")){
//                    Intent i = new Intent(LaunchScreen.this, HomePage.class);
//                    startActivity(i);
//                }
//                else {

               // }

                // close this activity
                finish();
            }
        }, Constants.LAUNCH_SCREEN_TIMEOUT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mainmenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
