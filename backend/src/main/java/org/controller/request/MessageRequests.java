package org.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.UUID;

public class MessageRequests {

    private MessageRequests() throws IllegalAccessException {
        throw new IllegalAccessException("Single instance protection");
    }

    public static final class Create implements Serializable {

        private static final long serialVersionUID = 5465210130498384045L;

        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;
        private UUID parentId;

        public String getContent() {
            return content;
        }

        public Create setContent(String content) {
            this.content = content;
            return this;
        }

        public UUID getParentId() {
            return parentId;
        }

        public Create setParentId(UUID parentId) {
            this.parentId = parentId;
            return this;
        }

        @JsonIgnore
        public boolean isInvalid() {
            return content == null || content.trim().isEmpty();
        }

    }

    public static final class Patch implements Serializable {

        private static final long serialVersionUID = 3037744518044780414L;

        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;

        public String getContent() {
            return content;
        }

        public Patch setContent(String content) {
            this.content = content;
            return this;
        }

        @JsonIgnore
        public boolean isInvalid() {
            return content == null || content.trim().isEmpty();
        }

    }

}
