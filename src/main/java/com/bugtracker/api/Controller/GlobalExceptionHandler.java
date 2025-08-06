package com.bugtracker.api.Controller;

import com.bugtracker.api.Payload.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle not found (404)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public MessageResponse handleEntityNotFound(EntityNotFoundException ex) {
        return new MessageResponse("Error: " + ex.getMessage());
    }

    // Handle forbidden (403)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public MessageResponse handleAccessDenied(AccessDeniedException ex) {
        return new MessageResponse("Error: " + ex.getMessage());
    }

    // Handle validation errors (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageResponse handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return new MessageResponse("Error: " + errorMessage);
    }

    // Handle all other exceptions (400)
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageResponse handleRuntimeException(RuntimeException ex) {
        return new MessageResponse("Error: " + ex.getMessage());
    }
}

