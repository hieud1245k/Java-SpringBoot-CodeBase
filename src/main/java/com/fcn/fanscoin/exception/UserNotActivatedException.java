package com.fcn.fanscoin.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class UserNotActivatedException
        extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(final String message) {
        super(message);
    }

    public UserNotActivatedException(final String message, final Throwable t) {
        super(message, t);
    }
}
