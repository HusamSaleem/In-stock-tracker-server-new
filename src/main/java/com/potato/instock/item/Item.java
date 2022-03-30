package com.potato.instock.item;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Item implements Serializable {
    private String itemId;
    private String price;
    private String name;
    private String website;
    private boolean inStock;
    @JsonIgnore
    private Long lastUpdated; // For caching purposes

    public Item() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public Item(String itemId, String price, String name, String website, boolean inStock, Long timeStamp) {
        this.itemId = itemId;
        this.price = price;
        this.name = name;
        this.website = website;
        this.inStock = inStock;
        this.lastUpdated = timeStamp;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long timeStamp) {
        this.lastUpdated = timeStamp;
    }

    @Override
    public String toString() {
        return "{" +
                "\"itemId\":\"" + itemId + "\"" +
                ", \"price\":\"" + price + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"website\":\"" + website + "\"" +
                ", \"inStock:" + inStock +
                "}";
    }
}
