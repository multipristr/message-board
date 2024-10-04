package org.repository;

import org.exception.DuplicateIdException;
import org.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements IUserRepository {

    private final Map<String, User> database = new HashMap<>();

    @Override
    public void saveUser(User user) {
        boolean userExists = database.putIfAbsent(user.getLogin(), user) != null;
        if (userExists) {
            throw new DuplicateIdException("Duplicate user " + user.getLogin());
        }
    }

    @Override
    public Optional<User> selectOneUser(String login) {
        return Optional.ofNullable(database.get(login));
    }

}
