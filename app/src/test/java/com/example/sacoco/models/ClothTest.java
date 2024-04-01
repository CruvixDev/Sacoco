package com.example.sacoco.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class ClothTest {
    private Cloth clothToTest;
    private final String clothName = "Red and white shirt";
    private final ClothTypeEnum clothType = ClothTypeEnum.SHIRT;
    private final String clothImagePath = "/";

    @Before
    public void initClothTest() {
        clothToTest = new Cloth(UUID.randomUUID(), clothName, clothType, clothImagePath);
    }

    @Test
    public void getClothUUID() {
        assertNotNull(clothToTest.getClothUUID());
    }

    @Test
    public void getClothName() {
        assertEquals(clothToTest.getClothName(), clothName);
    }

    @Test
    public void getClothType() {
        assertEquals(clothToTest.getClothType(), clothType);
    }

    @Test
    public void getImagePath() {
        assertEquals(clothToTest.getImagePath(), clothImagePath);
    }

    @Test
    public void setClothName() {
        String newClothName = "Red shirt with white strips";
        clothToTest.setClothName(newClothName);
        assertEquals(clothToTest.getClothName(), newClothName);
    }

    @Test
    public void setClothType() {
        ClothTypeEnum newClothType = ClothTypeEnum.PANT;
        clothToTest.setClothType(newClothType);
        assertEquals(clothToTest.getClothType(), newClothType);
    }

    @Test
    public void setImagePath() {
        String newClothImagePath = "/android/imagePath/";
        clothToTest.setImagePath(newClothImagePath);
        assertEquals(clothToTest.getImagePath(), newClothImagePath);
    }

    @Test
    public void testEquals() {
        Cloth clothEquals = new Cloth(clothToTest.getClothUUID());
        Cloth clothNotEquals = new Cloth(UUID.randomUUID());

        assertEquals(clothToTest, clothEquals);
        assertNotEquals(clothToTest, clothNotEquals);
    }
}