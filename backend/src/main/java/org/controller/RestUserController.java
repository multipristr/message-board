package org.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.controller.request.UserRequests;
import org.controller.response.UserResponses;
import org.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.PermitAll;
import java.net.URI;

@RestController
public class RestUserController {

    private final UserService service;

    public RestUserController(UserService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged id"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "401", description = "Incorrect credentials"),
    })
    @PermitAll
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponses.Login loginUser(@RequestBody UserRequests.Login userDto) {
        return service.loginUser(userDto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "409", description = "Login already taken"),
    })
    @PermitAll
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerUser(@RequestBody UserRequests.Register userDto) {
        service.registerUser(userDto);
        URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/{login}").buildAndExpand(userDto.getLogin()).toUri();
        return ResponseEntity.created(location).build();
    }

}
