package com.example.psomp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PubDao {

    @Insert
    long insertPub(PubEntity pub);

    @Insert
    void insertItem(ItemEntity item);

    @Transaction
    default void insertPubWithItems(PubWithItems pubWithItems) {
        long pubId = insertPub(pubWithItems.getPub());
        for (ItemEntity item : pubWithItems.getItems()) {
            item.setPubId((int) pubId);
            insertItem(item);
        }
    }

    @Transaction
    @Query("SELECT * FROM pubs WHERE id = :pubId")
    PubWithItems getPubWithItems(int pubId);

    @Transaction
    @Query("SELECT * FROM pubs")
    List<PubWithItems> getAllPubsWithItems();

    @Query("DELETE FROM pubs WHERE id = :pubId")
    void deletePubById(int pubId);

    @Query("DELETE FROM items WHERE id = :itemId")
    void deleteItemById(int itemId);

    @Query("SELECT * FROM pubs")
    List<PubEntity> getAllPubs();
}