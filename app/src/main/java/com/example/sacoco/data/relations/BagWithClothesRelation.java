package com.example.sacoco.data.relations;

import androidx.room.Embedded;

import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;

public class BagWithClothesRelation {
    @Embedded public Bag bag;
    @Embedded public Cloth cloth;
    public boolean isClothPresent;
}
