package org.repository;

import org.exception.DuplicateIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.User;

import java.util.Optional;

class InMemoryUserRepositoryTest {
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    void saveUser() {
        User user = new User().setLogin("login").setPassword("password");
        userRepository.saveUser(user);
        Optional<User> repositoryUser = userRepository.selectOneUser("login");
        Assertions.assertTrue(repositoryUser.isPresent());
        Assertions.assertEquals("password", repositoryUser.get().getPassword());
    }

    @Test
    void saveUserDuplicate() {
        User user = new User().setLogin("login").setPassword("password");
        userRepository.saveUser(user);
        Assertions.assertThrows(DuplicateIdException.class, () -> userRepository.saveUser(user));
    }

}