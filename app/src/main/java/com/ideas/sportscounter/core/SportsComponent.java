package com.ideas.sportscounter.core;

import com.ideas.sportscounter.core.module.SportsAppModule;
import com.ideas.sportscounter.core.module.UIModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        SportsAppModule.class,
        UIModule.class
})
public interface SportsComponent extends AppProvider {
}
