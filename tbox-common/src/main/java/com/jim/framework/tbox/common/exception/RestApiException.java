package com.jim.framework.tbox.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by celiang.hu on 2018-12-26.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RestApiException extends RuntimeException {
    private static final long serialVersionUID = -2811809643056759061L;

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(String message, Exception e) {
        super(message, e);
    }
}
