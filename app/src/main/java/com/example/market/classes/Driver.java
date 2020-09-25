package com.example.market.classes;

import java.util.ArrayList;
import java.util.List;

public class Driver extends User {
   private List<Inventory> inventories=new ArrayList<Inventory>();

    public List<Inventory> getInventories() {
        return inventories;
    }

    public Driver () {
    }

    public Driver(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}
