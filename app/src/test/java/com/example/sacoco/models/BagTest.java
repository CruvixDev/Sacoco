package com.example.sacoco.models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.UUID;

public class BagTest {
    private final int WEEK_NUMBER_1 = 1;
    private final int WEEK_NUMBER_52 = 52;
    private Bag bagToTest;
    private Bag bagToTestChecked;
    private Cloth redPant;
    private Cloth blueShirt;
    private Cloth brownShoe;
    private Cloth greySocks;

    @Before
    public void initBagTest() {
        bagToTest = new Bag(WEEK_NUMBER_1);
        bagToTestChecked = new Bag(WEEK_NUMBER_1, true);

        redPant = new Cloth(UUID.randomUUID(), "Red pant",
                ClothTypeEnum.PANT, URI.create("/"));
        blueShirt = new Cloth(UUID.randomUUID(), "Blue shirt",
                ClothTypeEnum.SHIRT, URI.create("/"));
        brownShoe = new Cloth(UUID.randomUUID(), "Brown shoe",
                ClothTypeEnum.SHOE, URI.create("/"));
        greySocks = new Cloth(UUID.randomUUID(), "Grey socks",
                ClothTypeEnum.SOCK, URI.create("/"));
    }

    @Test
    public void addClothToBag() {
        assertTrue(bagToTest.addClothToBag(redPant, false));
        assertTrue(bagToTest.addClothToBag(blueShirt, false));
        assertTrue(bagToTest.addClothToBag(brownShoe, false));
        assertTrue(bagToTest.addClothToBag(greySocks, false));

        //Try again to add clothes into bag
        assertFalse(bagToTest.addClothToBag(redPant, false));
        assertFalse(bagToTest.addClothToBag(blueShirt, false));
        assertFalse(bagToTest.addClothToBag(brownShoe, false));
        assertFalse(bagToTest.addClothToBag(greySocks, false));

        assertEquals(bagToTest.getClothesList().size(), 4);
    }

    @Test
    public void removeClothInBag() {
        assertTrue(bagToTest.addClothToBag(redPant, false));
        assertTrue(bagToTest.addClothToBag(blueShirt, false));
        assertTrue(bagToTest.addClothToBag(greySocks, false));

        assertTrue(bagToTest.removeClothInBag(redPant));
        assertTrue(bagToTest.removeClothInBag(blueShirt));
        assertFalse(bagToTest.removeClothInBag(brownShoe));
        assertTrue(bagToTest.removeClothInBag(greySocks));

        assertEquals(bagToTest.getClothesList().size(), 0);
    }

    @Test
    public void isInBag() {
        assertTrue(bagToTest.addClothToBag(redPant, false));
        assertTrue(bagToTest.addClothToBag(brownShoe, false));
        assertTrue(bagToTest.addClothToBag(greySocks, false));

        assertTrue(bagToTest.isInBag(redPant));
        assertFalse(bagToTest.isInBag(blueShirt));
        assertTrue(bagToTest.isInBag(brownShoe));
        assertTrue(bagToTest.isInBag(greySocks));
    }

    @Test
    public void getWeekNumber() {
        assertEquals(bagToTest.getWeekNumber(), WEEK_NUMBER_1);
    }

    @Test
    public void isChecked() {
        assertTrue(bagToTestChecked.addClothToBag(redPant, false));
        assertTrue(bagToTestChecked.addClothToBag(blueShirt, false));
        assertTrue(bagToTestChecked.addClothToBag(brownShoe, false));
        assertTrue(bagToTestChecked.addClothToBag(greySocks, false));

        assertTrue(bagToTestChecked.isChecked());
        bagToTestChecked.setChecked(false);
        assertFalse(bagToTestChecked.isChecked());
    }

    @Test
    public void testEquals() {
        Bag bagEquals = new Bag(WEEK_NUMBER_1);
        Bag bagNotEquals = new Bag(WEEK_NUMBER_52);

        assertEquals(bagToTest, bagEquals);
        assertNotEquals(bagToTest, bagNotEquals);
    }
}