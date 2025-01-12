package com.example.psomp;

import com.google.gson.annotations.SerializedName;

public class PubEntity {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    private int quantity;

    // Add other fields as needed

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}