package com.ideas.sportscounter.timer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.databinding.ActivityMainBinding;
import com.ideas.sportscounter.settings.SettingsActivity;
import com.ideas.sportscounter.timer.viewmodel.CountersViewModel;
import com.ideas.sportscounter.timer.viewmodel.MainScreenViewModel;
import com.ideas.sportscounter.utils.SessionPreferences;

public class MainActivity extends AppCompatActivity {

    private MainScreenViewModel mainModel;
    private ServiceConnection timerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof TimerService.TimerBinder) {
                Timer timer = ((TimerService.TimerBinder) service).getService();
                timer.initAnnouncers();
                if (mainModel != null) {
                    mainModel.setTimer(timer);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionPreferences preferences = new SessionPreferences(this);
        CountersViewModel countersViewModel = new CountersViewModel(preferences);
        mainModel = new MainScreenViewModel(countersViewModel);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainModel);
        binding.setCounters(countersViewModel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(TimerService.getServiceIntent(this));
        bindService(TimerService.getServiceIntent(this), timerConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(timerConnection);
        mainModel.setTimer(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
