package com.ideas.sportscounter.utils;

import android.databinding.BindingAdapter;
import android.support.annotation.StringRes;
import android.view.View;

import com.ideas.sportscounter.timer.TimePicker;

public class TimeBindingsAdapters {

    @BindingAdapter(value = {"onTimeChosed", "popupText"})
    public static void pickTime(final View view, final TimePicker.OnTimeChosedListener listener,
                                @StringRes final int popupText) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker.pickTime(v.getContext(), popupText, listener);
            }
        });
    }
}
