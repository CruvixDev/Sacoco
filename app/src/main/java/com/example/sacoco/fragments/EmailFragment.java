package com.example.sacoco.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;

public class EmailFragment extends Fragment {
    public EmailFragment() {
        super(R.layout.fragment_email_layout);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity = (MainActivity)requireActivity();
        mainActivity.setActivityTitle(this.getString(R.string.main_activity_menu_email));
    }
}
