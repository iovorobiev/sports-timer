package com.ideas.sportscounter.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.StringRes;
import android.util.Log;

import com.ideas.sportscounter.CountdownTimerZero;
import com.ideas.sportscounter.R;
import com.ideas.sportscounter.BR;

public class MainScreenViewModel extends BaseObservable {
    private CountdownTimerZero timer;

    private boolean timerStated;

    private CountersViewModel countersModel;
    @StringRes
    private int startButtonText = R.string.start;
    private boolean setsBlocked;

    public MainScreenViewModel(CountersViewModel model) {
        countersModel = model;
    }

    @Bindable
    @StringRes
    public int getStartButtonText() {
        return startButtonText;
    }

    private void setStartButtonText(@StringRes int res) {
        startButtonText = res;
        notifyPropertyChanged(BR.startButtonText);
    }

    @Bindable
    public boolean getTimerStarted() {
        return timerStated;
    }

    public void startCount() {
        if (!setsBlocked) {
            countersModel.incSetNumber();
        }
        setsBlocked = true;
        initTimer();
        setStartButtonText(R.string.stop);
        timer.start();
        setTimerStarted(true);
    }

    private void setTimerStarted(boolean timerStarted) {
        timerStated = timerStarted;
        notifyPropertyChanged(BR.timerStarted);
    }

    public void stopCount() {
        if (timer != null) {
            timer.cancel();
        }
        setStartButtonText(R.string.start);
        setTimerStarted(false);
    }

    public void clearSets() {
        countersModel.setSetNumber(0);
    }

    private void initTimer() {
        timer = new CountdownTimerZero(countersModel.getMillis(), (long) CountersViewModel.MILLIS_IN_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                countersModel.setMillis(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                setStartButtonText(R.string.start);
                countersModel.restorePrevious();
                setsBlocked = false;
                setTimerStarted(false);
            }
        };
    }
}
