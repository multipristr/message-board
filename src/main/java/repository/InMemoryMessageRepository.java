package repository;

import model.Message;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

class InMemoryMessageRepository implements IMessageRepository {
    private final Map<UUID, Message> database = new LinkedHashMap<>();

    @Override
    public void save(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID());
        }
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(ZonedDateTime.now());
        }
        message.setLastModifiedAt(ZonedDateTime.now());
        database.put(message.getId(), message);
    }

    @Override
    public void update(UUID id, String content) {
        Message message = database.get(id);
        if (message == null) {
            throw new NoSuchElementException("No message with id " + id);
        }
        message.setContent(content);
        message.setLastModifiedAt(ZonedDateTime.now());
        database.put(id, message);
    }

    @Override
    public void delete(UUID id) {
        boolean messageNotFound = database.remove(id) == null;
        if (messageNotFound) {
            throw new NoSuchElementException("No message with id " + id);
        }
    }

    @Override
    public Optional<Message> selectOne(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Message> selectChildren(UUID parentId) {
        return database.values().stream()
                .filter(message -> Objects.equals(parentId, message.getParentId()))
                .collect(Collectors.toList());
    }
}
