package com.potato.instock.notification;

import com.potato.instock.authentication.User;
import com.potato.instock.authentication.UserRepository;
import com.potato.instock.item.Item;
import com.potato.instock.item.ItemService;
import com.potato.instock.watchlist.Watchlist;
import com.potato.instock.watchlist.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NotificationService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final EmailService emailService;
    private final int SCHEDULE_RATE = 300 * 1000; // 5 minutes

    @Autowired
    public NotificationService(WatchlistRepository watchlistRepository, UserRepository userRepository, ItemService itemService, EmailService emailService) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = SCHEDULE_RATE, initialDelay = 10000)
    public void scheduleTaskWithInitialDelay() {
        // Get all watchlisted items from the users
        List<Watchlist> watchlists = watchlistRepository.findAll();

        if (watchlists.size() == 0)
            return;

        // Update all the items in the watchlists
        watchlists.forEach(this::updateWatchlist);

        // Get all the users' that want to be notified by email
        List<User> users = userRepository.findAll();
        users = users.stream()
                .filter(User::isNotifyByEmail)
                .collect(Collectors.toList());

        // Start notifying users
        users.forEach(user -> handleNotification(user,
                watchlists.stream()
                        .filter(list -> Objects.equals(list.getId(), user.getId()))
                        .findFirst())
        );
    }

    private void updateWatchlist(Watchlist list) {
        list.getCurrentWatchlist().forEach(item -> itemService.getItemFromWebsite(item.getItemId(), item.getWebsite()));
    }

    private void handleNotification(User user, Optional<Watchlist> optionalWatchlist) {
        if (optionalWatchlist.isEmpty()) {
            return;
        }

        if (optionalWatchlist.get().getCurrentWatchlist().size() == 0) {
            return;
        }

        // Get items that are in stock only
        optionalWatchlist.get().setCurrentWatchlist(optionalWatchlist.get().getCurrentWatchlist()
                .stream()
                .filter(Item::isInStock)
                .collect(Collectors.toList())
        );

        try {
            emailService.sendMailNotification(user.getEmail(), optionalWatchlist.get().getCurrentWatchlist());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
