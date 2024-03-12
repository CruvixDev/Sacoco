package com.example.sacoco.data;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import com.example.sacoco.data.relations.BagWithClothesRelation;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.BagClothCrossRef;
import com.example.sacoco.models.Cloth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
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
     * @return a completable object to subscribe to observe the saving status
     */
    public Completable saveBag(Bag bagToSave) {
        ArrayList<BagClothCrossRef> bagClothCrossRefArrayList = new ArrayList<>();
        BagClothCrossRef currentBagClothCrossRef;

        for (Cloth cloth : bagToSave.getClothesList()) {
            currentBagClothCrossRef = new BagClothCrossRef(bagToSave.getWeekNumber(),
                    cloth.getClothUUID());
            bagClothCrossRefArrayList.add(currentBagClothCrossRef);
        }

        return Completable.mergeArray(
                this.databaseManagerInstance.bagDAO().insertBag(bagToSave),
                this.databaseManagerInstance.bagClothCrossRefDAO().insertClothesInBag(
                        bagClothCrossRefArrayList)
        );
    }

    public Completable deleteBag(Bag bagToRemove) {
        return this.databaseManagerInstance.bagDAO().deleteBag(bagToRemove);
    }

    /**
     * Save clothes in bag into the app's storage
     * @param bag the bag where to add clothes
     * @param clothesToAdd the clothes to add into the bag
     * @return a completable object to subscribe to observe the saving status
     */
    public Completable saveClothesIntoBag(Bag bag, ArrayList<Cloth> clothesToAdd) {
        ArrayList<BagClothCrossRef> bagClothCrossRefArrayList = new ArrayList<>();

        BagClothCrossRef currentBagClothCrossRef;
        for (Cloth clothToAdd : clothesToAdd) {
            currentBagClothCrossRef = new BagClothCrossRef(bag.getWeekNumber(),
                    clothToAdd.getClothUUID());
            bagClothCrossRefArrayList.add(currentBagClothCrossRef);
        }

        return this.databaseManagerInstance.bagClothCrossRefDAO().
                insertClothesInBag(bagClothCrossRefArrayList);
    }

    /**
     * Remove a bag into the app's storage
     * @param bagToRemove the bag to remove from device
     * @return a completable object to subscribe to observe removing status
     */
    public Completable removeBag(Bag bagToRemove) {
        return null;
    }

    /**
     * Save a new cloth in the app's storage
     * @param clothToSave the cloth to save on device
     * @return a completable object to subscribe to observe saving status
     */
    public Completable saveCloth(Cloth clothToSave) {
        return null;
    }

    /**
     * Remove a cloth in app's storage
     * @param clothToRemove the cloth to remove from device
     * @return a completable object to subscribe to observe removing status
     */
    public Completable removeCloth(Cloth clothToRemove) {
        return null;
    }

    /**
     * Get all bags saved in the app's storage
     * @return an observable list of all bags in the app's storage
     */
    public Single<List<BagWithClothesRelation>> getAllBags() {
        return this.databaseManagerInstance.bagDAO().getAllBags();
    }

    /**
     * Get all clothes saved in the app's storage
     * @return an observable list of all clothes in the app's storage
     */
    public Single<List<Cloth>> getAllClothes() {
        return this.databaseManagerInstance.clothDAO().getAllClothes();
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
