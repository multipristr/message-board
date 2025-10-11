package org.repository;

import org.exception.DuplicateIdException;
import org.model.Message;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryMessageRepository implements IMessageRepository {

    private final Map<UUID, Message> database = new HashMap<>();

    @Override
    public Message saveMessage(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID());
        }
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(Instant.now());
        }
        message.setLastModifiedAt(Instant.now());
        boolean idUsed = database.putIfAbsent(message.getId(), message) != null;
        if (idUsed) {
            throw new DuplicateIdException("Duplicate message id " + message.getId());
        }
        return message;
    }

    @Override
    public Message updateMessage(Message message) {
        message.setLastModifiedAt(Instant.now());
        database.put(message.getId(), message);
        return message;
    }

    @Override
    public void deleteMessage(UUID id) {
        database.remove(id);
        new ArrayList<>(database.values()).stream()
                .filter(message -> id.equals(message.getParentId()))
                .forEach(message -> deleteMessage(message.getId()));
    }

    @Override
    public Optional<Message> selectOneMessage(UUID id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Message> selectTopLevelMessages() {
        return database.values().stream()
                .filter(message -> message.getParentId() == null)
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> selectChildMessages(UUID parentId) {
        return database.values().stream()
                .filter(message -> Objects.equals(parentId, message.getParentId()))
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());
    }
}
