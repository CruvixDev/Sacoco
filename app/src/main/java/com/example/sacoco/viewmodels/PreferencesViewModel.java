package com.example.sacoco.viewmodels;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.sacoco.data.AppRepository;

import java.util.Objects;

import io.reactivex.rxjava3.core.Flowable;

public class PreferencesViewModel extends ViewModel {
    private final AppRepository appRepository;

    public static final ViewModelInitializer<PreferencesViewModel> preferencesViewModelInitializer =
            new ViewModelInitializer<>(
                    PreferencesViewModel.class,
                    creationExtras -> {
                        Application application = creationExtras.get(APPLICATION_KEY);

                        RxDataStore<Preferences> appPreferencesDataStore =
                                new RxPreferenceDataStoreBuilder(Objects.requireNonNull(application),
                                        "appSettings").build();

                        AppRepository repository = new AppRepository(appPreferencesDataStore);

                        return new PreferencesViewModel(repository);
                    }
            );

    public PreferencesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     * Get the App version from preferences data store
     * @return a Flowable to observe to get the App version asynchronously
     */
    public Flowable<String> getAppVersion() {
        String appVersionKey = "appVersion";
        return this.appRepository.readStringPreference(appVersionKey);
    }

    /**
     * Write the App version to preferences data store
     * @param appVersion the App version to write
     */
    public void setAppVersion(String appVersion) {
        String appVersionKey = "appVersion";
        this.appRepository.writeStringPreference(appVersionKey, appVersion);
    }
}
