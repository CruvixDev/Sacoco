package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.sacoco.models.BagClothCrossRef;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface BagClothCrossRefDAO {
    @Insert
    Completable insertClothesInBag(List<BagClothCrossRef> clothesToInsertInBag);

    @Delete
    Completable deleteClothesInBag(List<BagClothCrossRef> clothesToDeleteInBag);
}
