package com.ideas.sportscounter.core.module;

import android.app.Application;

import com.ideas.sportscounter.core.UserNotifier;
import com.ideas.sportscounter.core.app.Toaster;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UIModule {
    @Provides
    @Singleton
    public static UserNotifier provideUserNotifier(Application context) {
        return new Toaster(context);
    }
}
