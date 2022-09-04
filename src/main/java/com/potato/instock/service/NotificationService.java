package com.potato.instock.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.potato.instock.model.User;
import com.potato.instock.model.Watchlist;
import com.potato.instock.repository.UserRepository;
import com.potato.instock.repository.WatchlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NotificationService {
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final WatchlistService watchlistService;
    private final int SCHEDULE_RATE = 600 * 1000; // 10 minutes

    @Autowired
    public NotificationService(WatchlistRepository watchlistRepository, UserRepository userRepository, WatchlistService watchlistService) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.watchlistService = watchlistService;
    }

    @Scheduled(fixedRate = SCHEDULE_RATE, initialDelay = 10000)
    public void scheduleTaskWithInitialDelay() {
        // Get all the users' that want to be notified by email
        List<User> users = userRepository.findAll();
        users = users.stream()
                .filter(User::isPushNotifications)
                .collect(Collectors.toList());

        // Update the watchlists for each user
        users.forEach(user -> watchlistService.refreshWatchlist(user.getUid()));
        List<Watchlist> watchlists = watchlistRepository.findAll();

        // Start notifying users
        users.forEach(user -> handleNotification(user,
                watchlists.stream()
                        .filter(list -> Objects.equals(list.getUid(), user.getUid()))
                        .findFirst())
        );
    }

    private void handleNotification(final User user, final Optional<Watchlist> watchlist) {
        if (watchlist.isEmpty() || watchlist.get().getItems().size() == 0) return;
        final String registrationToken = user.getRegistrationToken();
        final Message message = Message.builder()
                .setToken(registrationToken)
                .setNotification(Notification.builder()
                        .setTitle("Your item(s) are in stock!")
                        .setBody("One or more of your items are in stock again")
                        .build())
                .build();
        try {
            final String response = FirebaseMessaging.getInstance().send(message);
            log.info(response);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
