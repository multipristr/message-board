package org.repository;

import org.model.User;

import java.util.Optional;

public interface IUserRepository {

    void saveUser(User user);

    Optional<User> selectOneUser(String login);

}
