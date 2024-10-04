package org.controller;

import org.exception.ActionNotPermittedException;
import org.exception.DuplicateIdException;
import org.exception.InvalidRequestBodyException;
import org.exception.MissingEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DuplicateIdException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Cannot create because of duplicated ID";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {InvalidRequestBodyException.class})
    protected ResponseEntity<Object> handleInvalid(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Invalid entity in request body";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {MissingEntityException.class})
    protected ResponseEntity<Object> handleMissing(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "No entity with a matching ID";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ActionNotPermittedException.class})
    protected ResponseEntity<Object> handleAccess(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Insufficient permissions for action";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleUnauthorised(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Invalid username or password";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}
