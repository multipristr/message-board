package org.repository;

import org.model.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMessageRepository {

    Message saveMessage(Message message);

    Message updateMessage(Message message);

    void deleteMessage(UUID id);

    Optional<Message> selectOneMessage(UUID id);

    default List<Message> selectTopLevelMessages() {
        return selectChildMessages(null);
    }

    List<Message> selectChildMessages(UUID parentId);

}
