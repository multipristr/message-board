package repository;

import model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements IUserRepository {
    private final Map<String, User> database = new HashMap<>();

    @Override
    public void saveUser(User user) {
        boolean userExists = database.putIfAbsent(user.getLogin(), user) != null;
        if (userExists) {
            throw new IllegalStateException("Duplicate user " + user.getLogin());
        }
    }

    @Override
    public Optional<String> selectPassword(String login) {
        return Optional.ofNullable(database.get(login)).map(User::getPassword);
    }
}
