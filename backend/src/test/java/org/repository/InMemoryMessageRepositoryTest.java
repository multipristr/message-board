package org.repository;

import org.junit.jupiter.api.BeforeEach;

class InMemoryMessageRepositoryTest extends IMessageRepositoryTest {
    private InMemoryMessageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMessageRepository();
    }

    @Override
    IMessageRepository getRepository() {
        return repository;
    }
}