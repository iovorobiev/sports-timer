package com.ideas.sportscounter.timer.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ideas.sportscounter.BR;
import com.ideas.sportscounter.utils.SessionPreferences;

import static com.ideas.sportscounter.timer.Timer.MILLIS_IN_SECOND;

public class CountersViewModel extends BaseObservable {
    private static final int MAX_VALUE = 99;
    private static final int SECONDS_IN_MINUTE = 60;
    private final SessionPreferences preferences;
    private int stateMinutes;
    private int stateSeconds;
    private int minutes;
    private int seconds;
    private int setNumber;

    public CountersViewModel(SessionPreferences preferences) {
        this.preferences = preferences;
        setMillis(preferences.getLastTimeMillis());
        saveState();
    }

    @Bindable
    public String getMinutes() {
        return getTimeStringFromInt(minutes);
    }

    private String getTimeStringFromInt(int time) {
        String timeString;
        if (time < 10) {
            timeString = "0" + time;
        } else  {
            timeString = Integer.toString(time);
        }
        return timeString;
    }

    @Bindable
    public String getSeconds() {
        return getTimeStringFromInt(seconds);
    }

    @Bindable
    public String getSetNumber() {
        return Integer.toString(setNumber);
    }

    private void setMinutes(int minutes) {
        if (minutes <= MAX_VALUE) {
            this.minutes = minutes;
        } else {
            this.minutes = MAX_VALUE;
        }
        notifyPropertyChanged(BR.minutes);
    }

    private void setSeconds(int seconds) {
        this.seconds = seconds % SECONDS_IN_MINUTE;
        notifyPropertyChanged(BR.seconds);
        if (seconds >= SECONDS_IN_MINUTE) {
            int newMinutes = minutes + seconds / SECONDS_IN_MINUTE;
            setMinutes(newMinutes);
        }
    }

    void setMillis(long millis) {
        setMinutes(0);
        setSeconds(Math.round(millis / MILLIS_IN_SECOND));
    }

    public long getMillis() {
        return (long) ((minutes * SECONDS_IN_MINUTE + seconds) * MILLIS_IN_SECOND);
    }

    void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
        notifyPropertyChanged(BR.setNumber);
    }

    void incSetNumber() {
        setNumber++;
        notifyPropertyChanged(BR.setNumber);
    }

    private void saveState() {
        stateMinutes = minutes;
        stateSeconds = seconds;
        preferences.putLastTimeMillis(getMillis());
    }

    void restorePrevious() {
        minutes = stateMinutes;
        seconds = stateSeconds;
        notifyPropertyChanged(BR.minutes);
        notifyPropertyChanged(BR.seconds);
    }

    public void onSecondsChosed(int time) {
        setSeconds(time);
        saveState();
    }

    public void onMinutesChosed(int time) {
        setMinutes(time);
        saveState();
    }
}
