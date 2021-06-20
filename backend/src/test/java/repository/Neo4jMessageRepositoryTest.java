package repository;

import model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Neo4j;

import java.util.List;
import java.util.UUID;

class Neo4jMessageRepositoryTest {
    private Neo4jMessageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new Neo4jMessageRepository(new Neo4j());
    }

    @Test
    void save() {
        UUID id1 = UUID.randomUUID();
        Message message = new Message().setId(id1).setContent("content").setAuthor("author");
        repository.saveMessage(message);
        Message savedMessage = repository.selectOneMessage(id1).get();
        Assertions.assertEquals(id1, savedMessage.getId());
        Assertions.assertEquals("content", savedMessage.getContent());
    }

    @Test
    void update() {
        UUID id1 = UUID.randomUUID();
        Message message = new Message().setId(id1).setContent("content").setAuthor("author");
        repository.saveMessage(message);
        message.setContent("new content");
        repository.updateMessage(message);
        Message savedMessage = repository.selectOneMessage(id1).get();
        Assertions.assertEquals(id1, savedMessage.getId());
        Assertions.assertEquals("new content", savedMessage.getContent());
    }

    @Test
    void delete() {
        UUID id1 = UUID.randomUUID();
        Message message = new Message().setId(id1).setContent("content").setAuthor("author");
        repository.saveMessage(message);
        repository.deleteMessage(message.getId());
        Assertions.assertFalse(repository.selectOneMessage(id1).isPresent());
    }

    @Test
    void selectTopLevel() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Message message = new Message().setId(id1).setContent("content").setAuthor("author");
        repository.saveMessage(message);
        Message message2 = new Message().setId(id2).setContent("content2").setAuthor("author");
        repository.saveMessage(message2);
        List<Message> messages = repository.selectTopLevelMessages();
        Assertions.assertTrue(messages.size() >= 2);
    }

    @Test
    void selectChildren() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Message message = new Message().setId(id1).setContent("content").setAuthor("author");
        repository.saveMessage(message);
        Message message2 = new Message().setId(id2).setContent("content2").setParentId(message.getId()).setAuthor("author");
        repository.saveMessage(message2);
        List<Message> messages = repository.selectChildMessages(message.getId());
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(id2, messages.get(0).getId());
    }
}