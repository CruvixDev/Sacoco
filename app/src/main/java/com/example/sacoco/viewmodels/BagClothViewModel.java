package com.example.sacoco.viewmodels;

import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;
import android.util.Log;

import com.example.sacoco.data.AppRepository;
import com.example.sacoco.data.DatabaseManager;
import com.example.sacoco.data.relations.BagWithClothesRelation;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.models.ClothTypeEnum;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BagClothViewModel extends ViewModel {
    private boolean isBagsDataFetched;
    private boolean isClothesDataFetched;
    private final MutableLiveData<Bag> selectedBagLiveData;
    private final MutableLiveData<Cloth> selectedClothLiveData;
    private final MutableLiveData<ArrayList<Cloth>> clothesLiveData;
    private final MutableLiveData<ArrayList<Bag>> bagsLiveData;
    private final AppRepository appRepository;
    private final CompositeDisposable compositeDisposable;
    public static final ViewModelInitializer<BagClothViewModel> bagViewModelViewModelInitializer =
            new ViewModelInitializer<>(
                    BagClothViewModel.class,
                    creationExtras -> {
                        Application application = creationExtras.get(APPLICATION_KEY);
                        DatabaseManager databaseManager = DatabaseManager.getInstance(application);

                        RxDataStore< Preferences> appPreferencesDataStore =
                                new RxPreferenceDataStoreBuilder(Objects.requireNonNull(application),
                                        "appSettings").build();

                        AppRepository repository = new AppRepository(databaseManager,
                                appPreferencesDataStore);

                        return new BagClothViewModel(repository);
                    }
            );

    public BagClothViewModel(AppRepository appRepository) {
        this.selectedBagLiveData = new MutableLiveData<>();
        this.selectedClothLiveData = new MutableLiveData<>();
        this.clothesLiveData = new MutableLiveData<>();
        this.bagsLiveData = new MutableLiveData<>();
        this.appRepository = appRepository;
        this.compositeDisposable = new CompositeDisposable();

        //Fetch Bags and Clothes into Room database
        getAllBags();
        getAllClothes();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.compositeDisposable.dispose();
    }

    /**
     * Create and add a new bag to the list of bags
     *
     * @param weekNumber the number of the week in the year
     * @param clothesUUID the UUID of the clothes
     * @return a completable observable to subscribe to check the adding status
     */
    public Completable addBag(int weekNumber, ArrayList<UUID> clothesUUID) {
        Bag newBagToAdd = new Bag(weekNumber);

        if (checkWeekValidity(weekNumber)) {
            Cloth clothToAdd;

            for (UUID clothUUID : clothesUUID) {
                clothToAdd = this.getClothByUUID(clothUUID);
                newBagToAdd.addClothToBag(clothToAdd);
            }

            return this.appRepository.saveBag(newBagToAdd)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> {
                        ArrayList<Bag> bagsArrayList = this.bagsLiveData.getValue();
                        if (bagsArrayList != null) {
                            bagsArrayList.add(newBagToAdd);
                            this.bagsLiveData.setValue(bagsArrayList);
                        }
                    })
                    .doOnError(throwable -> Log.e(this.getClass().getName(), "Cannot create bag!"))
                    .subscribeOn(Schedulers.io());
        }
        else {
            return Completable.error(new IllegalStateException("Invalid week number"));
        }
    }

    /**
     * Remove a bag into the list of bags
     *
     * @param weekNumber the number of the week in the year
     * @return a completable to subscribe to check the removing status
     */
    public Completable removeBag(int weekNumber) {
        Bag bagToRemove = getBagByWeekNumber(weekNumber);

        if (bagToRemove != null) {
            //Update the bags live data to update UI
            return this.appRepository.deleteBag(bagToRemove)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> {
                        ArrayList<Bag> bagsArrayList = this.bagsLiveData.getValue();
                        if (bagsArrayList != null) {
                            bagsArrayList.remove(bagToRemove);
                            this.bagsLiveData.setValue(bagsArrayList);
                        }
                    })
                    .doOnError(throwable -> Log.e(this.getClass().getName(), "Cannot remove bag!"))
                    .subscribeOn(Schedulers.io());
        }
        else {
            return Completable.error(new IllegalStateException("Invalid bag"));
        }
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
        int MAX_STRING_LENGTH = 50;
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
     * @return a completable to observe to check the status of saving clothes into the selected bag
     */
    public Completable addClothesToBag(ArrayList<UUID> clothesIdentifiers) {
        boolean isBagSelected = this.selectedBagLiveData.isInitialized();

        if (isBagSelected) {
            Bag bagToAddClothes = this.selectedBagLiveData.getValue();
            ArrayList<Cloth> clothesToAdd = new ArrayList<>();

            Cloth clothToAdd;
            for (UUID clothUUID : clothesIdentifiers) {
                clothToAdd = this.getClothByUUID(clothUUID);
                clothesToAdd.add(clothToAdd);
            }

            return this.appRepository.saveClothesIntoBag(bagToAddClothes, clothesToAdd)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(
                            () -> {
                                if (bagToAddClothes != null) {
                                    bagToAddClothes.addClothesToBag(clothesToAdd);
                                }

                                this.selectedBagLiveData.setValue(bagToAddClothes);
                            }
                    )
                    .doOnError(throwable -> Log.e(this.getClass().getName(),
                            "Cannot insert clothes into the selected bag!")
                    )
                    .subscribeOn(Schedulers.io());
        }
        else {
            return Completable.error(new IllegalStateException("No bag selected"));
        }
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

    public boolean isBagsDataFetched() {
        return this.isBagsDataFetched;
    }

    public boolean isClothesDataFetched() {
        return this.isClothesDataFetched;
    }

    /**
     * Get all bags store into internal database from app repository
     */
    private void getAllBags() {
        Disposable disposable = this.appRepository.getAllBags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bagsWithClothesList -> {
                            ArrayList<Bag> bagsArrayList = new ArrayList<>();

                            for (BagWithClothesRelation bagWithClothesRelation :
                                    bagsWithClothesList) {

                                bagWithClothesRelation.bag.addClothesToBag(
                                        bagWithClothesRelation.clothesList);

                                bagsArrayList.add(bagWithClothesRelation.bag);
                            }

                            this.bagsLiveData.setValue(bagsArrayList);
                            this.isBagsDataFetched = true;
                        },
                        throwable -> this.bagsLiveData.setValue(new ArrayList<>())
                );
        this.compositeDisposable.add(disposable);
    }

    /**
     * Get all clothes store into internal database from app repository
     */
    private void getAllClothes() {
        Disposable disposable = this.appRepository.getAllClothes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        clothesList -> {
                            this.clothesLiveData.setValue(new ArrayList<>(clothesList));
                            this.isClothesDataFetched = true;
                        },
                        throwable -> this.clothesLiveData.setValue(new ArrayList<>())
                );
        this.compositeDisposable.add(disposable);
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

        if (clothMatchingIndex >= 0) {
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

        if (bagMatchingIndex >= 0) {
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
        int FIRST_WEEK_NUMBER = 1;
        int LAST_WEEK_NUMBER = 52;
        return FIRST_WEEK_NUMBER <= weekNumber && weekNumber <= LAST_WEEK_NUMBER;
    }
}
