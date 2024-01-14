package com.example.sacoco.models;

import java.net.URI;

import java.util.Objects;
import java.util.UUID;

public class Cloth {
    private UUID clothUUID;
    private String clothName;
    private ClothTypeEnum clothType;
    private URI imagePath;

    public Cloth(UUID clothUUID, String clothName, ClothTypeEnum clothType, URI imagePath) {
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
