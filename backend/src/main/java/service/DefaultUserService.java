package service;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.IUserRepository;

@Service
public class DefaultUserService implements IUserService {
    private final PasswordEncoder encoder;
    private final IUserRepository repository;

    @Autowired
    public DefaultUserService(PasswordEncoder encoder, IUserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
        repository.saveUser(new User().setLogin("user").setPassword(encoder.encode("password"))); // TODO FIXME remove
    }

    @Override
    public void registerUser(User user) {
        if (user.isInvalid()) {
            throw new IllegalArgumentException("Invalid user credentials " + user.getLogin());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repository.saveUser(user);
    }
}
