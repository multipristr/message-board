package service;

import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.InMemoryUserRepository;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {
    @Mock
    private PasswordEncoder encoder;

    private DefaultUserService userService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(encoder.encode(Mockito.anyString())).thenReturn("encoded");
        userService = new DefaultUserService(encoder, new InMemoryUserRepository());
    }

    @Test
    void registerUser() {
        Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        User user = new User().setLogin("login").setPassword("password");
        userService.registerUser(user);
        String token = userService.loginUser(user);
        Assertions.assertNotNull(token);
    }

    @Test
    void registerUserBlankPassword() {
        User user = new User().setLogin("login").setPassword("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void loginUserWrongLogin() {
        User user = new User().setLogin("login").setPassword("password");
        userService.registerUser(user);
        user.setLogin("different login");
        Assertions.assertThrows(BadCredentialsException.class, () -> userService.loginUser(user));
    }

    @Test
    void loginUserWrongPassword() {
        Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        User user = new User().setLogin("login").setPassword("password");
        userService.registerUser(user);
        Assertions.assertThrows(BadCredentialsException.class, () -> userService.loginUser(user));
    }

    @Test
    void loginUserEmptyPassword() {
        User user = new User().setLogin("login").setPassword("password");
        userService.registerUser(user);
        user.setPassword("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.loginUser(user));
    }
}