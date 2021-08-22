package com.fcn.fanscoin.exception;

import com.fcn.fanscoin.constant.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException
        extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;
    private final String errorKey;

    public BadRequestAlertException(final String defaultMessage,
                                    final String entityName,
                                    final String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(final URI type, final String defaultMessage,
                                    final String entityName, final String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(final String entityName, final String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
