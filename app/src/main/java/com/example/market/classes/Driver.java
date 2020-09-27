package com.example.market.classes;

import java.util.ArrayList;
import java.util.List;

public class Driver extends User {
    private Inventory inventory;

    public Driver(String name, String email, String type, String phone, Inventory inventory) {
        super(name, email, type, phone);
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
