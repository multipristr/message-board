package controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IMessageService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class RestMessageController {
    private final IMessageService service;

    @Autowired
    public RestMessageController(IMessageService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully created message"),
            @ApiResponse(code = 400, message = "Empty message content"),
    })
    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> createMessage(@RequestBody String content, @RequestParam(required = false) UUID parentId) {
        UUID id = service.createMessage(content, parentId);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully modified message, attached updated modified timestamp"),
            @ApiResponse(code = 400, message = "Empty message content"),
            @ApiResponse(code = 404, message = "No Message with the provided ID"),
    })
    @PutMapping(value = "/message/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ZonedDateTime modifyMessageContent(@PathVariable UUID id, @RequestBody String newContent) {
        return service.modifyMessageContent(id, newContent);
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully deleted message"),
            @ApiResponse(code = 404, message = "No Message with the provided ID"),
    })
    @DeleteMapping("/message/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable UUID id) {
        service.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully fetched all top level or under parent ID messages"),
    })
    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getAllMessages(@RequestParam(required = false) UUID parentId) {
        return service.getAllChildMessages(parentId);
    }
}
