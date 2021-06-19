package service;

import model.Message;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface IMessageService {
    UUID createMessage(String content, UUID parentId);

    ZonedDateTime modifyMessageContent(UUID id, String content);

    void deleteMessage(UUID id);

    List<Message> getAllChildMessages(UUID parentId);
}
