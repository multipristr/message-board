package controller;

import model.Message;
import service.IMessageService;

import java.util.List;
import java.util.UUID;

public class RestMessageController {
    private final IMessageService service;

    public RestMessageController(IMessageService service) {
        this.service = service;
    }

    // POST, parentId optional
    public void createMessage(String content, UUID parentId) {
        service.createMessage(content, parentId);
    }

    // PUT
    public void modifyMessageContent(UUID id, String newContent) {
        service.modifyMessageContent(id, newContent);
    }

    // DELETE
    public void deleteMessage(UUID id) {
        service.deleteMessage(id);
    }

    // GET, parentId optional
    public List<Message> getAllMessages(UUID parentId) {
        return service.getAllChildMessages(parentId);
    }
}
