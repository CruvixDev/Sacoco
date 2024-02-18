package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.sacoco.models.BagClothCrossRef;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface BagClothCrossRefDAO {
    @Insert
    Completable insertClothInBag(BagClothCrossRef clothToInsertInBag);

    @Delete
    Completable deleteClothInBag(BagClothCrossRef clothToDeleteInBag);
}
