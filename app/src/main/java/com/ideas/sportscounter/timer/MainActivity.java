package com.ideas.sportscounter.timer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.databinding.ActivityMainBinding;
import com.ideas.sportscounter.settings.SettingsActivity;
import com.ideas.sportscounter.viewmodel.CountersViewModel;
import com.ideas.sportscounter.viewmodel.MainScreenViewModel;

public class MainActivity extends AppCompatActivity {

    private Pronouncer pronouncer;
    private VibratorHelper vibratorHelper;
    private MainScreenViewModel mainModel;

    //TODO: use dagger to inject pronouncer/vibrator
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibratorHelper = new VibratorHelper(this);
        pronouncer = new Pronouncer(this, getString(R.string.go));
        CountersViewModel countersViewModel = new CountersViewModel();
        mainModel = new MainScreenViewModel(countersViewModel,
                vibratorHelper, pronouncer);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        binding.setMain(mainModel);
        binding.setCounters(countersViewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pronouncer.initEnabled(this);
        vibratorHelper.initEnabled(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pronouncer.destroyTextToSpeech();
        mainModel.stopCount();
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
