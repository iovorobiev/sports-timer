package com.ideas.sportscounter.timer;

public interface Timer {
    float MILLIS_IN_SECOND = 1000f;

    void start(long millis, long interval);

    void stop();

    void initAnnouncers();

    void setUiUpdateListener(TimerService.UiUpdateListener listener);
}
