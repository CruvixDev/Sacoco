package com.example.sacoco.viewmodels;

import static org.junit.Assert.*;

import com.example.sacoco.models.Cloth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

public class BagClothViewModelTest {
    private final BagClothViewModel bagClothViewModel = new BagClothViewModel(new FakeAppRepository());

    @Test
    public void addBagWithInvalidWeekNumberAndNotExistingClothes() {
        int previousSize = bagClothViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> notExistingClothesUUID = new ArrayList<>();
        notExistingClothesUUID.add(UUID.randomUUID());
        notExistingClothesUUID.add(UUID.randomUUID());

        bagClothViewModel.addBag(-1, notExistingClothesUUID);
        bagClothViewModel.addBag(53, notExistingClothesUUID);
        bagClothViewModel.addBag(0, notExistingClothesUUID);

        assertEquals(bagClothViewModel.getBagsLiveData().getValue().size(), previousSize);
    }
    
    @Test
    public void addBagWithInvalidWeekNumberAndExistingClothes() {
        int previousSize = bagClothViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> existingClothesUUID = new ArrayList<>();
        for (Cloth cloth : bagClothViewModel.getClothesLiveData().getValue()) {
            existingClothesUUID.add(cloth.getClothUUID());
        }

        bagClothViewModel.addBag(-1, existingClothesUUID);
        bagClothViewModel.addBag(53, existingClothesUUID);
        bagClothViewModel.addBag(0, existingClothesUUID);

        assertEquals(bagClothViewModel.getBagsLiveData().getValue().size(), previousSize);
    }

    @Test
    public void addBagWithValidWeekNumberAndNotExistingClothes() {
        int previousSize = bagClothViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> notExistingClothesUUID = new ArrayList<>();
        notExistingClothesUUID.add(UUID.randomUUID());
        notExistingClothesUUID.add(UUID.randomUUID());

        bagClothViewModel.addBag(7, notExistingClothesUUID);
        bagClothViewModel.addBag(8, notExistingClothesUUID);
        bagClothViewModel.addBag(9, notExistingClothesUUID);

        assertEquals(bagClothViewModel.getBagsLiveData().getValue().size(), previousSize + 3);
    }

    @Test
    public void addBagWithValidWeekNumberAndExistingClothes() {
        int previousSize = bagClothViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> existingClothesUUID = new ArrayList<>();
        for (Cloth cloth : bagClothViewModel.getClothesLiveData().getValue()) {
            existingClothesUUID.add(cloth.getClothUUID());
        }

        bagClothViewModel.addBag(10, existingClothesUUID);
        bagClothViewModel.addBag(11, existingClothesUUID);
        bagClothViewModel.addBag(12, existingClothesUUID);

        assertEquals(bagClothViewModel.getBagsLiveData().getValue().size(), previousSize + 3);
    }

    @Test
    public void addBagExistingWithNotExistingClothes() {
        int previousSize = bagClothViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> notExistingClothesUUID = new ArrayList<>();
        notExistingClothesUUID.add(UUID.randomUUID());
        notExistingClothesUUID.add(UUID.randomUUID());

        bagClothViewModel.addBag(7, notExistingClothesUUID);
        bagClothViewModel.addBag(8, notExistingClothesUUID);
        bagClothViewModel.addBag(9, notExistingClothesUUID);

        assertEquals(bagClothViewModel.getBagsLiveData().getValue().size(), previousSize);
    }

    @Test
    public void addBagExistingWithExistingClothes() {
        int previousSize = bagClothViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> existingClothesUUID = new ArrayList<>();
        for (Cloth cloth : bagClothViewModel.getClothesLiveData().getValue()) {
            existingClothesUUID.add(cloth.getClothUUID());
        }

        bagClothViewModel.addBag(10, existingClothesUUID);
        bagClothViewModel.addBag(11, existingClothesUUID);
        bagClothViewModel.addBag(12, existingClothesUUID);

        assertEquals(bagClothViewModel.getBagsLiveData().getValue().size(), previousSize);
    }

    @Test
    public void removeBag() {
        assertTrue(true);
    }

    @Test
    public void addCloth() {
        assertTrue(true);
    }

    @Test
    public void removeCloth() {
        assertTrue(true);
    }

    @Test
    public void addClothesToBag() {
        assertTrue(true);
    }

    @Test
    public void removeClothesInBag() {
        assertTrue(true);
    }

    @Test
    public void setSelectedBagLiveData() {
        assertTrue(true);
    }

    @Test
    public void setSelectedClothLiveData() {
        assertTrue(true);
    }

    @Test
    public void getSelectedBagLiveData() {
        assertTrue(true);
    }

    @Test
    public void getSelectedClothLiveData() {
        assertTrue(true);
    }

    @Test
    public void getClothesLiveData() {
        assertTrue(true);
    }

    @Test
    public void getBagsLiveData() {
        assertTrue(true);
    }
}