package controller;

import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.IUserService;

@RestController
public class RestUserController {
    private final IUserService service;

    @Autowired
    public RestUserController(IUserService service) {
        this.service = service;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String loginUser(@RequestBody User user) {
        return service.loginUser(user);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody User user) {
        service.registerUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
