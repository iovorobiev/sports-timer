package com.ideas.sportscounter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ideas.sportscounter.databinding.ActivityMainBinding;
import com.ideas.sportscounter.di.Injector;
import com.ideas.sportscounter.viewmodel.CountersViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private ImageButton clear;
    private TextView minutes;
    private TextView seconds;
    private boolean setsBlocked;

    private CountdownTimerZero timer;

    @Inject
    CountersViewModel countersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.perApp(this).inject(this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setCounters(countersViewModel);
//        binding.setTimepicker(timePicker);
        start = (Button) findViewById(R.id.start);
        clear = (ImageButton) findViewById(R.id.clear);
        minutes = (TextView) findViewById(R.id.timerMinutes);
        seconds = (TextView) findViewById(R.id.timerSeconds);
        //TODO: move to databindings
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isStated = TextUtils.equals(start.getText(), getString(R.string.stop));
                if (isStated) {
                    stopCount();
                } else {
                    startCount();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countersViewModel.setMinutes(0);
                countersViewModel.setSeconds(0);
                countersViewModel.setSetNumber(0);
                countersViewModel.saveState();
            }
        });
    }

    private void startCount() {
        if (!setsBlocked) {
            countersViewModel.incSetNumber();
        }
        setsBlocked = true;
        initTimer();
        start.setText(R.string.stop);
        timer.start();
    }

    private void stopCount() {
        if (timer != null) {
            timer.cancel();
        }
        start.setText(R.string.start);
    }

    private void initTimer() {
        timer = new CountdownTimerZero(countersViewModel.getMillis(), CountersViewModel
                .MILLIS_IN_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                countersViewModel.setMillis(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                start.setText(R.string.start);
                countersViewModel.restorePrevious();
                setsBlocked = false;
            }
        };
    }
}
