package com.example.sacoco.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sacoco.data.converters.Converters;
import com.example.sacoco.data.dao.BagClothCrossRefDAO;
import com.example.sacoco.data.dao.BagDAO;
import com.example.sacoco.data.dao.ClothDAO;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.BagClothCrossRef;
import com.example.sacoco.models.Cloth;

@Database(entities = {Bag.class, Cloth.class, BagClothCrossRef.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DatabaseManager extends RoomDatabase {
    private static DatabaseManager databaseManagerInstance;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManagerInstance == null) {
            databaseManagerInstance = Room.databaseBuilder(context, DatabaseManager.class,
                    "App-Database").build();
        }

        return databaseManagerInstance;
    }

    public abstract BagDAO bagDAO();
    public abstract ClothDAO clothDAO();
    public abstract BagClothCrossRefDAO bagClothCrossRefDAO();
}
