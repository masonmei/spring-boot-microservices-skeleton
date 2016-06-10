package com.igitras.uaa.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author mason
 */
public class UserNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = -5309171427535062980L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
