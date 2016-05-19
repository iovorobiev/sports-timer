package com.ideas.sportscounter.di;

import android.content.Context;

import com.ideas.sportscounter.SportsApp;

public final class Injector {
    public static AppComponent perApp(Context context) {
        return ((SportsApp)context.getApplicationContext()).getComponent();
    }
}
