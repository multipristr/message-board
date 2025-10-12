package org.repository;

import org.junit.jupiter.api.BeforeEach;

class InMemoryUserRepositoryTest extends IUserRepositoryTest {
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Override
    IUserRepository getRepository() {
        return userRepository;
    }
}