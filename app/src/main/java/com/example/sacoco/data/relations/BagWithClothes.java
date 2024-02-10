package com.example.sacoco.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.sacoco.models.Bag;
import com.example.sacoco.models.BagClothCrossRef;
import com.example.sacoco.models.Cloth;

import java.util.ArrayList;

public class BagWithClothes {
    @Embedded public Bag bag;
    @Relation(
            parentColumn = "weekNumber",
            entityColumn = "clothUUID",
            associateBy = @Junction(BagClothCrossRef.class)
    )
    public ArrayList<Cloth> clothesList;
}
