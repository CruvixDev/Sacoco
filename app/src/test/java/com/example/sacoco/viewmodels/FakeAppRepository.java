package com.example.sacoco.viewmodels;

import com.example.sacoco.data.AppRepository;
import com.example.sacoco.models.Bag;
import com.example.sacoco.models.Cloth;
import com.example.sacoco.models.ClothTypeEnum;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class FakeAppRepository extends AppRepository {
    public FakeAppRepository() {
        super(null);
    }

    @Override
    public boolean saveBag(Bag bagToSave) {
        return true;
    }

    @Override
    public boolean removeBag(Bag bagToRemove) {
        return true;
    }

    @Override
    public boolean saveCloth(Cloth clothToSave) {
        return true;
    }

    @Override
    public boolean removeCloth(Cloth clothToRemove) {
        return true;
    }

    @Override
    public ArrayList<Bag> getAllBags() {
        ArrayList<Bag> bagsList = new ArrayList<>();

        bagsList.add(new Bag(1, true));
        bagsList.add(new Bag(2, false));
        bagsList.add(new Bag(3, false));
        bagsList.add(new Bag(4, true));
        bagsList.add(new Bag(5, false));
        bagsList.add(new Bag(6, true));

        return bagsList;
    }

    @Override
    public ArrayList<Cloth> getAllClothes() {
        ArrayList<Cloth> clothesList = new ArrayList<>();

        clothesList.add(new Cloth(UUID.randomUUID(), "Red shirt", ClothTypeEnum.SHIRT,
                URI.create("/")));
        clothesList.add(new Cloth(UUID.randomUUID(), "Blue pants", ClothTypeEnum.PANT,
                URI.create("/")));
        clothesList.add(new Cloth(UUID.randomUUID(), "Grey socks", ClothTypeEnum.SOCK,
                URI.create("/")));
        clothesList.add(new Cloth(UUID.randomUUID(), "Brown shoes", ClothTypeEnum.SHOE,
                URI.create("/")));

        return clothesList;
    }
}
