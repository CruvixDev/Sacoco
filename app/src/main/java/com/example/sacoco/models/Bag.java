package com.example.sacoco.models;

import java.util.ArrayList;
import java.util.Date;

public class Bag {
    private Date startDate;
    private Date endDate;
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
     *
     * @param cloth
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
     *
     * @param cloth
     * @return true if the cloth has been removed false otherwise
     */
    public boolean removeClothInBag(Cloth cloth) {
        return this.clothesList.remove(cloth);
    }

    /**
     *
     * @param cloth
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
