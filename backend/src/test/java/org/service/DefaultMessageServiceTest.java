package org.service;

import org.controller.request.MessageRequests;
import org.controller.response.MessageResponses;
import org.exception.ActionNotPermittedException;
import org.exception.InvalidRequestBodyException;
import org.exception.MissingEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.repository.InMemoryMessageRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class DefaultMessageServiceTest {
    private static final String TEST_USER = "testUser";
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(new InMemoryMessageRepository());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(TEST_USER, null, Collections.emptyList()));
    }

    @Test
    void createMessageTopLevel() {
        MessageResponses.Message message1 = messageService.createMessage(new MessageRequests.Create().setContent("content1"));
        List<MessageResponses.Message> childMessages = messageService.getAllTopLevelMessages();
        Assertions.assertEquals(1, childMessages.size());
        MessageResponses.Message actual = childMessages.get(0);
        Assertions.assertEquals(message1.getId(), actual.getId());
        Assertions.assertEquals("content1", actual.getContent());
        Assertions.assertEquals(TEST_USER, actual.getAuthor());
        Assertions.assertNotNull(actual.getCreatedAt());
        Assertions.assertNotNull(actual.getLastModifiedAt());
    }

    @Test
    void createMessageUnderParent() {
        MessageResponses.Message parentMessage = messageService.createMessage(new MessageRequests.Create().setContent("content1"));
        MessageResponses.Message childMessage = messageService.createMessage(new MessageRequests.Create().setContent("contentChild").setParentId(parentMessage.getId()));
        List<MessageResponses.Message> childMessages = messageService.getAllChildMessages(parentMessage.getId());
        Assertions.assertEquals(1, childMessages.size());
        MessageResponses.Message actual = childMessages.get(0);
        Assertions.assertEquals(childMessage.getId(), actual.getId());
        Assertions.assertEquals("contentChild", actual.getContent());
        Assertions.assertEquals(TEST_USER, actual.getAuthor());
        Assertions.assertNotNull(actual.getCreatedAt());
        Assertions.assertNotNull(actual.getLastModifiedAt());
    }

    @Test
    void createMessageNotLoggedIn() {
        SecurityContextHolder.clearContext();
        Assertions.assertThrows(BadCredentialsException.class, () -> messageService.createMessage(new MessageRequests.Create().setContent("content")));
    }

    @Test
    void createMessageBlankContent() {
        Assertions.assertThrows(InvalidRequestBodyException.class, () -> messageService.createMessage(new MessageRequests.Create().setContent("")));
    }

    @Test
    void modifyMessageContent() {
        MessageResponses.Message message = messageService.createMessage(new MessageRequests.Create().setContent("content"));

        List<MessageResponses.Message> beforeMessages = messageService.getAllTopLevelMessages();
        Assertions.assertEquals(1, beforeMessages.size());
        Instant beforeModificationTimestamp = beforeMessages.get(0).getLastModifiedAt();

        messageService.modifyMessage(message.getId(), new MessageRequests.Patch().setContent("new content"));

        List<MessageResponses.Message> afterMessages = messageService.getAllTopLevelMessages();
        Assertions.assertEquals(1, afterMessages.size());
        MessageResponses.Message afterMessage = afterMessages.get(0);

        Assertions.assertEquals("new content", afterMessage.getContent());
        Assertions.assertFalse(afterMessage.getLastModifiedAt().isBefore(beforeModificationTimestamp));
    }

    @Test
    void modifyMessageContentBlank() {
        MessageResponses.Message message = messageService.createMessage(new MessageRequests.Create().setContent("content"));
        Assertions.assertThrows(InvalidRequestBodyException.class, () -> messageService.modifyMessage(message.getId(), new MessageRequests.Patch().setContent("")));
    }

    @Test
    void modifyMessageWrongId() {
        Assertions.assertThrows(MissingEntityException.class, () -> messageService.modifyMessage(UUID.randomUUID(), new MessageRequests.Patch().setContent("asdf")));
    }

    @Test
    void modifyMessageDifferentUser() {
        MessageResponses.Message message = messageService.createMessage(new MessageRequests.Create().setContent("content"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("not_" + TEST_USER, null, Collections.emptyList()));
        Assertions.assertThrows(ActionNotPermittedException.class, () -> messageService.modifyMessage(message.getId(), new MessageRequests.Patch().setContent("new content")));
    }

    @Test
    void deleteMessageNoReplies() {
        MessageResponses.Message message = messageService.createMessage(new MessageRequests.Create().setContent("content"));
        messageService.deleteMessage(message.getId());
        List<MessageResponses.Message> topLevelMessages = messageService.getAllTopLevelMessages();
        Assertions.assertTrue(topLevelMessages.isEmpty());
    }

    @Test
    void deleteMessageWithReplies() {
        MessageResponses.Message parentMessage = messageService.createMessage(new MessageRequests.Create().setContent("content"));
        MessageResponses.Message childMessage = messageService.createMessage(new MessageRequests.Create().setContent("contentChild").setParentId(parentMessage.getId()));
        messageService.deleteMessage(parentMessage.getId());

        List<MessageResponses.Message> replies = messageService.getAllChildMessages(parentMessage.getId());
        Assertions.assertTrue(replies.isEmpty());

        List<MessageResponses.Message> topLevelMessages = messageService.getAllTopLevelMessages();
        Assertions.assertTrue(topLevelMessages.isEmpty());
    }

    @Test
    void deleteMessageNonExistent() {
        MessageResponses.Message parentMessage = messageService.createMessage(new MessageRequests.Create().setContent("content"));
        MessageResponses.Message childMessage = messageService.createMessage(new MessageRequests.Create().setContent("contentChild").setParentId(parentMessage.getId()));
        messageService.deleteMessage(parentMessage.getId());
        Assertions.assertThrows(MissingEntityException.class, () -> messageService.deleteMessage(childMessage.getId()));
    }


    @Test
    void deleteMessageDifferentUser() {
        MessageResponses.Message parentMessage = messageService.createMessage(new MessageRequests.Create().setContent("content"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("not_" + TEST_USER, null, Collections.emptyList()));
        Assertions.assertThrows(ActionNotPermittedException.class, () -> messageService.deleteMessage(parentMessage.getId()));
    }

}