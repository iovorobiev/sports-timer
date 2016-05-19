package com.ideas.sportscounter;

import android.app.Application;

import com.ideas.sportscounter.di.AppComponent;
import com.ideas.sportscounter.di.DaggerAppComponent;

public class SportsApp extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.create();
    }

    public AppComponent getComponent() {
        return component;
    }
}
