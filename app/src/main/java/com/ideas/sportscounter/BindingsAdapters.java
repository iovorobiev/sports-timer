package com.ideas.sportscounter;

import android.databinding.BindingAdapter;
import android.support.annotation.StringRes;
import android.view.View;

public class BindingsAdapters {

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

    @BindingAdapter("onClick")
    public static void onClick(View view, final OnEmptyArgumentListener listener){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callAction();
            }
        });
    }

    public interface OnEmptyArgumentListener {
        void callAction();
    }
}
