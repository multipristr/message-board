package org.service;

import io.jsonwebtoken.Jwts;
import org.controller.request.UserRequests;
import org.controller.response.UserResponses;
import org.exception.InvalidRequestBodyException;
import org.model.User;
import org.repository.IUserRepository;
import org.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class UserService {

    private final PasswordEncoder encoder;
    private final IUserRepository repository;

    @Autowired
    public UserService(PasswordEncoder encoder, IUserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    public void registerUser(UserRequests.Register userDto) {
        if (userDto.isInvalid()) {
            throw new InvalidRequestBodyException("Invalid user credentials " + userDto.getLogin());
        }
        String encodedPassword = encoder.encode(userDto.getPassword());
        User user = new User().setLogin(userDto.getLogin()).setPassword(encodedPassword);
        repository.saveUser(user);
    }

    public UserResponses.Login loginUser(UserRequests.Login userDto) {
        if (userDto.isInvalid()) {
            throw new InvalidRequestBodyException("Invalid user credentials " + userDto.getLogin());
        }
        User user = repository.selectOneUser(userDto.getLogin()).orElseThrow(() -> new BadCredentialsException("Invalid user " + userDto.getLogin()));
        if (!encoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password for user " + userDto.getLogin());
        }

        String jwtToken = createJWTToken(userDto.getLogin());
        return new UserResponses.Login().setToken(jwtToken);
    }

    private String createJWTToken(String login) {
        return "Bearer " + Jwts.builder()
                .id("MESSAGE_BOARD_JWT")
                .subject(login)
                .claim(JWTAuthorizationFilter.JWT_ROLES, Collections.singletonList("ROLE_USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1_800_000))
                .signWith(JWTAuthorizationFilter.getSignInKey())
                .compact();

    }

}
