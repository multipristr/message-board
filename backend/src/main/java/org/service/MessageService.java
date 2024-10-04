package org.service;

import org.controller.request.MessageRequests;
import org.controller.response.MessageResponses;
import org.exception.ActionNotPermittedException;
import org.exception.InvalidRequestBodyException;
import org.exception.MissingEntityException;
import org.model.Message;
import org.repository.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final IMessageRepository repository;

    @Autowired
    public MessageService(IMessageRepository repository) {
        this.repository = repository;
    }

    private String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .orElseThrow(() -> new BadCredentialsException("Not logged in"));
    }

    private MessageResponses.Message mapToResponse(Message message) {
        return new MessageResponses.Message()
                .setId(message.getId())
                .setCreatedAt(message.getCreatedAt())
                .setLastModifiedAt(message.getLastModifiedAt())
                .setAuthor(message.getAuthor())
                .setContent(message.getContent());
    }

    public MessageResponses.Message createMessage(MessageRequests.Create messageDto) {
        if (messageDto.isInvalid()) {
            throw new InvalidRequestBodyException("Empty message content");
        }
        Message message = new Message().setContent(messageDto.getContent()).setParentId(messageDto.getParentId()).setAuthor(getCurrentUser());

        message = repository.saveMessage(message);
        return mapToResponse(message);
    }

    public MessageResponses.Message modifyMessage(UUID id, MessageRequests.Patch messageDto) {
        if (messageDto.isInvalid()) {
            throw new InvalidRequestBodyException("Empty new modified message content in " + id);
        }
        Optional<Message> savedMessage = repository.selectOneMessage(id);
        if (!savedMessage.isPresent()) {
            throw new MissingEntityException("No message id " + id);
        }
        String currentUser = getCurrentUser();
        Message modifiedMessage = savedMessage.get();
        if (!modifiedMessage.getAuthor().equals(currentUser)) {
            throw new ActionNotPermittedException("User '" + currentUser + "' can't modify message " + id);
        }
        modifiedMessage.setContent(messageDto.getContent());

        Message message = repository.updateMessage(modifiedMessage);
        return mapToResponse(message);
    }

    public void deleteMessage(UUID id) {
        Optional<Message> savedMessage = repository.selectOneMessage(id);
        if (!savedMessage.isPresent()) {
            throw new MissingEntityException("No message id " + id);
        }
        String currentUser = getCurrentUser();
        if (!savedMessage.get().getAuthor().equals(currentUser)) {
            throw new ActionNotPermittedException("User '" + currentUser + "' can't delete message " + id);
        }
        repository.deleteMessage(id);
    }

    public List<MessageResponses.Message> getAllChildMessages(UUID parentId) {
        return repository.selectChildMessages(parentId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<MessageResponses.Message> getAllTopLevelMessages() {
        return repository.selectTopLevelMessages().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

}
