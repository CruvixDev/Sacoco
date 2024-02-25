package com.example.sacoco.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Bag {
    @PrimaryKey
    private final int weekNumber;
    private boolean isChecked;
    @Ignore
    private final ArrayList<Cloth> clothesList;

    public Bag(int weekNumber, boolean checked) {
        this.weekNumber = weekNumber;
        this.isChecked = checked;
        this.clothesList = new ArrayList<>();
    }

    @Ignore
    public Bag(int weekNumber) {
        this.weekNumber = weekNumber;
        this.isChecked = false;
        this.clothesList = new ArrayList<>();
    }

    /**
     * Add a new cloth in the bag
     * @param cloth the cloth to add
     * @return true if the cloth does not already exists in the bag false otherwise
     */
    public boolean addClothToBag(Cloth cloth) {
        boolean clothAlreadyExists = !this.clothesList.contains(cloth);

        if (clothAlreadyExists) {
            this.clothesList.add(cloth);
        }

        return clothAlreadyExists;
    }

    /**
     * Add a set of clothes in the bag
     * @param clothesList the clothes list to add
     */
    public void addClothesToBag(List<Cloth> clothesList) {
        for (Cloth cloth : clothesList) {
            this.addClothToBag(cloth);
        }
    }

    /**
     * Remove an existing cloth in the bag
     * @param cloth the cloth to remove
     * @return true if the cloth has been removed false otherwise
     */
    public boolean removeClothInBag(Cloth cloth) {
        return this.clothesList.remove(cloth);
    }

    /**
     * Check if the specified cloth is in the bag
     * @param cloth the bag to check
     * @return true if the cloth is in the bag
     */
    public boolean isInBag(Cloth cloth) {
        return this.clothesList.contains(cloth);
    }

    /**
     * Get the week number of the Bag
     * @return the week number
     */
    public int getWeekNumber() {
        return this.weekNumber;
    }

    /**
     * Get all clothes into this bag
     * @return the list of all clothes
     */
    public ArrayList<Cloth> getClothesList() {
        return this.clothesList;
    }

    /**
     * Check if the bag is already checked or not
     * @return the checked status of the bag
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Set the checked state of the bag
     * @param checked true if the bag is checked false else
     */
    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bag)) return false;
        Bag bag = (Bag) o;
        return Objects.equals(weekNumber, bag.weekNumber);
    }
}
