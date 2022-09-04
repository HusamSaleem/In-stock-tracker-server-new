package com.potato.instock.controller;

import com.potato.instock.model.User;
import com.potato.instock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
@CrossOrigin
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createNewUser(final @RequestBody User user) {
        return userService.createNewUser(user);
    }

    @PatchMapping("token")
    public ResponseEntity<String> updateRegistrationToken(final @RequestBody User user) {
        return userService.updateRegistrationToken(user);
    }

    @PatchMapping("notification")
    public ResponseEntity<String>  updateNotificationPreference(final @RequestBody User user) {
        return userService.updateNotificationPreference(user);
    }

    @GetMapping(path = "notification/{uid}")
    public ResponseEntity<Boolean> getUserNotificationPreference(final @PathVariable("uid") String uid) {
        return userService.getUserNotificationPreference(uid);
    }
}
