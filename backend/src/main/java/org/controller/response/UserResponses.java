package org.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public class UserResponses {

    private UserResponses() throws IllegalAccessException {
        throw new IllegalAccessException("Single instance protection");
    }

    public static final class Login implements Serializable {

        private static final long serialVersionUID = 1770288128118236825L;

        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String token;

        public String getToken() {
            return token;
        }

        public Login setToken(String token) {
            this.token = token;
            return this;
        }

    }

}
