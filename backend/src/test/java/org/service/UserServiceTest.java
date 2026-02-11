package org.service;

import org.controller.request.UserRequests;
import org.controller.response.UserResponses;
import org.exception.InvalidRequestBodyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.repository.InMemoryUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private PasswordEncoder encoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(encoder.encode(Mockito.anyString())).thenReturn("encoded");
        userService = new UserService(encoder, new InMemoryUserRepository());
    }

    private static List<String> longCredentialProvider() {
        String longCredential = String.join("", Collections.nCopies(64, "1"));
        return Collections.singletonList(longCredential);
    }

    @Test
    void registerUser() {
        Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        UserRequests.Register user = new UserRequests.Register().setLogin("login").setPassword("password");
        userService.registerUser(user);
        UserRequests.Login loginDto = new UserRequests.Login().setLogin(user.getLogin()).setPassword(user.getPassword());
        UserResponses.Login loginResponse = userService.loginUser(loginDto);
        Assertions.assertNotNull(loginResponse);
        Assertions.assertNotNull(loginResponse.getToken());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("longCredentialProvider")
    void registerUserInvalidPassword(String password) {
        UserRequests.Register user = new UserRequests.Register().setLogin("login").setPassword(password);
        Assertions.assertThrows(InvalidRequestBodyException.class, () -> userService.registerUser(user));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("longCredentialProvider")
    void registerUserInvalidLogin(String login) {
        UserRequests.Register user = new UserRequests.Register().setLogin(login).setPassword("password");
        Assertions.assertThrows(InvalidRequestBodyException.class, () -> userService.registerUser(user));
    }

    @Test
    void loginUserWrongLogin() {
        UserRequests.Register user = new UserRequests.Register().setLogin("login").setPassword("password");
        userService.registerUser(user);
        UserRequests.Login loginDto = new UserRequests.Login().setLogin("different login").setPassword(user.getPassword());
        Assertions.assertThrows(BadCredentialsException.class, () -> userService.loginUser(loginDto));
    }

    @Test
    void loginUserWrongPassword() {
        Mockito.when(encoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        UserRequests.Register user = new UserRequests.Register().setLogin("login").setPassword("password");
        userService.registerUser(user);
        UserRequests.Login loginDto = new UserRequests.Login().setLogin(user.getLogin()).setPassword(user.getPassword());
        Assertions.assertThrows(BadCredentialsException.class, () -> userService.loginUser(loginDto));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("longCredentialProvider")
    void loginUserInvalidLogin(String login) {
        UserRequests.Register user = new UserRequests.Register().setLogin("login").setPassword("password");
        userService.registerUser(user);
        UserRequests.Login loginDto = new UserRequests.Login().setLogin(login).setPassword(user.getPassword());
        Assertions.assertThrows(InvalidRequestBodyException.class, () -> userService.loginUser(loginDto));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("longCredentialProvider")
    void loginUserInvalidPassword(String password) {
        UserRequests.Register user = new UserRequests.Register().setLogin("login").setPassword("password");
        userService.registerUser(user);
        UserRequests.Login loginDto = new UserRequests.Login().setLogin(user.getLogin()).setPassword(password);
        Assertions.assertThrows(InvalidRequestBodyException.class, () -> userService.loginUser(loginDto));
    }

}