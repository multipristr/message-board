package org.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class MessageResponses {

    private MessageResponses() throws IllegalAccessException {
        throw new IllegalAccessException("Single instance protection");
    }

    public static final class Message implements Serializable {

        private static final long serialVersionUID = -1761680825721786367L;

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private UUID id;
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private Instant createdAt;
        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String author;
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        private Instant lastModifiedAt;
        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;

        public UUID getId() {
            return id;
        }

        public Message setId(UUID id) {
            this.id = id;
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

}
