package org.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public class UserRequests {

    private UserRequests() throws IllegalAccessException {
        throw new IllegalAccessException("Single instance protection");
    }

    public static final class Register implements Serializable {

        private static final long serialVersionUID = -883717680259485427L;

        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String login;
        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String password;

        public String getLogin() {
            return login;
        }

        public Register setLogin(String login) {
            this.login = login;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Register setPassword(String password) {
            this.password = password;
            return this;
        }

        @JsonIgnore
        public boolean isInvalid() {
            return login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty();
        }

    }

    public static final class Login implements Serializable {

        private static final long serialVersionUID = 5026084312603177814L;

        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String login;
        @Schema(minLength = 1, requiredMode = Schema.RequiredMode.REQUIRED)
        private String password;

        public String getLogin() {
            return login;
        }

        public Login setLogin(String login) {
            this.login = login;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Login setPassword(String password) {
            this.password = password;
            return this;
        }

        @JsonIgnore
        public boolean isInvalid() {
            return login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty();
        }

    }

}
