package com.example.sacoco.data;

import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;

import java.util.ArrayList;

public class AppRepository {
    private final DatabaseManager databaseManagerInstance;

    public AppRepository(DatabaseManager databaseManagerInstance) {
        this.databaseManagerInstance = databaseManagerInstance;
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
     * @return a list of all bags in the app's storage
     */
    public ArrayList<Bag> getAllBags() {
        return new ArrayList<>();
    }

    /**
     * Get all clothes saved in the app's storage
     * @return a list of all clothes in the app's storage
     */
    public ArrayList<Cloth> getAllClothes() {
        return new ArrayList<>();
    }
}
