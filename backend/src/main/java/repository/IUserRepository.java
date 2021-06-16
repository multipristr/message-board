package repository;

import model.User;

import java.util.Optional;

public interface IUserRepository {
    void saveUser(User user);

    Optional<String> selectPassword(String login);
}
