package com.example.sacoco.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.example.sacoco.models.Cloth;

import java.util.ArrayList;

public class ClothDiffCallback extends DiffUtil.Callback {
    private final ArrayList<Cloth> oldClothesList;
    private final ArrayList<Cloth> newClothesList;

    public ClothDiffCallback(ArrayList<Cloth> oldClothesList, ArrayList<Cloth> newClothesList) {
        this.oldClothesList = oldClothesList;
        this.newClothesList = newClothesList;
    }

    @Override
    public int getOldListSize() {
        return this.oldClothesList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newClothesList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldClothesList.get(oldItemPosition).getClothUUID() == newClothesList.get(
                newItemPosition).getClothUUID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Cloth oldCloth = oldClothesList.get(oldItemPosition);
        Cloth newCloth = newClothesList.get(newItemPosition);

        return oldCloth.equals(newCloth);
    }
}
