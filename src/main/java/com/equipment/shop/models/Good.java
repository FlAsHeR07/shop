package com.equipment.shop.models;

public class Good {
    private int good_id;
    private String name;
    private long price_kopeck;
    private String description;
    private int category_id;

    public Good(int good_id, String name, long price_kopeck, String description, int category_id) {
        this.good_id = good_id;
        this.name = name;
        this.price_kopeck = price_kopeck;
        this.description = description;
        this.category_id = category_id;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice_kopeck() {
        return price_kopeck;
    }

    public double getPrice_grn() {
        return (double) (price_kopeck) / 100;
    }

    public void setPrice_kopeck(long price_kopeck) {
        this.price_kopeck = price_kopeck;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
