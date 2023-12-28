package com.example.sacoco.data;

import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;

import java.util.ArrayList;

public class AppRepository {
    private static AppRepository appRepositoryInstance;
    private DatabaseManager databaseManagerInstance;

    private AppRepository() {
        databaseManagerInstance = DatabaseManager.getInstance();
    }

    public static AppRepository getInstance() {
        if (appRepositoryInstance == null) {
            appRepositoryInstance = new AppRepository();
        }
        return appRepositoryInstance;
    }

    /**
     * Save a new bag into the app's storage
     * @param bagToSave
     * @return true if the bag has been successfully saved in the app's storage
     */
    public boolean saveBag(Bag bagToSave) {
        return true;
    }

    /**
     * Remove a bag into the app's storage
     * @param bagToRemove
     * @return true if the bag has been successfully removed in the app's storage
     */
    public boolean removeBag(Bag bagToRemove) {
        return true;
    }

    /**
     * Save a new cloth in the app's storage
     * @param clothToSave
     * @return true if the cloth has been successfully saved in the app's storage
     */
    public boolean saveCloth(Cloth clothToSave) {
        return true;
    }

    /**
     * Remove a cloth in app's storage
     * @param clothToRemove
     * @return true if th cloth has been successfully removed in the app's storage
     */
    public boolean removeCloth(Cloth clothToRemove) {
        return true;
    }

    /**
     * Get all bags saved in the app's storage
     * @return a list of all bags in the app's storage
     */
    public ArrayList<Bag> getAllBags() {
        return null;
    }

    /**
     * Get all clothes saved in the app's storage
     * @return a list of all clothes in the app's storage
     */
    public ArrayList<Cloth> getAllClothes() {
        return null;
    }
}
