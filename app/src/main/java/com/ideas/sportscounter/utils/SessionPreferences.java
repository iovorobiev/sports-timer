package com.ideas.sportscounter.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class SessionPreferences extends AbstractPreferences {

    public static final String LTMS_KEY = "LTMS";

    public SessionPreferences(Context context) {
        super(context);
    }

    @Override
    protected String getPreferenceName() {
        return getContext().getPackageName() + getClass().getSimpleName();
    }

    public long getLastTimeMillis() {
        return getSharedPreferences().getLong(LTMS_KEY, 0);
    }

    public void putLastTimeMillis(long millis) {
        getSharedPreferencesEdit().putLong(LTMS_KEY, millis).apply();
    }
}
