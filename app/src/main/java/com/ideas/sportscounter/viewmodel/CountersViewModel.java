package com.ideas.sportscounter.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ideas.sportscounter.BR;

//import javax.inject.Singleton;

//@Singleton
public class CountersViewModel extends BaseObservable {
    public static final int MAX_VALUE = 99;
    public static final int SECONDS_IN_MINUTE = 60;
    public static final int MILLIS_IN_SECOND = 1000;
    private int stateMinutes;
    private int stateSeconds;
    private int minutes;
    private int seconds;
    private int setNumber;

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

    public void setMinutes(int minutes) {
        if (minutes <= MAX_VALUE) {
            this.minutes = minutes;
        } else {
            this.minutes = MAX_VALUE;
        }
        notifyPropertyChanged(BR.minutes);
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds % SECONDS_IN_MINUTE;
        notifyPropertyChanged(BR.seconds);
        if (seconds >= SECONDS_IN_MINUTE) {
            int newMinutes = minutes + seconds / SECONDS_IN_MINUTE;
            setMinutes(newMinutes);
        }
    }

    public void setMillis(long millis) {
        minutes = 0;
        setSeconds((int) (millis / MILLIS_IN_SECOND));
    }

    public long getMillis() {
        return (minutes * SECONDS_IN_MINUTE + seconds) * MILLIS_IN_SECOND;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
        notifyPropertyChanged(BR.setNumber);
    }

    public void incSetNumber() {
        setNumber++;
        notifyPropertyChanged(BR.setNumber);
    }

    public void saveState() {
        stateMinutes = minutes;
        stateSeconds = seconds;
    }

    public void restorePrevious() {
        minutes = stateMinutes;
        seconds = stateSeconds;
        notifyPropertyChanged(BR.minutes);
        notifyPropertyChanged(BR.seconds);
    }
}
