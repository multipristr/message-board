package repository;

import model.Message;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryMessageRepository implements IMessageRepository {
    private final Map<UUID, Message> database = new LinkedHashMap<>();

    @Override
    public UUID saveMessage(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID());
        }
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(ZonedDateTime.now());
        }
        message.setLastModifiedAt(ZonedDateTime.now());
        boolean idUsed = database.putIfAbsent(message.getId(), message) != null;
        if (idUsed) {
            throw new IllegalStateException("Duplicate message id " + message.getId());
        }
        return message.getId();
    }

    @Override
    public ZonedDateTime updateMessage(Message message) {
        message.setLastModifiedAt(ZonedDateTime.now());
        database.put(message.getId(), message);
        return message.getLastModifiedAt();
    }

    @Override
    public void deleteMessage(UUID id) {
        database.remove(id);
        database.values().stream()
                .filter(message -> id.equals(message.getParentId()))
                .forEach(message -> deleteMessage(message.getId()));
    }

    @Override
    public Optional<Message> selectOneMessage(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Message> selectChildMessages(UUID parentId) {
        return database.values().stream()
                .filter(message -> Objects.equals(parentId, message.getParentId()))
                .collect(Collectors.toList());
    }
}
