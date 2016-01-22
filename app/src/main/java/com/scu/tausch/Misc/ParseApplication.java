package com.scu.tausch.Misc;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by Praneet on 1/16/16.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
