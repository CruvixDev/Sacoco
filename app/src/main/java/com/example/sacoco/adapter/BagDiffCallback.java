package com.example.sacoco.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.sacoco.models.Bag;

import java.util.ArrayList;

public class BagDiffCallback extends DiffUtil.Callback {
    private final ArrayList<Bag> oldBagsList;
    private final ArrayList<Bag> newBagsList;

    public BagDiffCallback(ArrayList<Bag> oldBagsList, ArrayList<Bag> newBagsList) {
        this.oldBagsList = oldBagsList;
        this.newBagsList = newBagsList;
    }

    @Override
    public int getOldListSize() {
        return this.oldBagsList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newBagsList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBagsList.get(oldItemPosition).getWeekNumber() == newBagsList.get(
                newItemPosition).getWeekNumber();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Bag oldBag = oldBagsList.get(oldItemPosition);
        Bag newBag = newBagsList.get(newItemPosition);

        return oldBag.equals(newBag);
    }
}
