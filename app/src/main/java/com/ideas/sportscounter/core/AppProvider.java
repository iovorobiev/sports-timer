package com.ideas.sportscounter.core;

import com.ideas.sportscounter.exercises.create.ExCreateActivity;

public interface AppProvider {
    UserNotifier notifier();

    void inject(ExCreateActivity createActivity);
}
