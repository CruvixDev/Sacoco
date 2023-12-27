package com.example.sacoco.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class BagViewModel extends ViewModel {
    private MutableLiveData<Bag> selectedBagLiveData;
    private MutableLiveData<Cloth> selectedClothLiveData;
    private MutableLiveData<ArrayList<Cloth>> clothesLiveData;
    private MutableLiveData<ArrayList<Bag>> bagsLiveData;

    public BagViewModel() {
        selectedBagLiveData = new MutableLiveData<>();
        selectedClothLiveData = new MutableLiveData<>();
        clothesLiveData = new MutableLiveData<>();
        bagsLiveData = new MutableLiveData<>();
    }

    /**
     * Create and add a new bag to the list of bags
     * @param startDate
     * @param endDate
     * @param clothesIdentifiers the UUID of the cloth
     * @return true if the bag has been created and added to the list of bags false if already
     * exists
     */
    public boolean addBag(Date startDate, Date endDate, ArrayList<UUID> clothesIdentifiers) {
        return true;
    }

    /**
     * Remove a bag into the list of bags
     * @param startDate
     * @param endDate
     * @return true if the bag has been removed from bags list false if it does not exists
     */
    public boolean removeBag(Date startDate, Date endDate) {
        return true;
    }

    /**
     * Create and add a new cloth to the list of clothes
     * @param clothIdentifier the UUID of cloth
     * @param clothName
     * @param clothType
     * @return true if the cloth has been created and added to the list of clothes false if it
     * already exists
     */
    public boolean addCloth(UUID clothIdentifier, String clothName, String clothType) {
        return true;
    }

    /**
     * Remove a cloth into the list of clothes
     * @param clothIdentifier
     * @return true if the cloth has been removed from the list of clothes and false it does not
     * exists
     */
    public boolean removeCloth(UUID clothIdentifier) {
        return true;
    }

    /**
     * Add existing clothes to the selected bag into the bags list
     * @param startDate
     * @param endDate
     * @param clothesIdentifiers the UUID of clothes
     * @return true if the clothes have been added to the bag false if its already exists or don't
     * exist
     */
    public boolean addClothesToBag(Date startDate, Date endDate, ArrayList<UUID> clothesIdentifiers) {
        return true;
    }

    /**
     * Remove existing clothes from selected bag into bags list
     * @param startDate
     * @param endDate
     * @param clothesIdentifiers the UUID of clothes list
     * @return true if the clothes have been removed from the bag false its don't exist
     */
    public boolean removeClothesInBag(Date startDate, Date endDate, ArrayList<UUID> clothesIdentifiers) {
        return true;
    }

    /**
     * Set the selected bag livedata to be observed
     * @param startDate
     * @param endDate
     */
    public void setSelectedBagLiveData(Date startDate, Date endDate) {

    }

    /**
     * Set the selected cloth livedata to be observed
     * @param clothIdentifier
     */
    public void setSelectedClothLiveData(UUID clothIdentifier) {

    }

    public MutableLiveData<Bag> getSelectedBagLiveData() {
        return selectedBagLiveData;
    }

    public MutableLiveData<Cloth> getSelectedClothLiveData() {
        return selectedClothLiveData;
    }

    public MutableLiveData<ArrayList<Cloth>> getClothesLiveData() {
        return clothesLiveData;
    }

    public MutableLiveData<ArrayList<Bag>> getBagsLiveData() {
        return bagsLiveData;
    }
}
