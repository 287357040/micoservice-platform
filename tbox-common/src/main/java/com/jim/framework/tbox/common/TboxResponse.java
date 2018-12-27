package com.jim.framework.tbox.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TboxResponse<T> implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TboxResponse.class);

    public static final String NOT_INITIALIZED = "not-initialized";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String UNAUTHORIZED_SERVICE_INVOKER = "unauthorized-invoker";
    public static final String VALIDATION_FAIL = "validation-fail";
    public static final String BAD_PARAMETER = "bad-parameter";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String USER_NOT_LOGIN = "user-not-login";
    public static final String RPC_FAIL = "rpc-fail";

    /**
     * auth code
     */
    public static final String SIGN_INVALID = "sign-invalid";
    public static final String SIGN_AUTHORITY_INVALID = "sign-authority-invalid";

    private String code;

    private String message;

    private T data;

    public TboxResponse() {
        this(NOT_INITIALIZED, null, null);
    }

    public TboxResponse(String code) {
        this(code, null, null);
    }

    public TboxResponse(String code, T data) {
        this(code, data, null);
    }

    public TboxResponse(String code, T data, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS.equals(this.getCode());
    }


    public static <T> TboxResponse<T> success(T data) {
        return new TboxResponse(SUCCESS, data);
    }

    public static TboxResponse fail(Throwable t) {
        return fail(t.getMessage());
    }


    public static TboxResponse fail(String message) {
        return new TboxResponse(FAIL, message, null);
    }

    public static TboxResponse fail(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return new TboxResponse(VALIDATION_FAIL, errorMap);
    }
}
