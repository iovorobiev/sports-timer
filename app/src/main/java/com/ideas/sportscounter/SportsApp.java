package com.ideas.sportscounter;

import android.app.Application;

import timber.log.Timber;


public class SportsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //TODO: add firebase crashreport tree
        }
    }
}
