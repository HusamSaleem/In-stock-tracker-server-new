package com.potato.instock.watchlist;

import com.potato.instock.authentication.User;
import com.potato.instock.authentication.UserRepository;
import com.potato.instock.exceptions.item.ItemInvalidIdException;
import com.potato.instock.exceptions.item.ItemNotFoundException;
import com.potato.instock.exceptions.user.UserExistsException;
import com.potato.instock.exceptions.watchlist.WatchlistEntryNotFoundException;
import com.potato.instock.exceptions.watchlist.WatchlistNotFoundException;
import com.potato.instock.item.Item;
import com.potato.instock.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private Optional<Item> getItemById(Watchlist watchlist, String itemId) {
        return watchlist.getCurrentWatchlist()
                .stream()
                .filter((item) -> Objects.equals(item.getItemId(), itemId))
                .findFirst();
    }

    public List<Item> getWatchlistForUser(String uniqueIdentifier) {
        Optional<User> optionalUser = userRepository.findUserByUniqueIdentifier(uniqueIdentifier);

        if (optionalUser.isEmpty()) {
            throw new UserExistsException("user does not exist");
        }

        long id = optionalUser.get().getId();
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(id);
        if (optionalWatchlist.isEmpty()) {
            throw new WatchlistNotFoundException("watch list not found");
        }
        return optionalWatchlist.get().getCurrentWatchlist();
    }

    @Transactional
    public Item addItemToWatchlist(String uniqueIdentifier, Item item) {
        if (item.getItemId() == null) {
            throw new ItemInvalidIdException("missing item id");
        }

        if (item.getWebsite() == null) {
            throw new ItemInvalidIdException("missing website information");
        }

        Optional<User> optionalUser = userRepository.findUserByUniqueIdentifier(uniqueIdentifier);

        if (optionalUser.isEmpty()) {
            throw new UserExistsException("user does not exist");
        }

        long id = optionalUser.get().getId();
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(id);
        if (optionalWatchlist.isEmpty()) {
            throw new WatchlistNotFoundException("watch list not found");
        }

        // Check if item exists
        Optional<Item> optionalItem = getItemById(optionalWatchlist.get(), item.getItemId());
        if (optionalItem.isPresent()) {
            throw new WatchlistNotFoundException("item already in watchlist");
        }

        item = itemService.getItemFromWebsite(item.getItemId(), item.getWebsite());
        if (item == null) {
            throw new ItemNotFoundException("item not found");
        }
        optionalWatchlist.get().getCurrentWatchlist().add(item);
        return item;
    }

    @Transactional
    public void deleteItemFromWatchlist(String uniqueIdentifier, Item item) {
        if (item.getItemId() == null) {
            throw new ItemInvalidIdException("missing item id");
        }

        Optional<User> optionalUser = userRepository.findUserByUniqueIdentifier(uniqueIdentifier);

        if (optionalUser.isEmpty()) {
            throw new UserExistsException("user does not exist");
        }

        long id = optionalUser.get().getId();
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(id);
        if (optionalWatchlist.isEmpty()) {
            throw new WatchlistNotFoundException("watch list not found");
        }

        Optional<Item> optionalItem = getItemById(optionalWatchlist.get(), item.getItemId());
        if (optionalItem.isEmpty()) {
            throw new WatchlistEntryNotFoundException("not able to find item id: " + item.getItemId() + " in the user's watchlist");
        }
        optionalWatchlist.get().getCurrentWatchlist().remove(optionalItem.get());
    }
}