package com.example.sacoco.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Bag {
    private final int weekNumber;
    private boolean checked;
    private ArrayList<Cloth> clothesList;

    public Bag(int weekNumber, boolean checked) {
        this.weekNumber = weekNumber;
        this.checked = checked;
        this.clothesList = new ArrayList<>();
    }

    public Bag(int weekNumber) {
        this.weekNumber = weekNumber;
        this.checked = false;
        this.clothesList = new ArrayList<>();
    }

    /**
     * Add a new cloth in the bag
     * @param cloth the cloth to add
     * @return true if the cloth does not already exists in the bag false otherwise
     */
    public boolean addClothToBag(Cloth cloth) {
        boolean clothExists = !this.clothesList.contains(cloth);

        if (clothExists) {
            this.clothesList.add(cloth);
        }

        return clothExists;
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
        return checked;
    }

    /**
     * Set the checked state of the bag
     * @param checked true if the bag is checked false else
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bag)) return false;
        Bag bag = (Bag) o;
        return Objects.equals(weekNumber, bag.weekNumber);
    }
}
