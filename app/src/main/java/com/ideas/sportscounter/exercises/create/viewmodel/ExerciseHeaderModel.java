package com.ideas.sportscounter.exercises.create.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.util.Pair;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.core.UserNotifier;
import com.ideas.sportscounter.exercises.create.ExCreationAdapter;
import com.ideas.sportscounter.utils.TimeFormatUtils;

public class ExerciseHeaderModel {
    private final ExCreationAdapter.OnItemsAddedListener itemsAddedListener;
    private UserNotifier notifier;

    public String title;
    private int lastSets = 0;
    private int lastTimes = 0;
    public final ObservableInt setsCount = new ObservableInt();
    public final ObservableInt timesCount = new ObservableInt();
    public final ObservableField<String> minutes = new ObservableField<>();
    public final ObservableField<String> seconds = new ObservableField<>();

    public ExerciseHeaderModel(UserNotifier notifier,
                               ExCreationAdapter.OnItemsAddedListener itemsAddedListener) {
        this.notifier = notifier;
        minutes.set("0");
        seconds.set("00");
        this.itemsAddedListener = itemsAddedListener;
    }

    public void titleChanged(String newTitle) {
        title = newTitle;
    }

    public void setsCountChanged(String newSetsCount) {
        lastSets = setNewCounter(setsCount, newSetsCount, lastSets);
    }

    public void timesCountChanged(String newTimesCount) {
        lastTimes = setNewCounter(timesCount, newTimesCount, lastTimes);
    }

    public void minutesChanged(int minutes) {
        this.minutes.set(TimeFormatUtils.getMinutesString(minutes));
    }

    public void secondsChanged(int seconds) {
        Pair<Integer, Integer> result = TimeFormatUtils.getFromSeconds(seconds);
        this.seconds.set(TimeFormatUtils.getTimeStringFromInt(result.second));
        if (result.first > 0) {
            minutesChanged(result.first);
        }
    }

    public void addItems() {
        itemsAddedListener.addItems(setsCount.get(), timesCount.get());
    }

    private int setNewCounter(ObservableInt counter, String newValue, int defaultValue) {
        if (!TextUtils.isDigitsOnly(newValue) || TextUtils.isEmpty(newValue)) {
            counter.set(defaultValue);
            notifier.notifyWith(R.string.dig_only);
            return defaultValue;
        }
        counter.set(Integer.parseInt(newValue));
        return counter.get();
    }
}
