package com.potato.instock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Item implements Serializable {
    private String itemId;
    private String price;
    private String name;
    private String website;
    private boolean inStock;
    @Expose(serialize = false, deserialize = false)
    @JsonIgnore
    private Long lastUpdated; // For caching purposes

    public Item() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
