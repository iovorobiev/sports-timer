package com.ideas.sportscounter.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class AbstractPreferences {
    private final Context context;
    private SharedPreferences sharedPreferences;

    public AbstractPreferences(Context context) {
        this.context = context;
    }

    SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = createSharedPreferences();
        }
        return sharedPreferences;
    }

    protected SharedPreferences createSharedPreferences() {
        return context.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
    }

    protected abstract String getPreferenceName();

    Context getContext() {
        return context;
    }

    SharedPreferences.Editor getSharedPreferencesEdit() {
        return getSharedPreferences().edit();
    }


}
