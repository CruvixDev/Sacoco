package com.example.sacoco.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.net.URI;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Cloth {
    @NonNull
    @PrimaryKey
    private final UUID clothUUID;
    private String clothName;
    private ClothTypeEnum clothType;
    private URI imagePath;

    public Cloth(@NonNull UUID clothUUID, String clothName, ClothTypeEnum clothType, URI imagePath) {
        this.clothUUID = clothUUID;
        this.clothName = clothName;
        this.clothType = clothType;
        this.imagePath = imagePath;
    }

    @Ignore
    public Cloth(@NonNull UUID clothUUID) {
        this.clothUUID = clothUUID;
    }

    @NonNull
    public UUID getClothUUID() {
        return clothUUID;
    }

    public String getClothName() {
        return clothName;
    }

    public ClothTypeEnum getClothType() {
        return clothType;
    }

    public URI getImagePath() {
        return imagePath;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public void setClothType(ClothTypeEnum clothType) {
        this.clothType = clothType;
    }

    public void setImagePath(URI imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cloth)) return false;
        Cloth cloth = (Cloth) o;
        return Objects.equals(clothUUID, cloth.clothUUID);
    }
}
