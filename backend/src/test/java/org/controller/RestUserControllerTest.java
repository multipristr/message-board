package org.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.controller.request.UserRequests;
import org.controller.response.UserResponses;
import org.exception.DuplicateIdException;
import org.exception.InvalidRequestBodyException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.security.SecurityConfig;
import org.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = RestUserController.class)
@Import({RestUserController.class, RestExceptionHandler.class})
@ContextConfiguration(classes = SecurityConfig.class)
class RestUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService service;

    @Test
    void loginUser() throws Exception {
        UserRequests.Login request = new UserRequests.Login().setLogin("login").setPassword("password");
        UserResponses.Login response = new UserResponses.Login().setToken("token");
        Mockito.when(service.loginUser(Mockito.any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void loginUserEmptyCredentials() throws Exception {
        UserRequests.Login request = new UserRequests.Login().setLogin("login").setPassword("");
        Mockito.when(service.loginUser(Mockito.any())).thenThrow(InvalidRequestBodyException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void loginUserWrongPassword() throws Exception {
        UserRequests.Login request = new UserRequests.Login().setLogin("login").setPassword("wrongPassword");
        Mockito.when(service.loginUser(Mockito.any())).thenThrow(BadCredentialsException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void registerUser() throws Exception {
        UserRequests.Register request = new UserRequests.Register().setLogin("login").setPassword("password");
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.not(Matchers.blankOrNullString())));
    }

    @Test
    void registerUserEmptyCredentials() throws Exception {
        UserRequests.Register request = new UserRequests.Register().setLogin("login").setPassword("");
        Mockito.doThrow(InvalidRequestBodyException.class).when(service).registerUser(Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void registerUserDuplicate() throws Exception {
        UserRequests.Register request = new UserRequests.Register().setLogin("login").setPassword("password");
        Mockito.doThrow(DuplicateIdException.class).when(service).registerUser(Mockito.any());
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}