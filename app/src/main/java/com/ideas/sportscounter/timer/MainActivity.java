package com.ideas.sportscounter.timer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ideas.sportscounter.R;
import com.ideas.sportscounter.databinding.ActivityMainBinding;
import com.ideas.sportscounter.settings.SettingsActivity;
import com.ideas.sportscounter.utils.SessionPreferences;
import com.ideas.sportscounter.timer.viewmodel.CountersViewModel;
import com.ideas.sportscounter.timer.viewmodel.MainScreenViewModel;

public class MainActivity extends AppCompatActivity {
    private Pronouncer pronouncer;
    private VibratorHelper vibratorHelper;
    private MainScreenViewModel mainModel;
    private CountersViewModel countersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibratorHelper = new VibratorHelper(this);
        pronouncer = new Pronouncer(this, getString(R.string.go));

        SessionPreferences preferences = new SessionPreferences(this);
        countersViewModel = new CountersViewModel(preferences);
        mainModel = new MainScreenViewModel(countersViewModel,
                vibratorHelper, pronouncer);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainModel);
        binding.setCounters(countersViewModel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pronouncer.initEnabled();
        vibratorHelper.initEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pronouncer.destroyTextToSpeech();
        mainModel.stopCount();
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
