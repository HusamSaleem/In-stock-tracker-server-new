package com.potato.instock.watchlist;

import com.potato.instock.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path = "{uniqueIdentifier}")
    public List<Item> getWatchlistForUser(@PathVariable("uniqueIdentifier") String uniqueIdentifier) {
        return watchlistService.getWatchlistForUser(uniqueIdentifier);
    }

    @PostMapping(path = "{uniqueIdentifier}")
    public Item addItemToWatchlist(
            @PathVariable String uniqueIdentifier,
            @RequestBody Item item) {
        return watchlistService.addItemToWatchlist(uniqueIdentifier, item);
    }

    @DeleteMapping(path = "{uniqueIdentifier}")
    public void deleteItemFromWatchlist(
            @PathVariable String uniqueIdentifier,
            @RequestBody Item item) {
        watchlistService.deleteItemFromWatchlist(uniqueIdentifier, item);
    }
}
