package com.fcn.fanscoin.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatorException
        extends RuntimeException {

    private final String fieldName;

    public ValidatorException(final String message, final String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
}
