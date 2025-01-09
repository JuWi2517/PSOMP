package com.example.psomp;

public class Item {
    private String name;
    private double price;
    private int quantity;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        if (quantity > 0) quantity--;
    }

    // Add this method to calculate the total price for this item
    public double getTotalPrice() {
        return price * quantity;
    }
}
