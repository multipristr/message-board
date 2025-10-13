package org.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.controller.request.MessageRequests;
import org.controller.request.UserRequests;
import org.controller.response.MessageResponses;
import org.exception.ActionNotPermittedException;
import org.exception.InvalidRequestBodyException;
import org.exception.MissingEntityException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.model.User;
import org.repository.IUserRepository;
import org.security.SecurityConfig;
import org.service.MessageService;
import org.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(value = RestMessageController.class)
@Import({RestMessageController.class, RestExceptionHandler.class})
@ContextConfiguration(classes = SecurityConfig.class)
class RestMessageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MessageService service;

    private String createToken() {
        IUserRepository repository = Mockito.mock();
        Mockito.when(repository.selectOneUser(Mockito.any())).thenReturn(Optional.of(new User()));
        PasswordEncoder encoder = Mockito.mock();
        Mockito.when(encoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
        UserRequests.Login request = new UserRequests.Login().setLogin("login").setPassword("password");
        return new UserService(encoder, repository).loginUser(request).getToken();
    }

    @Test
    void createMessage() throws Exception {
        MessageRequests.Create request = new MessageRequests.Create().setContent("content");
        MessageResponses.Message response = new MessageResponses.Message()
                .setCreatedAt(Instant.now())
                .setAuthor("author")
                .setId(UUID.randomUUID())
                .setContent(request.getContent());
        Mockito.when(service.createMessage(Mockito.any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.not(Matchers.blankOrNullString())));
    }

    @Test
    void createMessageEmptyContent() throws Exception {
        MessageRequests.Create request = new MessageRequests.Create().setContent("");
        Mockito.when(service.createMessage(Mockito.any())).thenThrow(InvalidRequestBodyException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void modifyMessage() throws Exception {
        MessageRequests.Patch request = new MessageRequests.Patch().setContent("content");
        MessageResponses.Message response = new MessageResponses.Message()
                .setCreatedAt(Instant.now())
                .setAuthor("author")
                .setId(UUID.randomUUID())
                .setLastModifiedAt(Instant.now())
                .setContent(request.getContent());
        UUID id = UUID.randomUUID();
        Mockito.when(service.modifyMessage(Mockito.eq(id), Mockito.any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.patch("/messages/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void modifyMessageEmptyContent() throws Exception {
        MessageRequests.Patch request = new MessageRequests.Patch().setContent("");
        UUID id = UUID.randomUUID();
        Mockito.when(service.modifyMessage(Mockito.eq(id), Mockito.any())).thenThrow(InvalidRequestBodyException.class);
        mockMvc.perform(MockMvcRequestBuilders.patch("/messages/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void modifyMessageWrongId() throws Exception {
        MessageRequests.Patch request = new MessageRequests.Patch().setContent("content");
        UUID id = UUID.randomUUID();
        Mockito.when(service.modifyMessage(Mockito.eq(id), Mockito.any())).thenThrow(MissingEntityException.class);
        mockMvc.perform(MockMvcRequestBuilders.patch("/messages/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void modifyMessageDifferentUser() throws Exception {
        MessageRequests.Patch request = new MessageRequests.Patch().setContent("content");
        UUID id = UUID.randomUUID();
        Mockito.when(service.modifyMessage(Mockito.eq(id), Mockito.any())).thenThrow(ActionNotPermittedException.class);
        mockMvc.perform(MockMvcRequestBuilders.patch("/messages/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/{id}", UUID.randomUUID())
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteMessageNonExistent() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doThrow(MissingEntityException.class).when(service).deleteMessage(Mockito.eq(id));
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/{id}", id)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteMessageDifferentUser() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doThrow(ActionNotPermittedException.class).when(service).deleteMessage(Mockito.eq(id));
        mockMvc.perform(MockMvcRequestBuilders.delete("/messages/{id}", id)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getAllChildMessages() throws Exception {
        List<MessageResponses.Message> response = Collections.singletonList(new MessageResponses.Message()
                .setCreatedAt(Instant.now())
                .setAuthor("author")
                .setId(UUID.randomUUID())
                .setContent("content"));
        UUID parentId = UUID.randomUUID();
        Mockito.when(service.getAllChildMessages(Mockito.eq(parentId))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages/{parentId}/children", parentId)
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getAllTopLevelMessages() throws Exception {
        List<MessageResponses.Message> response = Collections.singletonList(new MessageResponses.Message()
                .setCreatedAt(Instant.now())
                .setAuthor("author")
                .setId(UUID.randomUUID())
                .setLastModifiedAt(Instant.now())
                .setContent("content"));
        Mockito.when(service.getAllTopLevelMessages()).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/messages")
                        .header("Authorization", createToken())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }
}