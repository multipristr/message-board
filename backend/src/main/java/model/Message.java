package model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = -5208261623943103835L;
    @ApiModelProperty(required = true)
    private UUID id;
    private UUID parentId;
    @ApiModelProperty(required = true)
    private ZonedDateTime createdAt;
    @ApiModelProperty(required = true)
    private String author;
    @ApiModelProperty(required = true)
    private ZonedDateTime lastModifiedAt;
    @ApiModelProperty(required = true)
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Message setCreatedAt(ZonedDateTime createdAt) {
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

    public ZonedDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public Message setLastModifiedAt(ZonedDateTime lastModifiedAt) {
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
