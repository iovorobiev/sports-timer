package com.ideas.sportscounter.timer.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Pair;

import com.ideas.sportscounter.BR;
import com.ideas.sportscounter.utils.TimeFormatUtils;
import com.ideas.sportscounter.utils.preferences.SessionPreferences;

import static com.ideas.sportscounter.timer.Timer.MILLIS_IN_SECOND;

public class CountersViewModel extends BaseObservable {

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
        return TimeFormatUtils.getTimeStringFromInt(minutes);
    }

    @Bindable
    public String getSeconds() {
        return TimeFormatUtils.getTimeStringFromInt(seconds);
    }

    @Bindable
    public String getSetNumber() {
        return Integer.toString(setNumber);
    }

    private void setMinutes(int minutes) {
        this.minutes = TimeFormatUtils.getMinutesValue(minutes);
        notifyPropertyChanged(BR.minutes);
    }

    private void setSeconds(int seconds) {
        Pair<Integer, Integer> newValues = TimeFormatUtils.getFromSeconds(seconds);
        this.seconds = newValues.second;
        notifyPropertyChanged(BR.seconds);
        if (newValues.first > 0) {
            setMinutes(newValues.first);
        }
    }

    void setMillis(long millis) {
        setMinutes(0);
        setSeconds(Math.round(millis / MILLIS_IN_SECOND));
    }

    public long getMillis() {
        return TimeFormatUtils.getMillisFromMinsAndSecs(minutes, seconds);
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
