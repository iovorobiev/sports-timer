package com.ideas.sportscounter;

import android.databinding.BindingAdapter;
import android.view.View;

import com.ideas.sportscounter.di.Injector;

public class BindingsAdapters {

    @BindingAdapter(value = {"type"})
    public static void pickTime(final View view, final TimeType type) {
        final TimePicker picker = Injector.perApp(view.getContext()).timePicker();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.pickTime(v.getContext(), type == TimeType.MINUTE);
            }
        });
    }

    public enum TimeType {MINUTE, SECOND};
}
