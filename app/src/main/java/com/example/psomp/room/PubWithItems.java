package com.example.psomp.room;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

public class PubWithItems {
    @Embedded
    private PubEntity pub;

    @Relation(
            parentColumn = "id",
            entityColumn = "pubId"
    )
    private List<ItemEntity> items;

    public PubEntity getPub() {
        return pub;
    }

    public void setPub(PubEntity pub) {
        this.pub = pub;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}