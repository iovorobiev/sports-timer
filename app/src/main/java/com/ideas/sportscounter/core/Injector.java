package com.ideas.sportscounter.core;

import android.content.Context;

public class Injector {

    private Injector() {
        throw new IllegalStateException("No need to create instance");
    }

    public static AppProvider forApp(Context context) {
        return ((SportsApp) context.getApplicationContext()).getComponent();
    }
}
