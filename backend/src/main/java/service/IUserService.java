package service;

import model.User;

public interface IUserService {
    void registerUser(User user);

    /**
     * @return authorization token
     */
    String loginUser(User user);
}
