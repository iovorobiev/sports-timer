package com.ideas.sportscounter.timer;

import android.content.Context;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.ideas.sportscounter.R;

public class VibratorHelper {
    private static final int VIBRATION_PERIOD = 200;
    private static final int VIBRATION_TIME = 5;

    private long[] finalPattern = {0, 100, 100, 100};
    private boolean enabled;

    private Vibrator vibrator;

    VibratorHelper(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    void initEnabled(Context context) {
        enabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context
                .getString(R.string.settings_vibro), false);
    }

    public void vibrate(float seconds) {
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
