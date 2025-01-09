package com.example.psomp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PubWithItems {
    @SerializedName("pub")
    private PubEntity pub;

    @SerializedName("items")
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