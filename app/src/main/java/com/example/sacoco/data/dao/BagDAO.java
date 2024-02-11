package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.sacoco.models.Bag;

import java.util.List;

@Dao
public interface BagDAO {
    @Insert
    void insertBag(Bag bagToInsert);

    @Delete
    void deleteBag(Bag bagToDelete);

    @Update
    void updateBag(Bag bagToUpdate);

    @Transaction
    @Query("SELECT * FROM Bag")
    List<Bag> getAllBags();
}
