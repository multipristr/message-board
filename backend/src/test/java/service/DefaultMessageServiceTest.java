package service;

import model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import repository.InMemoryMessageRepository;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

class DefaultMessageServiceTest {
    private static final String TEST_USER = "testUser";
    private DefaultMessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new DefaultMessageService(new InMemoryMessageRepository());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(TEST_USER, null, Collections.emptyList()));
    }

    @Test
    void createMessageTopLevel() {
        UUID id1 = messageService.createMessage("content1", null);
        List<Message> childMessages = messageService.getAllChildMessages(null);
        Assertions.assertEquals(1, childMessages.size());
        Message actual = childMessages.get(0);
        Assertions.assertEquals(id1, actual.getId());
        Assertions.assertNull(actual.getParentId());
        Assertions.assertEquals("content1", actual.getContent());
        Assertions.assertEquals(TEST_USER, actual.getAuthor());
        Assertions.assertNotNull(actual.getCreatedAt());
        Assertions.assertNotNull(actual.getLastModifiedAt());
    }

    @Test
    void createMessageUnderParent() {
        UUID parentId = messageService.createMessage("content1", null);
        UUID id1 = messageService.createMessage("contentChild", parentId);
        List<Message> childMessages = messageService.getAllChildMessages(parentId);
        Assertions.assertEquals(1, childMessages.size());
        Message actual = childMessages.get(0);
        Assertions.assertEquals(id1, actual.getId());
        Assertions.assertEquals(parentId, actual.getParentId());
        Assertions.assertEquals("contentChild", actual.getContent());
        Assertions.assertEquals(TEST_USER, actual.getAuthor());
        Assertions.assertNotNull(actual.getCreatedAt());
        Assertions.assertNotNull(actual.getLastModifiedAt());
    }

    @Test
    void createMessageNotLoggedIn() {
        SecurityContextHolder.clearContext();
        Assertions.assertThrows(BadCredentialsException.class, () -> messageService.createMessage("content", UUID.randomUUID()));
    }

    @Test
    void createMessageBlankContent() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.createMessage("", UUID.randomUUID()));
    }

    @Test
    void modifyMessageContent() {
        UUID messageId = messageService.createMessage("content", null);

        List<Message> beforeMessages = messageService.getAllChildMessages(null);
        Assertions.assertEquals(1, beforeMessages.size());
        ZonedDateTime beforeModificationTimestamp = beforeMessages.get(0).getLastModifiedAt();

        messageService.modifyMessageContent(messageId, "new content");

        List<Message> afterMessages = messageService.getAllChildMessages(null);
        Assertions.assertEquals(1, afterMessages.size());
        Message afterMessage = afterMessages.get(0);

        Assertions.assertEquals("new content", afterMessage.getContent());
        Assertions.assertFalse(afterMessage.getLastModifiedAt().isBefore(beforeModificationTimestamp));
    }

    @Test
    void modifyMessageContentBlank() {
        UUID messageId = messageService.createMessage("content", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> messageService.modifyMessageContent(messageId, ""));
    }

    @Test
    void modifyMessageWrongId() {
        Assertions.assertThrows(NoSuchElementException.class, () -> messageService.modifyMessageContent(UUID.randomUUID(), "asdf"));
    }

    @Test
    void modifyMessageDifferentUser() {
        UUID messageId = messageService.createMessage("content", null);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("not_" + TEST_USER, null, Collections.emptyList()));
        Assertions.assertThrows(SecurityException.class, () -> messageService.modifyMessageContent(messageId, "new content"));
    }

    @Test
    void deleteMessageNoReplies() {
        UUID messageId = messageService.createMessage("content", null);
        messageService.deleteMessage(messageId);
        List<Message> topLevelMessages = messageService.getAllChildMessages(null);
        Assertions.assertTrue(topLevelMessages.isEmpty());
    }

    @Test
    void deleteMessageWithReplies() {
        UUID parentId = messageService.createMessage("content", null);
        UUID childId = messageService.createMessage("contentChild", parentId);
        messageService.deleteMessage(parentId);

        List<Message> replies = messageService.getAllChildMessages(parentId);
        Assertions.assertTrue(replies.isEmpty());

        List<Message> topLevelMessages = messageService.getAllChildMessages(null);
        Assertions.assertTrue(topLevelMessages.isEmpty());
    }

    @Test
    void deleteMessageNonExistent() {
        UUID parentId = messageService.createMessage("content", null);
        UUID childId = messageService.createMessage("contentChild", parentId);
        messageService.deleteMessage(parentId);
        Assertions.assertThrows(NoSuchElementException.class, () -> messageService.deleteMessage(childId));
    }


    @Test
    void deleteMessageDifferentUser() {
        UUID parentId = messageService.createMessage("content", null);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("not_" + TEST_USER, null, Collections.emptyList()));
        Assertions.assertThrows(SecurityException.class, () -> messageService.deleteMessage(parentId));
    }
}