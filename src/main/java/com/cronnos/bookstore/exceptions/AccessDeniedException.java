package com.cronnos.bookstore.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.Principal;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccessDeniedException extends RuntimeException {
    private String username;

    public AccessDeniedException(String message, Principal principal) {
        super(message);
        this.username = principal.getName();
    }
}
