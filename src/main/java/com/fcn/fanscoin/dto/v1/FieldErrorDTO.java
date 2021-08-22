package com.fcn.fanscoin.dto.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldErrorDTO {

    private final String objectName;
    private final String field;
    private final String message;
}
