package com.ideas.sportscounter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ideas.sportscounter.databinding.ActivityMainBinding;
import com.ideas.sportscounter.viewmodel.CountersViewModel;
import com.ideas.sportscounter.viewmodel.MainScreenViewModel;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CountersViewModel countersViewModel = new CountersViewModel();
        MainScreenViewModel mainModel = new MainScreenViewModel(countersViewModel);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainModel);
        binding.setCounters(countersViewModel);
    }
}
