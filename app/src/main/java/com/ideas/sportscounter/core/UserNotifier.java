package com.ideas.sportscounter.core;

import android.support.annotation.StringRes;
import android.support.annotation.UiThread;

public interface UserNotifier {
    @UiThread
    void notifyWith(String msg);

    @UiThread
    void notifyWith(@StringRes int msgId);

    @UiThread
    void notifyError(String msg);

    @UiThread
    void notifyError(@StringRes int msgId);

    @UiThread
    void notifyError(Throwable t, String msg);

    @UiThread
    void notifyError(Throwable t, @StringRes int msgId);
}
