package repository;

import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Optional<String> repositoryPassword = userRepository.selectPassword("login");
        Assertions.assertTrue(repositoryPassword.isPresent());
        Assertions.assertEquals("password", repositoryPassword.get());
    }

    @Test
    void saveUserDuplicate() {
        User user = new User().setLogin("login").setPassword("password");
        userRepository.saveUser(user);
        Assertions.assertThrows(IllegalStateException.class, () -> userRepository.saveUser(user));
    }
}