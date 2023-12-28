package com.example.sacoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sacoco.viewmodels.BagViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BagViewModel bagViewModel;
    private BottomNavigationView bottomNavigationView;
    private TextView activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        activityTitle = (TextView) findViewById(R.id.mainActivityBaseTitle);

        bagViewModel = new ViewModelProvider(this).get(BagViewModel.class);
    }
}