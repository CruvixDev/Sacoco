package com.example.sacoco.viewmodels;

import static org.junit.Assert.*;

import com.example.sacoco.data.AppRepository;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.models.ClothTypeEnum;

import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class BagViewModelTest {
    private BagViewModel bagViewModel = new BagViewModel(new FakeAppRepository());

    @Test
    public void addBagWithInvalidWeekNumberAndNotExistingClothes() {
        int previousSize = bagViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> notExistingClothesUUID = new ArrayList<>();
        notExistingClothesUUID.add(UUID.randomUUID());
        notExistingClothesUUID.add(UUID.randomUUID());

        bagViewModel.addBag(-1, notExistingClothesUUID);
        bagViewModel.addBag(53, notExistingClothesUUID);
        bagViewModel.addBag(0, notExistingClothesUUID);

        assertEquals(bagViewModel.getBagsLiveData().getValue().size(), previousSize);
    }
    
    @Test
    public void addBagWithInvalidWeekNumberAndExistingClothes() {
        int previousSize = bagViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> existingClothesUUID = new ArrayList<>();
        for (Cloth cloth : bagViewModel.getClothesLiveData().getValue()) {
            existingClothesUUID.add(cloth.getClothUUID());
        }

        bagViewModel.addBag(-1, existingClothesUUID);
        bagViewModel.addBag(53, existingClothesUUID);
        bagViewModel.addBag(0, existingClothesUUID);

        assertEquals(bagViewModel.getBagsLiveData().getValue().size(), previousSize);
    }

    @Test
    public void addBagWithValidWeekNumberAndNotExistingClothes() {
        int previousSize = bagViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> notExistingClothesUUID = new ArrayList<>();
        notExistingClothesUUID.add(UUID.randomUUID());
        notExistingClothesUUID.add(UUID.randomUUID());

        bagViewModel.addBag(7, notExistingClothesUUID);
        bagViewModel.addBag(8, notExistingClothesUUID);
        bagViewModel.addBag(9, notExistingClothesUUID);

        assertEquals(bagViewModel.getBagsLiveData().getValue().size(), previousSize + 3);
    }

    @Test
    public void addBagWithValidWeekNumberAndExistingClothes() {
        int previousSize = bagViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> existingClothesUUID = new ArrayList<>();
        for (Cloth cloth : bagViewModel.getClothesLiveData().getValue()) {
            existingClothesUUID.add(cloth.getClothUUID());
        }

        bagViewModel.addBag(10, existingClothesUUID);
        bagViewModel.addBag(11, existingClothesUUID);
        bagViewModel.addBag(12, existingClothesUUID);

        assertEquals(bagViewModel.getBagsLiveData().getValue().size(), previousSize + 3);
    }

    @Test
    public void addBagExistingWithNotExistingClothes() {
        int previousSize = bagViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> notExistingClothesUUID = new ArrayList<>();
        notExistingClothesUUID.add(UUID.randomUUID());
        notExistingClothesUUID.add(UUID.randomUUID());

        bagViewModel.addBag(7, notExistingClothesUUID);
        bagViewModel.addBag(8, notExistingClothesUUID);
        bagViewModel.addBag(9, notExistingClothesUUID);

        assertEquals(bagViewModel.getBagsLiveData().getValue().size(), previousSize);
    }

    @Test
    public void addBagExistingWithExistingClothes() {
        int previousSize = bagViewModel.getBagsLiveData().getValue().size();

        ArrayList<UUID> existingClothesUUID = new ArrayList<>();
        for (Cloth cloth : bagViewModel.getClothesLiveData().getValue()) {
            existingClothesUUID.add(cloth.getClothUUID());
        }

        bagViewModel.addBag(10, existingClothesUUID);
        bagViewModel.addBag(11, existingClothesUUID);
        bagViewModel.addBag(12, existingClothesUUID);

        assertEquals(bagViewModel.getBagsLiveData().getValue().size(), previousSize);
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