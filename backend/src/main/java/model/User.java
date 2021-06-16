package model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -7957258751882803869L;
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isInvalid() {
        return login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty();
    }
}
