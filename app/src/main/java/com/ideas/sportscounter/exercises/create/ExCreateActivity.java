package com.ideas.sportscounter.exercises.create;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
        list.addItemDecoration(new RecyclerView.ItemDecoration() {
            Paint standardPaint = new Paint();

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                c.drawLine(0, parent.getMeasuredHeight(), parent.getMeasuredWidth(), parent.getMeasuredHeight() - 1,
                        standardPaint);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 1;
            }
        });
        list.setAdapter(new ExCreationAdapter(this, notifier));
    }
}
