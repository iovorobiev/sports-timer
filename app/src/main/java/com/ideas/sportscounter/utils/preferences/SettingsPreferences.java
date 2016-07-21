package com.ideas.sportscounter.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ideas.sportscounter.R;

public final class SettingsPreferences extends AbstractPreferences {
    private final String voiceKey;
    private final String vibroKey;

    public SettingsPreferences(Context context) {
        super(context);
        voiceKey = context.getString(R.string.settings_voice);
        vibroKey = context.getString(R.string.settings_vibro);
    }

    @Override
    protected SharedPreferences createSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    protected String getPreferenceName() {
        return null;
    }

    public boolean isVoiceEnabled() {
        return getSharedPreferences().getBoolean(voiceKey, false);
    }

    public boolean isVibroEnabled() {
        return getSharedPreferences().getBoolean(vibroKey, false);
    }
}
