package repository;

import model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

class InMemoryMessageRepositoryTest {
    private final static UUID ID1 = UUID.randomUUID();
    private final static UUID ID2 = UUID.randomUUID();
    private InMemoryMessageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMessageRepository();
    }

    @Test
    void save() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.save(message);
        Message savedMessage = repository.selectOne(ID1).get();
        Assertions.assertEquals(ID1, savedMessage.getId());
        Assertions.assertEquals("content", savedMessage.getContent());
    }

    @Test
    void saveDuplicate() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.save(message);
        Assertions.assertThrows(IllegalArgumentException.class, () -> repository.save(message));
    }

    @Test
    void update() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.save(message);
        repository.update(ID1, "new content");
        Message savedMessage = repository.selectOne(ID1).get();
        Assertions.assertEquals(ID1, savedMessage.getId());
        Assertions.assertEquals("new content", savedMessage.getContent());
    }

    @Test
    void updateNonExistent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> repository.update(ID2, "new content"));
    }

    @Test
    void delete() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.save(message);
        repository.delete(message.getId());
        Assertions.assertFalse(repository.selectOne(ID1).isPresent());
    }

    @Test
    void deleteNonExistent() {
        Assertions.assertThrows(NoSuchElementException.class, () -> repository.delete(ID2));
    }

    @Test
    void selectTopLevel() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.save(message);
        Message message2 = new Message().setId(ID2).setContent("content2");
        repository.save(message2);
        List<Message> messages = repository.selectTopLevel();
        Assertions.assertEquals(2, messages.size());
        Assertions.assertEquals(ID1, messages.get(0).getId());
        Assertions.assertEquals(ID2, messages.get(1).getId());
    }

    @Test
    void selectChildren() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.save(message);
        Message message2 = new Message().setId(ID2).setContent("content2").setParentId(message.getId());
        repository.save(message2);
        List<Message> messages = repository.selectChildren(message.getId());
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(ID2, messages.get(0).getId());
    }
}