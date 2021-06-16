package controller;

import io.swagger.annotations.*;
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

    @ApiOperation(value = "Create message")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "Message text", required = true),
            @ApiImplicitParam(name = "parentId", value = "ID of message this message replies to"),
    })
    @ApiResponses(@ApiResponse(code = 201, message = "Successfully created message"))
    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> createMessage(@RequestBody String content, @RequestParam(required = false) UUID parentId) {
        UUID id = service.createMessage(content, parentId);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Modify own message")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID of message to modify", required = true),
            @ApiImplicitParam(name = "newContent", value = "New message text", required = true),
    })
    @ApiResponses(@ApiResponse(code = 204, message = "Successfully modified message"))
    @PutMapping(value = "/message/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyMessageContent(@PathVariable UUID id, @RequestBody String newContent) {
        service.modifyMessageContent(id, newContent);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete own message")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID of message to delete", required = true),
    })
    @ApiResponses(@ApiResponse(code = 204, message = "Successfully deleted message"))
    @DeleteMapping("/message/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable UUID id) {
        service.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get all messages", response = Message.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "ID of parent message, return top level messages if parentId not specified"),
    })
    @ApiResponses(@ApiResponse(code = 200, message = "Successfully retrieved list of all child messages"))
    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getAllMessages(@RequestParam(required = false) UUID parentId) {
        return service.getAllChildMessages(parentId);
    }
}
