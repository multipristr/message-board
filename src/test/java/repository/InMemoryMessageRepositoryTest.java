package repository;

import model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

class InMemoryMessageRepositoryTest {
    private final UUID id1 = UUID.randomUUID();
    private final UUID id2 = UUID.randomUUID();
    private InMemoryMessageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMessageRepository();
    }

    @Test
    void save() {
        Message message = new Message().setId(id1).setContent("content");
        repository.save(message);
        Message savedMessage = repository.selectAll().get(0);
        Assertions.assertEquals(id1, savedMessage.getId());
        Assertions.assertEquals("content", savedMessage.getContent());
    }

    @Test
    void update() {
        Message message = new Message().setId(id1).setContent("content");
        repository.save(message);
        repository.update(id1, "new content");
        Message savedMessage = repository.selectAll().get(0);
        Assertions.assertEquals(id1, savedMessage.getId());
        Assertions.assertEquals("new content", savedMessage.getContent());
    }

    @Test
    void updateNonExistent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> repository.update(id2, "new content"));
    }

    @Test
    void delete() {
        Message message = new Message().setId(id1).setContent("content");
        repository.save(message);
        repository.delete(message.getId());
        Assertions.assertTrue(repository.selectAll().isEmpty());
    }

    @Test
    void deleteNonExistent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> repository.delete(id2));
    }

    @Test
    void selectAll() {
        Message message = new Message().setId(id1).setContent("content");
        repository.save(message);
        Message message2 = new Message().setId(id2).setContent("content2");
        repository.save(message2);
        List<Message> messages = repository.selectAll();
        Assertions.assertEquals(2, messages.size());
        Assertions.assertEquals(id1, messages.get(0).getId());
        Assertions.assertEquals(id2, messages.get(1).getId());
    }
}