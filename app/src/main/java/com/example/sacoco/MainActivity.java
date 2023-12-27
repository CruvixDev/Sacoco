package com.example.sacoco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.sacoco.viewmodels.BagViewModel;

public class MainActivity extends AppCompatActivity {
    private BagViewModel bagViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
        bagViewModel = new ViewModelProvider(this).get(BagViewModel.class);
    }
}