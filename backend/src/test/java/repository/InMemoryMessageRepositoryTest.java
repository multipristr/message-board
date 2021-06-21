package repository;

import model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
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
        repository.saveMessage(message);
        Optional<Message> optionalMessage = repository.selectOneMessage(ID1);
        Assertions.assertTrue(optionalMessage.isPresent());
        Message savedMessage = optionalMessage.get();
        Assertions.assertEquals(ID1, savedMessage.getId());
        Assertions.assertEquals("content", savedMessage.getContent());
    }

    @Test
    void saveDuplicate() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.saveMessage(message);
        Assertions.assertThrows(IllegalStateException.class, () -> repository.saveMessage(message));
    }

    @Test
    void update() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.saveMessage(message);
        message.setContent("new content");
        repository.updateMessage(message);
        Optional<Message> optionalMessage = repository.selectOneMessage(ID1);
        Assertions.assertTrue(optionalMessage.isPresent());
        Message savedMessage = optionalMessage.get();
        Assertions.assertEquals(ID1, savedMessage.getId());
        Assertions.assertEquals("new content", savedMessage.getContent());
    }

    @Test
    void delete() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.saveMessage(message);
        repository.deleteMessage(message.getId());
        Assertions.assertFalse(repository.selectOneMessage(ID1).isPresent());
    }

    @Test
    void selectTopLevel() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.saveMessage(message);
        Message message2 = new Message().setId(ID2).setContent("content2");
        repository.saveMessage(message2);
        List<Message> messages = repository.selectTopLevelMessages();
        Assertions.assertEquals(2, messages.size());
        Assertions.assertEquals(ID1, messages.get(0).getId());
        Assertions.assertEquals(ID2, messages.get(1).getId());
    }

    @Test
    void selectChildren() {
        Message message = new Message().setId(ID1).setContent("content");
        repository.saveMessage(message);
        Message message2 = new Message().setId(ID2).setContent("content2").setParentId(message.getId());
        repository.saveMessage(message2);
        List<Message> messages = repository.selectChildMessages(message.getId());
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(ID2, messages.get(0).getId());
    }
}