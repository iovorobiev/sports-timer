package com.ideas.sportscounter.settings;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.WindowUtils;
import com.ideas.sportscounter.timer.Pronouncer;


public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int TTS_CHECK_CODE = 101;
    private String settingsVoiceKey;
    private String settingsVibroKey;
    private Pronouncer pronouncer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        settingsVibroKey = getString(R.string.settings_vibro);
        settingsVoiceKey = getString(R.string.settings_voice);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
         if (TextUtils.equals(key, settingsVoiceKey)) {
            connectTextToSpeechIfNeed(sharedPreferences.getBoolean(key, false));
        }
    }

    private void connectTextToSpeechIfNeed(boolean enabled) {
        if (!enabled) {
            return;
        }
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_CHECK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TTS_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                pronouncer = new Pronouncer(getActivity(), getString(R.string.go));
                pronouncer.initTextToSpeech(getActivity(), new Pronouncer.InitFinishedListener() {
                    @Override
                    public void onInitFinished() {
                        pronouncer.createFiles();

                    }
                });

            } else {
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                        .putBoolean (settingsVoiceKey, false).apply();
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        if (pronouncer != null) {
            pronouncer.destroyTextToSpeech();
        }
    }
}
