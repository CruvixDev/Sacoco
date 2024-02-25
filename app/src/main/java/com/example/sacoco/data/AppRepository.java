package com.example.sacoco.data;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import com.example.sacoco.data.relations.BagWithClothesRelation;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class AppRepository {
    private final DatabaseManager databaseManagerInstance;
    private final RxDataStore<Preferences> appPreferencesDataStore;

    public AppRepository(DatabaseManager databaseManagerInstance,
                         RxDataStore<Preferences> appPreferencesDataStore) {
        this.databaseManagerInstance = databaseManagerInstance;
        this.appPreferencesDataStore = appPreferencesDataStore;
    }

    /**
     * Save a new bag into the app's storage
     * @param bagToSave the bag to save on device
     * @return true if the bag has been successfully saved in the app's storage
     */
    public boolean saveBag(Bag bagToSave) {
        return true;
    }

    /**
     * Remove a bag into the app's storage
     * @param bagToRemove the bag to remove from device
     * @return true if the bag has been successfully removed in the app's storage
     */
    public boolean removeBag(Bag bagToRemove) {
        return true;
    }

    /**
     * Save a new cloth in the app's storage
     * @param clothToSave the cloth to save on device
     * @return true if the cloth has been successfully saved in the app's storage
     */
    public boolean saveCloth(Cloth clothToSave) {
        return true;
    }

    /**
     * Remove a cloth in app's storage
     * @param clothToRemove the cloth to remove from device
     * @return true if th cloth has been successfully removed in the app's storage
     */
    public boolean removeCloth(Cloth clothToRemove) {
        return true;
    }

    /**
     * Get all bags saved in the app's storage
     * @return an observable list of all bags in the app's storage
     */
    public Single<List<BagWithClothesRelation>> getAllBags() {
        return databaseManagerInstance.bagDAO().getAllBags();
    }

    /**
     * Get all clothes saved in the app's storage
     * @return an observable list of all clothes in the app's storage
     */
    public Single<List<Cloth>> getAllClothes() {
        return databaseManagerInstance.clothDAO().getAllClothes();
    }

    /**
     * Read a preference String value from a String key
     * @param key the String key
     * @return a RxJava Flowable to observe
     */
    public Flowable<String> readStringPreference(String key) {
        Preferences.Key<String> preferenceKey = PreferencesKeys.stringKey(key);

        return appPreferencesDataStore.data().map(
                preferences -> preferences.get(preferenceKey)
        );
    }

    /**
     * Write a preference String value for a specified String key
     * @param key the String key
     * @param value the String value to write
     */
    public void writeStringPreference(String key, String value) {
        Preferences.Key<String> preferenceKey = PreferencesKeys.stringKey(key);

        appPreferencesDataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(preferenceKey, value);
            return Single.just(mutablePreferences);
        });
    }

    /**
     * Read a preference boolean value from a String key
     * @param key the String key
     * @return a RxJava Flowable to observe
     */
    public Flowable<Boolean> readBooleanPreference(String key) {
        Preferences.Key<Boolean> preferenceKey = PreferencesKeys.booleanKey(key);

        return appPreferencesDataStore.data().map(
                preferences -> preferences.get(preferenceKey)
        );
    }
}
