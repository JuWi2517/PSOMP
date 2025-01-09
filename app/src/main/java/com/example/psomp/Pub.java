package com.example.psomp;

import java.util.ArrayList;
import java.util.List;

public class Pub {
    private String name;
    private List<Item> items;

    public Pub(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public double getTotalBill() {
        double total = 0;
        for (Item item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
