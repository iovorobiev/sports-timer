package com.ideas.sportscounter.timer;

import android.content.Context;
import android.os.Vibrator;

import com.ideas.sportscounter.utils.SettingsPreferences;

public class VibratorHelper {
    private static final int VIBRATION_PERIOD = 200;
    private static final int VIBRATION_TIME = 5;

    private long[] finalPattern = {0, 100, 100, 100};
    private boolean enabled;
    private SettingsPreferences preferences;

    private Vibrator vibrator;

    VibratorHelper(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        preferences = new SettingsPreferences(context);
    }

    void initEnabled() {
        enabled = preferences.isVibroEnabled();
    }

    public void vibrate(int seconds) {
        if (!enabled) {
            return;
        }
        if (seconds == 0) {
            vibrateFinal();
        }
        if (seconds <= VIBRATION_TIME) {
            vibrator.vibrate(VIBRATION_PERIOD);
        }
    }

    private void vibrateFinal() {
        vibrator.vibrate(finalPattern, -1);
    }
}
