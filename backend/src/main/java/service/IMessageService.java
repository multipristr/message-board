package service;

import model.Message;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface IMessageService {
    /**
     * @return ID of the newly created message
     */
    UUID createMessage(String content, UUID parentId);

    /**
     * @return updated lastModifiedAt timestamp
     */
    ZonedDateTime modifyMessageContent(UUID id, String content);

    void deleteMessage(UUID id);

    /**
     * Get all messages directly under parentId or top level messages if parentId is null
     *
     * @param parentId nullable, returns top level messages if not specified
     * @return replies to parentId or top level messages
     */
    List<Message> getAllChildMessages(UUID parentId);
}
