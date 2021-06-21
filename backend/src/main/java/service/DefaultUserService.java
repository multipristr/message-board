package service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.IUserRepository;
import security.JWTAuthorizationFilter;

import java.util.Collections;
import java.util.Date;

@Service
public class DefaultUserService implements IUserService {
    private final PasswordEncoder encoder;
    private final IUserRepository repository;

    @Autowired
    public DefaultUserService(PasswordEncoder encoder, IUserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public void registerUser(User user) {
        if (user.isInvalid()) {
            throw new IllegalArgumentException("Invalid user credentials " + user.getLogin());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repository.saveUser(user);
    }

    @Override
    public String loginUser(User user) {
        if (user.isInvalid()) {
            throw new IllegalArgumentException("Invalid user credentials " + user.getLogin());
        }
        String dbPassword = repository.selectPassword(user.getLogin()).orElseThrow(() -> new BadCredentialsException("Invalid user " + user.getLogin()));
        if (!encoder.matches(user.getPassword(), dbPassword)) {
            throw new BadCredentialsException("Invalid password for user " + user.getLogin());
        }

        return createJWTToken(user.getLogin());
    }

    private String createJWTToken(String login) {
        return "Bearer " + Jwts.builder()
                .setId("MESSAGE_BOARD_JWT")
                .setSubject(login)
                .claim(JWTAuthorizationFilter.JWT_ROLES, Collections.singletonList("ROLE_USER"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1_800_000))
                .signWith(SignatureAlgorithm.HS512, JWTAuthorizationFilter.JWT_SECRET).compact();

    }
}
