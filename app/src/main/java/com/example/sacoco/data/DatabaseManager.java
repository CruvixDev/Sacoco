package com.example.sacoco.data;

public class DatabaseManager {
    private static DatabaseManager databaseManagerInstance;

    private DatabaseManager() {

    }

    public static DatabaseManager getInstance() {
        if (databaseManagerInstance == null) {
            databaseManagerInstance = new DatabaseManager();
        }
        return databaseManagerInstance;
    }
}
