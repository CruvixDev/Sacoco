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
                childColumns = "weekNumber"
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
    public int weekNumber;
    @NonNull
    public UUID clothUUID;
}
