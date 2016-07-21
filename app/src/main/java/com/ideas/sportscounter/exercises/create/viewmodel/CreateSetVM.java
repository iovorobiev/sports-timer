package com.ideas.sportscounter.exercises.create.viewmodel;

import android.databinding.ObservableField;

import com.ideas.sportscounter.core.UserNotifier;

public class CreateSetVM {
    private final UserNotifier notifier;

    private int lastKg = 0;
    private int lastTimes = 0;
    public final ObservableField<String> setNumber = new ObservableField<>();

    public final ObservableField<String> kgCount = new ObservableField<>();
    public final ObservableField<String> timesCount = new ObservableField<>();

    public final ObservableField<String> kgIncrease = new ObservableField<>();
    public final ObservableField<String> kgPerTraining = new ObservableField<>();
    public final ObservableField<String> timesIncrease = new ObservableField<>();
    public final ObservableField<String> timesPerTraining = new ObservableField<>();

    public CreateSetVM(UserNotifier notifier, String title, int times) {
        this.notifier = notifier;
        timesCount.set(Integer.toString(times));
        setNumber.set(title);
    }
}
