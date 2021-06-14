package repository;

import model.Message;

import java.util.List;
import java.util.UUID;

public interface IMessageRepository {
    void save(Message message);

    void update(UUID id, String content);

    void delete(UUID id);

    List<Message> selectAll();
}
