package com.potato.instock.controller;

import com.potato.instock.model.Item;
import com.potato.instock.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/watchlist")
@CrossOrigin
public class WatchlistController {
    private final WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping(path = "{uid}")
    public ResponseEntity<List<Item>> getWatchlistForUser(final @PathVariable("uid") String uid) {
        return watchlistService.getWatchlistForUser(uid);
    }

    @PostMapping(path = "{uid}")
    public ResponseEntity<Item> addItemToWatchlist(
            final @PathVariable String uid,
            final @RequestBody Item item) {
        return watchlistService.addItemToWatchlist(uid, item);
    }

    @DeleteMapping(path = "{uid}")
    public ResponseEntity<String> deleteItemFromWatchlist(
            final @PathVariable String uid,
            final @RequestBody Item item) {
        return watchlistService.deleteItemFromWatchlist(uid, item);
    }

    @GetMapping(path = "refresh/{uid}")
    public ResponseEntity<List<Item>> refreshWatchlist(final @PathVariable("uid") String uid) {
        return watchlistService.refreshWatchlist(uid);
    }
}
