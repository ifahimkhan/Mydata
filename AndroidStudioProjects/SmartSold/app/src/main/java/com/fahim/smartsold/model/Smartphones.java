package com.fahim.smartsold.model;

/**
 * Created by root on 13/3/18.
 */

public class Smartphones {
    String name;
    String model;
    int price;

    public Smartphones() {
    }

    public Smartphones(String name, String model, int price) {
        this.name = name;
        this.model = model;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
