package com.jim.framework.tbox.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by celiang.hu on 2018-12-14.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SystemException extends RuntimeException {
    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }
}
