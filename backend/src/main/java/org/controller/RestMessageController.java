package org.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.controller.request.MessageRequests;
import org.controller.response.MessageResponses;
import org.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("messages")
@SecurityRequirement(name = "bearerAuth")
public class RestMessageController {

    private final MessageService service;

    @Autowired
    public RestMessageController(MessageService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created message"),
            @ApiResponse(responseCode = "400", description = "Empty message content"),
            @ApiResponse(responseCode = "401", description = "Not logged in"),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createMessage(@RequestBody MessageRequests.Create messageDto) {
        MessageResponses.Message message = service.createMessage(messageDto);
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, message.getId().toString()).build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully modified message, attached updated modified timestamp"),
            @ApiResponse(responseCode = "400", description = "Empty message content"),
            @ApiResponse(responseCode = "401", description = "Not logged in"),
            @ApiResponse(responseCode = "403", description = "Action not permitted for the user"),
            @ApiResponse(responseCode = "404", description = "No Message with the provided ID"),
    })
    @PatchMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageResponses.Message modifyMessage(@PathVariable UUID id, @RequestBody MessageRequests.Patch messageDto) {
        return service.modifyMessage(id, messageDto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted message"),
            @ApiResponse(responseCode = "401", description = "Not logged in"),
            @ApiResponse(responseCode = "403", description = "Action not permitted for the user"),
            @ApiResponse(responseCode = "404", description = "No Message with the provided ID"),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable UUID id) {
        service.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched all under parent ID messages"),
            @ApiResponse(responseCode = "401", description = "Not logged in"),
    })
    @GetMapping(value = "{parentId}/children", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageResponses.Message> getAllChildMessages(@PathVariable UUID parentId) {
        return service.getAllChildMessages(parentId);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched all top level messages"),
            @ApiResponse(responseCode = "401", description = "Not logged in"),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageResponses.Message> getAllTopLevelMessages() {
        return service.getAllTopLevelMessages();
    }

}
