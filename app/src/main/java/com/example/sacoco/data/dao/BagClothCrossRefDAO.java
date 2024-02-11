package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.sacoco.models.BagClothCrossRef;

@Dao
public interface BagClothCrossRefDAO {
    @Insert
    void insertClothInBag(BagClothCrossRef clothToInsertInBag);

    @Delete
    void deleteClothInBag(BagClothCrossRef clothToDeleteInBag);
}
