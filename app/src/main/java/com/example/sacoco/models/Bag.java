package com.example.sacoco.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Bag {
    private final Date startDate;
    private final Date endDate;
    private boolean checked;
    private ArrayList<Cloth> clothesList;

    public Bag(Date startDate, Date endDate, boolean checked) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.checked = checked;
        this.clothesList = new ArrayList<>();
    }

    public Bag(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Add a new cloth in the bag
     * @param cloth the cloth to add
     * @return true if the cloth does not already exists in the bag false otherwise
     */
    public boolean addClothToBag(Cloth cloth) {
        boolean clothExists = this.clothesList.contains(cloth);

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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
     * Check if the bag is already checked or not
     * @return the checked status of the bag
     */
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bag)) return false;
        Bag bag = (Bag) o;
        return Objects.equals(startDate, bag.startDate) && Objects.equals(endDate, bag.endDate);
    }
}
