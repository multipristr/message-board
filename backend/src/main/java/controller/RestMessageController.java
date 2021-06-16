package controller;

import model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IMessageService;

import java.util.List;
import java.util.UUID;

@RestController
public class RestMessageController {
    private final IMessageService service;

    @Autowired
    public RestMessageController(IMessageService service) {
        this.service = service;
    }

    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> createMessage(@RequestBody String content, @RequestParam(required = false) UUID parentId) {
        UUID id = service.createMessage(content, parentId);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/message/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyMessageContent(@PathVariable UUID id, @RequestBody String newContent) {
        service.modifyMessageContent(id, newContent);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable UUID id) {
        service.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getAllMessages(@RequestParam(required = false) UUID parentId) {
        return service.getAllChildMessages(parentId);
    }
}
