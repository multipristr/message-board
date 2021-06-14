package repository;

import model.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMessageRepository {
    void save(Message message);

    void update(UUID id, String content);

    void delete(UUID id);

    Optional<Message> selectOne(UUID id);

    default List<Message> selectTopLevel() {
        return selectChildren(null);
    }

    List<Message> selectChildren(UUID parentId);
}
