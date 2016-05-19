package com.ideas.sportscounter.di;

import com.ideas.sportscounter.MainActivity;
import com.ideas.sportscounter.TimePicker;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = BindingsModule.class)
public interface AppComponent {
    TimePicker timePicker();

    void inject(MainActivity activity);
}
