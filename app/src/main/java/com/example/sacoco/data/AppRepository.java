package com.example.sacoco.data;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.example.sacoco.data.relations.BagWithClothesRelation;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.BagClothCrossRef;
import com.example.sacoco.models.Cloth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppRepository {
    private final DatabaseManager databaseManagerInstance;
    private final RxDataStore<Preferences> appPreferencesDataStore;

    public AppRepository(RxDataStore<Preferences> appPreferencesDataStore) {
        this.databaseManagerInstance = null;
        this.appPreferencesDataStore = appPreferencesDataStore;
    }

    public AppRepository(DatabaseManager databaseManagerInstance) {
        this.databaseManagerInstance = databaseManagerInstance;
        this.appPreferencesDataStore = null;
    }

    /**
     * Save a new bag into the app's storage
     *
     * @param bagToSave the bag to save on device
     * @return a completable object to subscribe to observe the saving status
     */
    public Completable saveBag(Bag bagToSave) {
        ArrayList<BagClothCrossRef> bagClothCrossRefArrayList = new ArrayList<>();
        BagClothCrossRef currentBagClothCrossRef;

        for (Cloth cloth : bagToSave.getClothesList()) {
            currentBagClothCrossRef = new BagClothCrossRef(bagToSave.getWeekNumber(),
                    cloth.getClothUUID(), false);
            bagClothCrossRefArrayList.add(currentBagClothCrossRef);
        }

        if (this.databaseManagerInstance != null) {
            return Completable.mergeArray(
                    this.databaseManagerInstance.bagDAO().insertBag(bagToSave),
                    this.databaseManagerInstance.bagClothCrossRefDAO().insertClothesInBag(
                            bagClothCrossRefArrayList)
            );
        }
        else {
            return Completable.error(new IllegalStateException("The database instance is null"));
        }
    }

    public Completable deleteBag(Bag bagToRemove) {
        if (this.databaseManagerInstance != null) {
            return this.databaseManagerInstance.bagDAO().deleteBag(bagToRemove);
        }
        else {
            return Completable.error(new IllegalStateException("The database instance is null"));
        }
    }

    /**
     * Save clothes in bag into the app's storage
     *
     * @param bag          the bag where to add clothes
     * @param clothesToAdd the clothes to add into the bag
     * @return a completable object to subscribe to observe the saving status
     */
    public Completable saveClothesIntoBag(Bag bag, ArrayList<Cloth> clothesToAdd) {
        ArrayList<BagClothCrossRef> bagClothCrossRefArrayList = new ArrayList<>();

        BagClothCrossRef currentBagClothCrossRef;
        for (Cloth clothToAdd : clothesToAdd) {
            currentBagClothCrossRef = new BagClothCrossRef(bag.getWeekNumber(),
                    clothToAdd.getClothUUID(), false);
            bagClothCrossRefArrayList.add(currentBagClothCrossRef);
        }

        if (this.databaseManagerInstance != null) {
            return this.databaseManagerInstance.bagClothCrossRefDAO().
                    insertClothesInBag(bagClothCrossRefArrayList);
        }
        else {
            return Completable.error(new IllegalStateException("The database instance is null"));
        }
    }

    /**
     * Save a new cloth in the app's storage
     *
     * @param clothToSave the cloth to save on device
     * @return a completable object to subscribe to observe saving status
     */
    public Completable saveCloth(Cloth clothToSave) {
        if (this.databaseManagerInstance != null) {
            return this.databaseManagerInstance.clothDAO().insertCloth(clothToSave);
        }
        else {
            return Completable.error(new IllegalStateException("The database instance is null"));
        }
    }

    /**
     * Remove a cloth in app's storage
     *
     * @param clothToRemove the cloth to remove from device
     * @return a completable object to subscribe to observe removing status
     */
    public Completable removeCloth(Cloth clothToRemove) {
        if (this.databaseManagerInstance != null) {
            return this.databaseManagerInstance.clothDAO().deleteCloth(clothToRemove);
        }
        else {
            return Completable.error(new IllegalStateException("The database instance is null"));
        }
    }

    /**
     * Get all bags saved in the app's storage
     *
     * @return an observable list of all bags in the app's storage
     */
    public Single<List<BagWithClothesRelation>> getAllBags() {
        if (this.databaseManagerInstance != null) {
            return this.databaseManagerInstance.bagDAO().getAllBags();
        }
        else {
            return Single.error(new IllegalStateException("The database instance is null"));
        }
    }

    /**
     * Get all clothes saved in the app's storage
     *
     * @return an observable list of all clothes in the app's storage
     */
    public Single<List<Cloth>> getAllClothes() {
        if (this.databaseManagerInstance != null) {
            return this.databaseManagerInstance.clothDAO().getAllClothes();
        }
        else {
            return Single.error(new IllegalStateException("The database instance is null"));
        }
    }

    /**
     * Read a preference String value from a String key
     *
     * @param key the String key
     * @return a RxJava Flowable to observe
     */
    public Flowable<String> readStringPreference(String key) {
        Preferences.Key<String> preferenceKey = PreferencesKeys.stringKey(key);

        if (appPreferencesDataStore != null) {
            return appPreferencesDataStore.data().map(
                    preferences -> preferences.get(preferenceKey)
            );
        }
        else {
            return Flowable.error(new IllegalStateException("The preference datastore instance is null"));
        }
    }

    /**
     * Write a preference String value for a specified String key
     *
     * @param key   the String key
     * @param value the String value to write
     */
    public void writeStringPreference(String key, String value) {
        Preferences.Key<String> preferenceKey = PreferencesKeys.stringKey(key);

        if (appPreferencesDataStore != null) {
            appPreferencesDataStore.updateDataAsync(preferences -> {
                MutablePreferences mutablePreferences = preferences.toMutablePreferences();
                mutablePreferences.set(preferenceKey, value);
                return Single.just(mutablePreferences);
            });
        }
    }

    /**
     * Read a preference boolean value from a String key
     *
     * @param key the String key
     * @return a RxJava Flowable to observe
     */
    public Flowable<Boolean> readBooleanPreference(String key) {
        Preferences.Key<Boolean> preferenceKey = PreferencesKeys.booleanKey(key);

        if (appPreferencesDataStore != null) {
            return appPreferencesDataStore.data().map(
                    preferences -> preferences.get(preferenceKey)
            );
        }
        else {
            return Flowable.error(new IllegalStateException("The preference datastore instance is null"));
        }
    }

    /**
     * Save cloth's image bitmap asynchronously
     *
     * @param application the application context
     * @param cloth the cloth from which the bitmap image originates
     * @param clothBitmapImage the cloth's image bitmap to save
     * @return a completable to observe the saving status
     */
    public Completable saveClothBitmapImage(Application application, Cloth cloth, Bitmap clothBitmapImage) {
        return Completable.fromAction(() -> {
            File clothImageFile = new File(application.getFilesDir(), cloth.getClothUUID()
                    + ".jpeg");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(clothImageFile);
                clothBitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            }
            catch (IOException e) {
                Log.e(this.getClass().getName(), e.toString());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    /**
     * Delete the cloth's image bitmap asynchronously
     *
     * @param application the application context
     * @param cloth the cloth from which we want to delete bitmap image
     * @return a completable to observe the removing status
     */
    public Completable deleteClothBitmapImage(Application application, Cloth cloth) {
        return Completable.fromAction(() -> {
            File clothImageFile = new File(application.getFilesDir(), cloth.getClothUUID()
                    + ".jpeg");

            if (clothImageFile.exists()) {
                boolean isClothDeleted = clothImageFile.delete();

                if (isClothDeleted) {
                    Log.i(this.getClass().getName(), "The file has been deleted");
                }
                else {
                    Log.e(this.getClass().getName(), "The file has not been deleted");
                }
            }
            else {
                Log.e(this.getClass().getName(), "The file does not exist!");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    /**
     * Load cloth's image bitmap asynchronously
     *
     * @param application the application context
     * @param cloth the cloth whose image we want to load
     * @return an observable on cloth's bitmap image
     */
    public Observable<Bitmap> loadClothBitmapImage(Application application, Cloth cloth) {
        return Observable.fromCallable(() -> {
            FutureTarget<Bitmap> target = Glide.with(application)
                    .asBitmap()
                    .load(new File(application.getFilesDir(), cloth.getClothUUID() + ".jpeg"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .submit();
            return target.get();
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }
}
