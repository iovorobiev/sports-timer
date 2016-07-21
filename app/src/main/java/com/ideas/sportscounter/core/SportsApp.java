package com.ideas.sportscounter.core;

import android.app.Application;

import com.ideas.sportscounter.BuildConfig;
import com.ideas.sportscounter.core.module.SportsAppModule;

import timber.log.Timber;


public class SportsApp extends Application {

    private SportsComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //TODO: add firebase crashreport tree
        }
        component = DaggerSportsComponent.builder()
                .sportsAppModule(new SportsAppModule(this))
                .build();
    }

    public AppProvider getComponent() {
        return component;
    }
}
