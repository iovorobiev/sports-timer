package com.ideas.sportscounter.di;

import com.ideas.sportscounter.TimePicker;
import com.ideas.sportscounter.viewmodel.CountersViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BindingsModule {

    @Provides
    @Singleton
    static CountersViewModel countersViewModel() {
        return new CountersViewModel();
    }

    @Provides
    @Singleton
    static ITimePicker timePicker() {
        return new TimePicker();
    }
}
