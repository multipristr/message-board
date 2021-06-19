package repository;

import model.Message;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMessageRepository {
    UUID saveMessage(Message message);

    ZonedDateTime updateMessage(Message message);

    void deleteMessage(UUID id);

    Optional<Message> selectOneMessage(UUID id);

    default List<Message> selectTopLevelMessages() {
        return selectChildMessages(null);
    }

    List<Message> selectChildMessages(UUID parentId);
}
