package com.example.sacoco.fragments;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.sacoco.R;
import com.example.sacoco.viewmodels.BagViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference appVersionPreference = findPreference("preferencesAppVersion");

        BagViewModel bagViewModel = new ViewModelProvider(requireActivity()).get(BagViewModel.class);
    }
}
