package com.example.sacoco.fragments;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.sacoco.MainActivity;
import com.example.sacoco.R;
import com.example.sacoco.viewmodels.PreferencesViewModel;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingsFragment extends PreferenceFragmentCompat {
    private Disposable disposable;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference appVersionPreference = findPreference("preferencesAppVersion");

        PreferencesViewModel preferencesViewModel = new ViewModelProvider(requireActivity()).get(
                PreferencesViewModel.class);

        MainActivity mainActivity = (MainActivity)requireActivity();
        mainActivity.setActivityTitle(this.getString(R.string.main_activity_menu_settings));

        disposable = preferencesViewModel.getAppVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appVersion ->
                        Objects.requireNonNull(appVersionPreference).setSummary(appVersion));
    }

    @Override
    public void onPause() {
        super.onPause();

        //Unsubscribe to avoid memory leak
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
