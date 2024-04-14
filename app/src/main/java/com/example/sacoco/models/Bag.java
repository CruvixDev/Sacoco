package com.example.sacoco.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Entity
public class Bag {
    @PrimaryKey
    private final int weekNumber;
    private boolean isChecked;
    @Ignore
    private final HashMap<Cloth, Boolean> clothesMap;

    public Bag(int weekNumber, boolean checked) {
        this.weekNumber = weekNumber;
        this.isChecked = checked;
        this.clothesMap = new HashMap<>();
    }

    @Ignore
    public Bag(int weekNumber) {
        this.weekNumber = weekNumber;
        this.isChecked = false;
        this.clothesMap = new HashMap<>();
    }

    /**
     * Add a new cloth in the bag
     *
     * @param cloth             the cloth to add
     * @param clothPresentState the cloth present state (false the cloth is not present in the bag
     *                          true the cloth is present in the bag)
     * @return true if the cloth does not already exists in the bag false otherwise
     */
    public boolean addClothToBag(Cloth cloth, boolean clothPresentState) {
        boolean clothAlreadyExists = !this.clothesMap.containsKey(cloth);

        if (clothAlreadyExists) {
            this.clothesMap.put(cloth, clothPresentState);
        }

        return clothAlreadyExists;
    }

    /**
     * Add a set of clothes in the bag
     *
     * @param clothesList the clothes list to add
     */
    public void addClothesToBag(ArrayList<Cloth> clothesList) {
        for (Cloth cloth : clothesList) {
            this.addClothToBag(cloth, false);
        }
    }

    /**
     * Remove an existing cloth in the bag
     *
     * @param cloth the cloth to remove
     */
    public void removeClothInBag(Cloth cloth) {
        this.clothesMap.remove(cloth);
    }

    /**
     * Remove a set of existing clothes in the bag
     *
     * @param clothesToRemove the clothes to remove
     */
    public void removeClothesInBag(ArrayList<Cloth> clothesToRemove) {
        for (Cloth cloth : clothesToRemove) {
            this.removeClothInBag(cloth);
        }
    }

    /**
     * Check if the specified cloth is in the bag
     *
     * @param cloth the bag to check
     * @return true if the cloth is in the bag
     */
    public boolean isInBag(Cloth cloth) {
        return this.clothesMap.containsKey(cloth);
    }

    /**
     * Get the week number of the Bag
     *
     * @return the week number
     */
    public int getWeekNumber() {
        return this.weekNumber;
    }

    /**
     * Get all clothes into this bag
     *
     * @return the list of all clothes
     */
    public ArrayList<Cloth> getClothesList() {
        return new ArrayList<>(this.clothesMap.keySet());
    }

    /**
     * Get the presence state for clothes
     *
     * @return the list of presence state for all clothes in the Bag
     */
    public ArrayList<Boolean> getClothesPresentStateList() {
        return new ArrayList<>(this.clothesMap.values());
    }

    /**
     * Check if the bag is already checked or not
     *
     * @return the checked status of the bag
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Set the checked state of the bag
     *
     * @param checked true if the bag is checked false else
     */
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    /**
     * Modify the state
     *
     * @param cloth         the cloth to modify presence state
     * @param clothPresence the cloth presence state
     */
    public void setClothPresence(Cloth cloth, boolean clothPresence) {
        if (this.isInBag(cloth)) {
            this.clothesMap.put(cloth, clothPresence);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bag)) return false;
        Bag bag = (Bag) o;
        return Objects.equals(weekNumber, bag.weekNumber);
    }
}
