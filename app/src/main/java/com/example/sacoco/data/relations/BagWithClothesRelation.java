package com.example.sacoco.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.sacoco.models.Bag;
import com.example.sacoco.models.BagClothCrossRef;
import com.example.sacoco.models.Cloth;

import java.util.List;

public class BagWithClothesRelation {
    @Embedded public Bag bag;
    @Relation(
            parentColumn = "weekNumber",
            entityColumn = "clothUUID",
            associateBy = @Junction(BagClothCrossRef.class)
    )
    public List<Cloth> clothesList;
}
