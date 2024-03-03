package com.example.sacoco.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.sacoco.models.Bag;

import java.util.List;

public class BagDiffCallback extends DiffUtil.Callback {
    private final List<Bag> oldBagsList;
    private final List<Bag> newBagsList;

    public BagDiffCallback(List<Bag> oldBagsList, List<Bag> newBagsList) {
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
