package service;

import model.User;

public interface IUserService {
    void registerUser(User user);

    String loginUser(User user);
}
