package com.potato.instock.authentication;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(path = "/register")
    public void registerNewUser(@RequestBody User user) {
        userService.registerNewUser(user);
    }

    @PostMapping(path = "/login")
    public User login(@RequestBody User user) {
        return userService.login(user);
    }

    @PutMapping(path = "/notifications/{uniqueIdentifier}")
    public void updateUserNotificationPreferences(
            @PathVariable("uniqueIdentifier") String uniqueIdentifier,
            @RequestParam boolean notifyByEmail) {
        userService.updateUserNotificationPreferences(uniqueIdentifier, notifyByEmail);
    }

    @PutMapping(path = "/email/{uniqueIdentifier}")
    public void updateUserEmail(
            @PathVariable("uniqueIdentifier") String uniqueIdentifier,
            @RequestParam String newEmail) {
        userService.updateUserEmail(uniqueIdentifier, newEmail);
    }
}
