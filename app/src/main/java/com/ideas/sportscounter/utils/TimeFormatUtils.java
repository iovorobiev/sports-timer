package com.ideas.sportscounter.utils;

import android.util.Pair;

import static com.ideas.sportscounter.timer.Timer.MILLIS_IN_SECOND;

public class TimeFormatUtils {
    private static final int MAX_VALUE = 99;
    private static final int SECONDS_IN_MINUTE = 60;

    public static String getMinutesString(int minutes) {
        return Integer.toString(getMinutesValue(minutes));
    }

    public static int getMinutesValue(int minutes) {
        if (minutes <= MAX_VALUE) {
            return minutes;
        }
        return MAX_VALUE;
    }

    /**
     * Counts minutes and seconds from given seconds
     *
     * @param seconds
     * @return Pair where first element is minutes and second is seconds
     */
    public static Pair<Integer, Integer> getFromSeconds(int seconds) {
        int totalSeconds = seconds % SECONDS_IN_MINUTE;
        int totalMinutes = 0;
        if (seconds >= SECONDS_IN_MINUTE) {
            totalMinutes = seconds / SECONDS_IN_MINUTE;
        }
        return new Pair<>(totalMinutes, totalSeconds);
    }

    public static long getMillisFromMinsAndSecs(int mins, int secs) {
        return (long) ((mins * SECONDS_IN_MINUTE + secs) * MILLIS_IN_SECOND);
    }

    public static String getTimeStringFromInt(int time) {
        String timeString;
        if (time < 10) {
            timeString = "0" + time;
        } else {
            timeString = Integer.toString(time);
        }
        return timeString;
    }
}
