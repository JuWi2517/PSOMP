package com.example.psomp.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PubEntity.class, ItemEntity.class}, version = 1, exportSchema = false)
public abstract class PubDatabase extends RoomDatabase {
    private static PubDatabase instance;

    public abstract PubDao pubDao();

    public static synchronized PubDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PubDatabase.class, "pub_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}