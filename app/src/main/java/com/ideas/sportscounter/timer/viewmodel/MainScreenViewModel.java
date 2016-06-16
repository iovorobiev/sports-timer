package com.ideas.sportscounter.timer.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.StringRes;

import com.ideas.sportscounter.BR;
import com.ideas.sportscounter.R;
import com.ideas.sportscounter.timer.Timer;
import com.ideas.sportscounter.timer.TimerService;

public class MainScreenViewModel extends BaseObservable {
    private Timer timer;
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

    public void setTimer(Timer timer) {
        this.timer = timer;
        if (timer == null) {
            return;
        }
        timer.setUiUpdateListener(new TimerService.UiUpdateListener() {
            @Override
            public void onUiUpdate(long millis) {
                countersModel.setMillis((long) (millis - Timer.MILLIS_IN_SECOND));
            }

            @Override
            public void onFinish() {
                setStartButtonText(R.string.start);
                countersModel.restorePrevious();
                setsBlocked = false;
                setTimerStarted(false);
            }
        });
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
        setStartButtonText(R.string.stop);
        if (timer != null) {
            timer.start(countersModel.getMillis(), (long) Timer.MILLIS_IN_SECOND);
            setTimerStarted(true);
        }
    }

    private void setTimerStarted(boolean timerStarted) {
        timerStated = timerStarted;
        notifyPropertyChanged(BR.timerStarted);
    }

    public void stopCount() {
        if (timer != null) {
            timer.stop();
        }
        setStartButtonText(R.string.start);
        setTimerStarted(false);
    }

    public void clearSets() {
        countersModel.setSetNumber(0);
    }
}
