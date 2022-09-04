package com.potato.instock.service;

import com.potato.instock.exceptions.item.ItemInvalidIdException;
import com.potato.instock.exceptions.item.ItemNotFoundException;
import com.potato.instock.exceptions.user.UserExistsException;
import com.potato.instock.exceptions.watchlist.WatchlistEntryNotFoundException;
import com.potato.instock.exceptions.watchlist.WatchlistNotFoundException;
import com.potato.instock.model.Item;
import com.potato.instock.model.User;
import com.potato.instock.model.Watchlist;
import com.potato.instock.repository.UserRepository;
import com.potato.instock.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;

    @Autowired
    public WatchlistService(WatchlistRepository watchlistRepository, UserRepository userRepository, ItemService itemService) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
    }

    public ResponseEntity<List<Item>> getWatchlistForUser(String uid) {
        final Optional<User> optionalUser = userRepository.findUserByUid(uid);
        if (optionalUser.isEmpty()) throw new UserExistsException("user does not exist");

        Optional<Watchlist> optionalWatchlist = watchlistRepository.findWatchlistByUid(uid);
        if (optionalWatchlist.isEmpty()) throw new WatchlistNotFoundException("watch list not found");
        return ResponseEntity.ok(optionalWatchlist.get().getItems());
    }

    public ResponseEntity<Item> addItemToWatchlist(final String uid, Item item) {
        if (item.getItemId() == null || item.getWebsite() == null) throw new ItemInvalidIdException("item body is missing information");
        Optional<User> optionalUser = userRepository.findUserByUid(uid);
        if (optionalUser.isEmpty()) throw new UserExistsException("user does not exist");

        final Optional<Watchlist> optionalWatchlist = watchlistRepository.findWatchlistByUid(uid);
        if (optionalWatchlist.isEmpty()) throw new WatchlistNotFoundException("watch list not found");
        final Optional<Item> optionalItem = getItemById(optionalWatchlist.get(), item.getItemId());
        if (optionalItem.isPresent()) throw new WatchlistNotFoundException("item already in watchlist");

        item = itemService.getItemFromWebsite(item.getItemId(), item.getWebsite());
        if (item == null) throw new ItemNotFoundException("item not found");
        optionalWatchlist.get().getItems().add(item);
        watchlistRepository.save(optionalWatchlist.get());
        return ResponseEntity.ok(item);
    }

    public ResponseEntity<String> deleteItemFromWatchlist(final String uid, final Item item) {
        if (item.getItemId() == null) throw new ItemInvalidIdException("missing item id");
        final Optional<User> optionalUser = userRepository.findUserByUid(uid);
        if (optionalUser.isEmpty()) throw new UserExistsException("user does not exist");

        final Optional<Watchlist> optionalWatchlist = watchlistRepository.findWatchlistByUid(uid);
        if (optionalWatchlist.isEmpty()) throw new WatchlistNotFoundException("watchlist not found");
        final Optional<Item> optionalItem = getItemById(optionalWatchlist.get(), item.getItemId());
        if (optionalItem.isEmpty()) throw new WatchlistEntryNotFoundException("not able to find item id: " + item.getItemId() + " in the user's watchlist");

        optionalWatchlist.get().getItems().removeIf(i -> Objects.equals(i.getItemId(), item.getItemId()));
        watchlistRepository.save(optionalWatchlist.get());
        return ResponseEntity.ok().build();
    }

    private Optional<Item> getItemById(Watchlist watchlist, String itemId) {
        return watchlist.getItems()
                .stream()
                .filter((item) -> Objects.equals(item.getItemId(), itemId))
                .findFirst();
    }

    public ResponseEntity<List<Item>> refreshWatchlist(final String uid) {
        final Optional<User> optionalUser = userRepository.findUserByUid(uid);
        if (optionalUser.isEmpty()) throw new UserExistsException("user does not exist");
        final Optional<Watchlist> optionalWatchlist = watchlistRepository.findWatchlistByUid(uid);
        if (optionalWatchlist.isEmpty()) throw new WatchlistNotFoundException("watchlist not found");

        for (int i = 0; i < optionalWatchlist.get().getItems().size(); i++) {
            Item item = optionalWatchlist.get().getItems().get(i);
            item = itemService.getItemFromWebsite(item.getItemId(), item.getWebsite());
            optionalWatchlist.get().getItems().set(i, item);
        }
        watchlistRepository.save(optionalWatchlist.get());
        return ResponseEntity.ok(optionalWatchlist.get().getItems());
    }
}