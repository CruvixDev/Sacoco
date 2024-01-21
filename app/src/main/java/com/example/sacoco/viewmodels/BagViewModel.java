package com.example.sacoco.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.sacoco.data.AppRepository;
import com.example.sacoco.data.DatabaseManager;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.models.ClothTypeEnum;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class BagViewModel extends ViewModel {
    private final int MAX_STRING_LENGTH = 50;
    private final int FIRST_WEEK_NUMBER = 1;
    private final int LAST_WEEK_NUMBER = 52;
    private final MutableLiveData<Bag> selectedBagLiveData;
    private final MutableLiveData<Cloth> selectedClothLiveData;
    private final MutableLiveData<ArrayList<Cloth>> clothesLiveData;
    private final MutableLiveData<ArrayList<Bag>> bagsLiveData;
    private final AppRepository appRepositoryInstance;
    public static final ViewModelInitializer<BagViewModel> bagViewModelViewModelInitializer =
            new ViewModelInitializer<>(
                    BagViewModel.class,
                    creationExtras -> new BagViewModel(new AppRepository(new DatabaseManager()))
            );

    public BagViewModel(AppRepository appRepositoryInstance) {
        this.selectedBagLiveData = new MutableLiveData<>();
        this.selectedClothLiveData = new MutableLiveData<>();
        this.clothesLiveData = new MutableLiveData<>();
        this.bagsLiveData = new MutableLiveData<>();
        this.appRepositoryInstance = appRepositoryInstance;

        //Init the bags and clothes lists
        this.bagsLiveData.setValue(new ArrayList<>());
        this.clothesLiveData.setValue(new ArrayList<>());
    }

    /**
     * Create and add a new bag to the list of bags
     *
     * @param weekNumber the number of the week in the year
     * @param clothesIdentifiers the UUID of the cloth
     * @return true if the bag has been created and added to the list of bags false if already
     * exists or parameters invalid
     */
    public boolean addBag(int weekNumber, ArrayList<UUID> clothesIdentifiers) {
        boolean isBagExists = getBagByWeekNumber(weekNumber) == null;
        boolean isBagAdded = false;
        boolean isWeekValid = checkWeekValidity(weekNumber);

        if (isBagExists && isWeekValid) {
            Bag newBagToAdd = new Bag(weekNumber);
            Cloth clothToAdd;
            //Get the wrapped data
            ArrayList<Bag> bagsList = this.bagsLiveData.getValue();

            for (UUID clothIdentifier : clothesIdentifiers) {
                clothToAdd = getClothByUUID(clothIdentifier);

                if (clothToAdd != null) {
                    newBagToAdd.addClothToBag(clothToAdd);
                }
            }

            if (bagsList != null) {
                isBagAdded = bagsList.add(newBagToAdd);
                //Update the bags live data to update UI
                this.bagsLiveData.setValue(bagsList);
            }
        }

        return isBagAdded;
    }

    /**
     * Remove a bag into the list of bags
     *
     * @param weekNumber the number of the week in the year
     * @return true if the bag has been removed from bags list false if it does not exists
     */
    public boolean removeBag(int weekNumber) {
        Bag bagToRemove = getBagByWeekNumber(weekNumber);
        //Get the wrapped data
        ArrayList<Bag> bagsList = this.bagsLiveData.getValue();
        boolean isBagRemoved = false;

        if (bagToRemove != null && bagsList != null) {
            isBagRemoved = bagsList.remove(bagToRemove);
            //Update the bags live data to update UI
            this.bagsLiveData.setValue(bagsList);
        }

        return isBagRemoved;
    }

    /**
     * Create and add a new cloth to the list of clothes
     *
     * @param clothIdentifier the UUID of cloth
     * @param clothName       the cloth name
     * @param clothType       the cloth type
     * @param imagePath       the cloth image path on the system
     * @return true if the cloth has been created and added to the list of clothes false if it
     * already exists or parameters invalid
     */
    public boolean addCloth(UUID clothIdentifier, String clothName, ClothTypeEnum clothType,
                            URI imagePath) {
        boolean isClothExists = getClothByUUID(clothIdentifier) == null;
        boolean isClothAdded = false;

        if (isClothExists && clothName.length() <= MAX_STRING_LENGTH) {
            Cloth newClothToAdd = new Cloth(clothIdentifier, clothName, clothType, imagePath);
            //Get the wrapped data
            ArrayList<Cloth> clothesList = this.clothesLiveData.getValue();

            if (clothesList != null) {
                isClothAdded = clothesList.add(newClothToAdd);
            }

            //Update the clothes list to update the UI
            this.clothesLiveData.setValue(clothesList);
        }

        return isClothAdded;
    }

    /**
     * Remove a cloth into the list of clothes
     *
     * @param clothIdentifier the cloth UUID
     * @return true if the cloth has been removed from the list of clothes and false it does not
     * exists
     */
    public boolean removeCloth(UUID clothIdentifier) {
        Cloth clothToRemove = getClothByUUID(clothIdentifier);
        //Get the wrapped data
        ArrayList<Cloth> clothesList = this.clothesLiveData.getValue();
        boolean isClothRemoved = false;

        if (clothToRemove != null && clothesList != null) {
            isClothRemoved = clothesList.remove(clothToRemove);
            //Update the clothes list to update the UI
            this.clothesLiveData.setValue(clothesList);
        }

        return isClothRemoved;
    }

    /**
     * Add existing clothes to the selected bag into the bags list
     *
     * @param clothesIdentifiers the UUID of clothes
     * @return true if a bag is selected false otherwise
     */
    public boolean addClothesToBag(ArrayList<UUID> clothesIdentifiers) {
        boolean isBagSelected = this.selectedBagLiveData.isInitialized();

        if (isBagSelected) {
            Bag bagToAddClothes = this.selectedBagLiveData.getValue();
            Cloth clothToAdd;

            for (UUID clothIdentifier : clothesIdentifiers) {
                clothToAdd = getClothByUUID(clothIdentifier);

                if (clothToAdd != null && bagToAddClothes != null) {
                    bagToAddClothes.addClothToBag(clothToAdd);
                }
            }

            //Update the selected bag livedata to update the UI
            this.selectedBagLiveData.setValue(bagToAddClothes);
        }

        return isBagSelected;
    }

    /**
     * Remove existing clothes from selected bag into bags list
     *
     * @param clothesIdentifiers the UUID of clothes list
     * @return true if a bag is selected false otherwise
     */
    public boolean removeClothesInBag(ArrayList<UUID> clothesIdentifiers) {
        boolean isBagSelected = this.selectedBagLiveData.isInitialized();

        if (isBagSelected) {
            Bag bagToRemoveClothes = this.selectedBagLiveData.getValue();
            Cloth clothToRemove;

            for (UUID clothIdentifier : clothesIdentifiers) {
                clothToRemove = getClothByUUID(clothIdentifier);
                if (clothToRemove != null && bagToRemoveClothes != null) {
                    bagToRemoveClothes.removeClothInBag(clothToRemove);
                }
            }

            this.selectedBagLiveData.setValue(bagToRemoveClothes);
        }

        return isBagSelected;
    }

    /**
     * Set the selected bag livedata to be observed
     *
     * @param weekNumber the number of the week in the year
     */
    public void setSelectedBagLiveData(int weekNumber) {
        Bag selectedBag = getBagByWeekNumber(weekNumber);

        if (selectedBag != null) {
            this.selectedBagLiveData.setValue(selectedBag);
        }
    }

    /**
     * Set the selected cloth livedata to be observed
     *
     * @param clothIdentifier the cloth UUID
     */
    public void setSelectedClothLiveData(UUID clothIdentifier) {
        Cloth selectedCloth = getClothByUUID(clothIdentifier);

        if (selectedCloth != null) {
            this.selectedClothLiveData.setValue(selectedCloth);
        }
    }

    public LiveData<Bag> getSelectedBagLiveData() {
        return selectedBagLiveData;
    }

    public LiveData<Cloth> getSelectedClothLiveData() {
        return selectedClothLiveData;
    }

    public LiveData<ArrayList<Cloth>> getClothesLiveData() {
        return clothesLiveData;
    }

    public LiveData<ArrayList<Bag>> getBagsLiveData() {
        return bagsLiveData;
    }

    /**
     * Get cloth in the clothes list by its UUID
     *
     * @param clothIdentifier the UUID of the close to find
     * @return the cloth matching the UUID
     */
    @Nullable
    private Cloth getClothByUUID(UUID clothIdentifier) {
        Cloth clothToGet = new Cloth(clothIdentifier);
        int clothMatchingIndex = Objects.requireNonNull(this.clothesLiveData.getValue()).
                indexOf(clothToGet);

        if (clothMatchingIndex > 0) {
            return this.clothesLiveData.getValue().get(clothMatchingIndex);
        } else {
            return null;
        }
    }

    /**
     * Get bag in the bags list by its start date and end date
     *
     * @param weekNumber the number of the week in the year
     * @return the bag matching the start and end date
     */
    @Nullable
    private Bag getBagByWeekNumber(int weekNumber) {
        Bag bagToGet = new Bag(weekNumber);
        int bagMatchingIndex = Objects.requireNonNull(this.bagsLiveData.getValue()).
                indexOf(bagToGet);

        if (bagMatchingIndex > 0) {
            return this.bagsLiveData.getValue().get(bagMatchingIndex);
        } else {
            return null;
        }
    }

    /**
     * Check that the the start date and the end date are in the same week of the year and start
     * date is a monday and the end date a sunday
     *
     * @param weekNumber the number of the week in the year
     * @return true if start date is monday and end date is a sunday and start date and the end date
     * are in the same week of the year
     */
    private boolean checkWeekValidity(int weekNumber) {
        return FIRST_WEEK_NUMBER <= weekNumber && weekNumber <= LAST_WEEK_NUMBER;
    }
}
