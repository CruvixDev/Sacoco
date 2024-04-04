package com.example.sacoco.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.sacoco.data.AppRepository;
import com.example.sacoco.data.DatabaseManager;
import com.example.sacoco.data.relations.BagWithClothesRelation;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.models.ClothTypeEnum;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BagClothViewModel extends AndroidViewModel {
    private boolean isBagsDataFetched;
    private boolean isClothesDataFetched;
    private Cloth clothInCreation;
    private Bitmap clothImageTemp;
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

                        AppRepository repository = new AppRepository(databaseManager);

                        return new BagClothViewModel(repository, application);
                    }
            );

    public BagClothViewModel(AppRepository appRepository, Application application) {
        super(application);

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
     * @param weekNumber  the number of the week in the year
     * @param clothesUUID the UUID of the clothes
     * @return a completable observable to subscribe to check the adding status
     */
    public Completable addBag(int weekNumber, ArrayList<UUID> clothesUUID) {
        Bag newBagToAdd = new Bag(weekNumber);

        if (checkWeekValidity(weekNumber)) {
            Cloth clothToAdd;

            for (UUID clothUUID : clothesUUID) {
                clothToAdd = this.getClothByUUID(clothUUID);
                newBagToAdd.addClothToBag(clothToAdd, false);
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
     * @param clothName       the cloth name
     * @param clothType       the cloth type
     * @return a completable observable to subscribe to check the adding status
     */
    public Completable addCloth(String clothName, ClothTypeEnum clothType) {
        int MAX_STRING_LENGTH = 50;

        if (clothName.length() < MAX_STRING_LENGTH) {
            if (this.clothInCreation != null) {
                String clothImagePath = this.getApplication().getFilesDir().getAbsolutePath() +
                        this.clothInCreation.getClothUUID();

                this.clothInCreation.setClothName(clothName);
                this.clothInCreation.setClothType(clothType);
                this.clothInCreation.setImagePath(clothImagePath);

                return this.appRepository.saveCloth(this.clothInCreation)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> {
                            ArrayList<Cloth> clothesList = this.clothesLiveData.getValue();

                            if (clothesList != null) {
                                clothesList.add(this.clothInCreation);
                                this.clothesLiveData.setValue(clothesList);
                            }

                            Disposable disposable = this.appRepository.saveClothBitmapImage(
                                    clothImagePath, this.clothImageTemp).subscribe(
                                            () -> Log.i(this.getClass().getName(), "Image saved!"),
                                    throwable -> Log.e(this.getClass().getName(), "Failed to save image!")
                            );
                            this.compositeDisposable.add(disposable);
                        })
                        .doOnError(throwable -> Log.e(this.getClass().getName(), "Cannot add cloth"))
                        .subscribeOn(Schedulers.io());
            }
            else {
                return Completable.error(new IllegalStateException("No cloth in creation"));
            }
        }
        else {
            return Completable.error(new IllegalStateException("Cloth name too long"));
        }
    }

    /**
     * Remove a cloth into the list of clothes
     *
     * @param clothIdentifier the cloth UUID
     * @return a completable to subscribe to check the removing status
     */
    public Completable removeCloth(UUID clothIdentifier) {
        Cloth clothToRemove = getClothByUUID(clothIdentifier);

        if (clothToRemove != null) {
            return this.appRepository.removeCloth(clothToRemove)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> {
                        ArrayList<Cloth> clothesList = this.clothesLiveData.getValue();
                        ArrayList<Bag> bagsList = this.bagsLiveData.getValue();

                        if (clothesList != null && bagsList != null) {
                            for (Bag bag : bagsList) {
                                bag.removeClothInBag(clothToRemove);
                            }
                            clothesList.remove(clothToRemove);

                            this.clothesLiveData.setValue(clothesList);
                            this.bagsLiveData.setValue(bagsList);
                        }
                    })
                    .doOnError(throwable -> Log.e(this.getClass().getName(), "Cannot remove cloth!"))
                    .subscribeOn(Schedulers.io());
        }
        else {
            return Completable.error(new IllegalStateException("The cloth to remove does not exist"));
        }
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

    /**
     * Set the temporary cloth in creation
     *
     * @param clothUUID the UUID of the temporary cloth
     */
    public void setClothInCreation(UUID clothUUID) {
        this.clothInCreation = new Cloth(clothUUID);
    }

    public void clearClothInCreation() {
        this.clothInCreation = null;
    }

    /**
     * Set the temporary cloth image bitmap
     *
     * @param clothImageBitmap the temporary cloth image bitmap
     */
    public void setClothImageTemp(Bitmap clothImageBitmap) {
        this.clothImageTemp = clothImageBitmap;
    }

    public void clearClothImageTemp() {
        this.clothImageTemp = null;
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

    public Cloth getClothInCreation() {
        return this.clothInCreation;
    }

    public Bitmap getClothImageTemp() {
        return this.clothImageTemp;
    }

    /**
     * Get the cloth's image bitmap from given UUID
     * @param clothUUID the cloth's UUID
     * @return the cloth's image bitmap to get
     */
    public Observable<Bitmap> getClothImageBitmap(UUID clothUUID) {
        String clothImagePath = this.getApplication().getFilesDir().getAbsolutePath() + clothUUID;

        return this.appRepository.loadClothBitmapImage(this.getApplication(), clothImagePath);
    }

    public boolean isBagsDataFetched() {
        return this.isBagsDataFetched;
    }

    public boolean isClothesDataFetched() {
        return this.isClothesDataFetched;
    }

    public boolean isClothInCreationSet() {
        return this.clothInCreation != null;
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

                            if (!bagsWithClothesList.isEmpty()) {
                                Bag currentBag = bagsWithClothesList.get(0).bag;

                                for (BagWithClothesRelation bagWithClothes : bagsWithClothesList) {
                                    if (currentBag.getWeekNumber() != bagWithClothes.bag.getWeekNumber()) {
                                        bagsArrayList.add(currentBag);
                                        currentBag = bagWithClothes.bag;
                                    }

                                    if (bagWithClothes.cloth != null) {
                                        currentBag.addClothToBag(bagWithClothes.cloth,
                                                bagWithClothes.isClothPresent);
                                    }
                                }

                                bagsArrayList.add(currentBag);
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
        }
        else {
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
        }
        else {
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
