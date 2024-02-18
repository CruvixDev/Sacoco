package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sacoco.models.Cloth;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ClothDAO {
    @Insert
    Completable insertCloth(Cloth clothToInsert);

    @Delete
    Completable deleteCloth(Cloth clothToDelete);

    @Update
    Completable updateCloth(Cloth clothToUpdate);

    @Query("SELECT * FROM Cloth")
    Single<List<Cloth>> getAllClothes();
}
