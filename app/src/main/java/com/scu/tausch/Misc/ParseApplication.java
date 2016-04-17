package com.scu.tausch.Misc;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.scu.tausch.DTO.Message;

/**
 * Created by Praneet on 1/16/16.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models here
        ParseObject.registerSubclass(Message.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        ParsePush.subscribeInBackground("Tausch");


        //We need current user many times, so need to make sure its not null.
        if (ParseUser.getCurrentUser()==null) {
            //ParseUser.enableAutomaticUser();
         //   ParseUser.getCurrentUser().saveInBackground();
        }
    }

}
