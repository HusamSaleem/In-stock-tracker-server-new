package com.potato.instock.authentication;

import com.potato.instock.exceptions.user.UserExistsException;
import com.potato.instock.exceptions.user.UserInfoException;
import com.potato.instock.watchlist.Watchlist;
import com.potato.instock.watchlist.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private boolean isEmailValid(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return email.matches(regex);
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8;
    }

    private String getEncryptedPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public void registerNewUser(User newUser) {
        if (newUser.getEmail() == null) {
            throw new UserInfoException("missing email address");
        }

        if (newUser.getPassword() == null) {
            throw new UserInfoException("missing password");
        }

        if (!isEmailValid(newUser.getEmail())) {
            throw new UserInfoException("email is not valid");
        }

        if (!isPasswordStrong(newUser.getPassword())) {
            throw new UserInfoException("password must be at least 8 characters or more");
        }

        // Check for existing users
        Optional<User> existingUserByEmail = userRepository.findUserByEmail(newUser.getEmail());

        if (existingUserByEmail.isPresent()) {
            throw new UserExistsException("email is taken");
        }
        newUser.setEmail(newUser.getEmail().toLowerCase());
        // Encrypt password
        newUser.setPassword(getEncryptedPassword(newUser.getPassword()));
        userRepository.save(newUser);
        watchlistRepository.save(new Watchlist()); // Create a relationship of user to a watchlist by id
    }

    public User login(User user) {
        Optional<User> existingUserByEmail = userRepository.findUserByEmail(user.getEmail());

        if (existingUserByEmail.isEmpty()) {
            throw new UserExistsException("failed to login");
        }

        // Check if passwords match
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(user.getPassword(), existingUserByEmail.get().getPassword())) {
            throw new UserExistsException("invalid password");
        }

        return existingUserByEmail.get();
    }

    @Transactional
    public void updateUserNotificationPreferences(String uniqueIdentifier, boolean notifyByEmail) {
        Optional<User> optionalUser = userRepository.findUserByUniqueIdentifier(uniqueIdentifier);

        if (optionalUser.isEmpty()) {
            throw new UserExistsException("user does not exist");
        }

        optionalUser.get().setNotifyByEmail(notifyByEmail);
    }

    @Transactional
    public void updateUserEmail(String uniqueIdentifier, String newEmail) {
        if (!isEmailValid(newEmail)) {
            throw new UserInfoException("email is not valid");
        }

        Optional<User> optionalUser = userRepository.findUserByUniqueIdentifier(uniqueIdentifier);
        if (optionalUser.isEmpty()) {
            throw new UserExistsException("user does not exist");
        }

        optionalUser.get().setEmail(newEmail);
    }
}