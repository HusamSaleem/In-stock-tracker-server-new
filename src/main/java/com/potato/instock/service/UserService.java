package com.potato.instock.service;

import com.potato.instock.model.User;
import com.potato.instock.model.Watchlist;
import com.potato.instock.repository.UserRepository;
import com.potato.instock.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;

    @Autowired
    public UserService(UserRepository userRepository, WatchlistRepository watchlistRepository) {
        this.userRepository = userRepository;
        this.watchlistRepository = watchlistRepository;
    }

    public ResponseEntity<String> updateRegistrationToken(final User userRequest) {
        final String uid = userRequest.getUid();
        final Optional<User> user = userRepository.findUserByUid(uid);
        if (user.isEmpty()) return ResponseEntity.internalServerError().body("could not find this user");
        user.get().setRegistrationToken(userRequest.getRegistrationToken());
        userRepository.save(user.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> createNewUser(final User user) {
        if (user.getRegistrationToken().isEmpty() || user.getUid().isEmpty()) {
            return ResponseEntity.internalServerError().body("missing registration token");
        }
        final Watchlist watchlist = Watchlist.builder()
                .uid(user.getUid())
                .items(Collections.emptyList())
                .build();
        try {
            user.setPushNotifications(false);
            userRepository.save(user);
            watchlistRepository.save(watchlist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("user already exists");
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> updateNotificationPreference(final User userRequest) {
        final Optional<User> user = userRepository.findUserByUid(userRequest.getUid());
        if (user.isEmpty()) return ResponseEntity.internalServerError().body("could not find the user");
        user.get().setPushNotifications(userRequest.isPushNotifications());
        userRepository.save(user.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Boolean> getUserNotificationPreference(final String uid) {
        Optional<User> user = userRepository.findUserByUid(uid);
        if (user.isEmpty()) return ResponseEntity.badRequest().body(false);
        return ResponseEntity.ok(user.get().isPushNotifications());
    }
}
