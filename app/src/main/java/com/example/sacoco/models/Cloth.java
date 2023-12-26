package com.example.sacoco.models;

import android.net.Uri;

import java.util.UUID;

public class Cloth {
    private UUID clothUUID;
    private String clothName;
    private ClothTypeEnum clothType;
    private Uri imagePath;

    public Cloth(UUID clothUUID, String clothName, ClothTypeEnum clothType, Uri imagePath) {
        this.clothUUID = clothUUID;
        this.clothName = clothName;
        this.clothType = clothType;
        this.imagePath = imagePath;
    }

    public Cloth(UUID clothUUID) {
        this.clothUUID = clothUUID;
    }

    public UUID getClothUUID() {
        return clothUUID;
    }

    public String getClothName() {
        return clothName;
    }

    public ClothTypeEnum getClothType() {
        return clothType;
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public void setClothType(ClothTypeEnum clothType) {
        this.clothType = clothType;
    }

    public void setImagePath(Uri imagePath) {
        this.imagePath = imagePath;
    }
}
