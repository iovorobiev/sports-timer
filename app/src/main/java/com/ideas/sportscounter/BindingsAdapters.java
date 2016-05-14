package com.ideas.sportscounter;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingsAdapters {
    @BindingAdapter(value = {"type"})
    public static void pickTime(View view, TimeType type) {

    }

    enum TimeType {MINUTE, SECOND};
}
