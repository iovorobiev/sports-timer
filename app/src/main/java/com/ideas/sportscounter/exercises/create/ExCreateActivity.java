package com.ideas.sportscounter.exercises.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.core.Injector;
import com.ideas.sportscounter.core.UserNotifier;

import javax.inject.Inject;

public class ExCreateActivity extends AppCompatActivity {
    @Inject
    public UserNotifier notifier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_exercise);
        Injector.forApp(this).inject(this);
        RecyclerView list = (RecyclerView) findViewById(R.id.ce_list);
        if (list == null) {
            throw new NullPointerException("Didn't find recycler view");
        }
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new ExCreationAdapter(this, notifier));
    }
}
