package com.ideas.sportscounter.core.app;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.ideas.sportscounter.core.UserNotifier;

import timber.log.Timber;

public class Toaster implements UserNotifier {
    private final Context context;

    public Toaster(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void notifyWith(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Timber.e(e, "Error while notifying");
        }
    }

    @Override
    public void notifyWith(@StringRes int msgId) {
        notifyWith(getFromContext(msgId));
    }

    @Override
    public void notifyError(String msg) {
        notifyWith(msg);
    }

    @Override
    public void notifyError(@StringRes int msgId) {
        notifyWith(msgId);
    }

    @Override
    public void notifyError(Throwable t, String msg) {
        notifyWith(msg);
    }

    @Override
    public void notifyError(Throwable t, @StringRes int msgId) {
        notifyWith(msgId);
    }

    private String getFromContext(@StringRes int string) {
        try {
            return context.getString(string);
        } catch (Exception e) {
            Timber.e("Cannot get string from context");
        }
        return "";
    }
}
