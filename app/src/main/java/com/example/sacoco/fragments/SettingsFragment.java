package com.example.sacoco.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.sacoco.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference appVersionPreference = findPreference("preferencesAppVersion");

        try {
            PackageInfo packageInfo = requireContext().getPackageManager().getPackageInfo(
                    requireContext().getPackageName(), 0);
            String versionName = packageInfo.versionName;

            if (appVersionPreference != null) {
                appVersionPreference.setSummary(versionName);
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
