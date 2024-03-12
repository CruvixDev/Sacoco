package com.example.sacoco.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import java.util.UUID;

@Entity(
        primaryKeys = {"weekNumber", "clothUUID"},
        foreignKeys = {
                @ForeignKey(
                entity = Bag.class,
                parentColumns = "weekNumber",
                childColumns = "weekNumber",
                onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Cloth.class,
                        parentColumns = "clothUUID",
                        childColumns = "clothUUID"
                )
        },
        indices = {@Index(value = {"clothUUID"})}
)
public class BagClothCrossRef {
    public final int weekNumber;
    @NonNull
    public final UUID clothUUID;

    public BagClothCrossRef(int weekNumber, @NonNull UUID clothUUID) {
        this.weekNumber = weekNumber;
        this.clothUUID = clothUUID;
    }
}
