package com.example.sacoco.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sacoco.models.Cloth;

import java.util.List;

@Dao
public interface ClothDAO {
    @Insert
    void insertCloth(Cloth clothToInsert);

    @Delete
    void deleteCloth(Cloth clothToDelete);

    @Update
    void updateCloth(Cloth clothToUpdate);

    @Query("SELECT * FROM Cloth")
    List<Cloth> getAllClothes();
}
