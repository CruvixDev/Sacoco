package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sacoco.data.relations.BagWithClothesRelation;
import com.example.sacoco.models.Bag;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BagDAO {
    @Insert
    Completable insertBag(Bag bagToInsert);

    @Delete
    Completable deleteBag(Bag bagToDelete);

    @Transaction
    @Query("SELECT * FROM Bag")
    Single<List<BagWithClothesRelation>> getAllBags();
}
