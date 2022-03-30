package com.potato.instock.watchlist;

import com.potato.instock.item.Item;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<Item> currentWatchlist;

    public Watchlist() {
        currentWatchlist = new ArrayList<>();
    }

    public Watchlist(Long id, List<Item> currentWatchlist) {
        this.id = id;
        this.currentWatchlist = currentWatchlist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getCurrentWatchlist() {
        return currentWatchlist;
    }

    public void setCurrentWatchlist(List<Item> currentWatchlist) {
        this.currentWatchlist = currentWatchlist;
    }
}
