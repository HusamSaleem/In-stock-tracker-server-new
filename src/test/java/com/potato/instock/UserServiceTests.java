package com.potato.instock;

import com.potato.instock.authentication.User;
import com.potato.instock.authentication.UserRepository;
import com.potato.instock.authentication.UserService;
import com.potato.instock.exceptions.user.UserInfoException;
import com.potato.instock.watchlist.WatchlistRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    WatchlistRepository watchlistRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        watchlistRepository.deleteAll();
    }

    @Test(expected = UserInfoException.class)
    public void tryRegisterNewUserWithNullEmail() {
        User user = new User();
        userService.registerNewUser(user);
    }

    @Test(expected = UserInfoException.class)
    public void tryRegisterNewUserWithNullPassword() {
        User user = new User();
        user.setEmail("potato@gmail.com");

        userService.registerNewUser(user);
    }

    @Test(expected = UserInfoException.class)
    public void tryRegisterNewUserWithBadEmail() {
        User user = new User();
        user.setEmail("gmail.com");
        user.setPassword("123456789");

        userService.registerNewUser(user);
    }

    @Test(expected = UserInfoException.class)
    public void tryRegisterNewUserWithBadPassword() {
        User user = new User();
        user.setEmail("potato@gmail.com");
        user.setPassword("1234");

        userService.registerNewUser(user);
    }
}
