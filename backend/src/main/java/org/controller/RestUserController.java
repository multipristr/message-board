package org.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.controller.request.UserRequests;
import org.controller.response.UserResponses;
import org.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@RestController
public class RestUserController {

    private final UserService service;

    @Autowired
    public RestUserController(UserService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully logged id"),
            @ApiResponse(responseCode = "400", description = "Empty credentials"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
    })
    @PermitAll
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponses.Login loginUser(@RequestBody UserRequests.Login userDto) {
        return service.loginUser(userDto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully registered"),
            @ApiResponse(responseCode = "409", description = "Login already taken"),
            @ApiResponse(responseCode = "400", description = "Empty credentials"),
    })
    @PermitAll
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody UserRequests.Register userDto) {
        service.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, userDto.getLogin()).build();
    }

}
