package org.repository;

import org.exception.DuplicateIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.model.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

abstract class IMessageRepositoryTest {
    private final static UUID ID1 = UUID.randomUUID();
    private final static UUID ID2 = UUID.randomUUID();

    abstract IMessageRepository getRepository();

    @Test
    void save() {
        Message message = new Message().setId(ID1).setContent("content");
        IMessageRepository repository = getRepository();
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
        IMessageRepository repository = getRepository();
        repository.saveMessage(message);
        Assertions.assertThrows(DuplicateIdException.class, () -> repository.saveMessage(message));
    }

    @Test
    void update() {
        Message message = new Message().setId(ID1).setContent("content");
        IMessageRepository repository = getRepository();
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
        IMessageRepository repository = getRepository();
        repository.saveMessage(message);
        repository.deleteMessage(message.getId());
        Assertions.assertFalse(repository.selectOneMessage(ID1).isPresent());
    }

    @Test
    void selectTopLevel() {
        Instant time = Instant.now();
        Message message = new Message().setId(ID1).setContent("content").setCreatedAt(time);
        IMessageRepository repository = getRepository();
        repository.saveMessage(message);
        Message message2 = new Message().setId(ID2).setContent("content2").setCreatedAt(time.plusMillis(3));
        repository.saveMessage(message2);
        List<Message> messages = repository.selectTopLevelMessages();
        Assertions.assertEquals(2, messages.size());
        Assertions.assertEquals(ID1, messages.get(1).getId());
        Assertions.assertEquals(ID2, messages.get(0).getId());
    }

    @Test
    void selectChildren() {
        Message message = new Message().setId(ID1).setContent("content");
        IMessageRepository repository = getRepository();
        repository.saveMessage(message);
        Message message2 = new Message().setId(ID2).setContent("content2").setParentId(message.getId());
        repository.saveMessage(message2);
        List<Message> messages = repository.selectChildMessages(message.getId());
        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(ID2, messages.get(0).getId());
    }

}