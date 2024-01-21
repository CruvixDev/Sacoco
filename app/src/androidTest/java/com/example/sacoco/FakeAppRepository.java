package com.example.sacoco;

import com.example.sacoco.data.AppRepository;
import com.example.sacoco.data.DatabaseManager;

public class FakeAppRepository extends AppRepository {

    public FakeAppRepository(DatabaseManager databaseManagerInstance) {
        super(databaseManagerInstance);
    }
}
