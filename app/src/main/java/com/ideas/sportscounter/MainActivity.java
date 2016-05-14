package com.ideas.sportscounter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ideas.sportscounter.databinding.ActivityMainBinding;
import com.ideas.sportscounter.viewmodel.CountersViewModel;

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Button clear;
    private TextView minutes;
    private TextView seconds;
    private boolean setsBlocked;

    private CountdownTimerZero timer;
    private CountersViewModel countersViewModel = new CountersViewModel();
    private TimePicker timePicker = new TimePicker(countersViewModel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setCounters(countersViewModel);
        binding.setTimepicker(timePicker);
        start = (Button) findViewById(R.id.start);
        clear = (Button) findViewById(R.id.clear);
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
