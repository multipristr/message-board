package org.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = -5208261623943103835L;

    private UUID id;
    private UUID parentId;
    private Instant createdAt;
    private String author;
    private Instant lastModifiedAt;
    private String content;

    public UUID getId() {
        return id;
    }

    public Message setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getParentId() {
        return parentId;
    }

    public Message setParentId(UUID parentId) {
        this.parentId = parentId;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Message setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Message setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public Message setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

}
