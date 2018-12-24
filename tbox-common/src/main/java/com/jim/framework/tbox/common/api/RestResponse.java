package com.jim.framework.tbox.common.api;

/**
 * Created by celiang.hu on 2018-12-07.
 */
public class RestResponse<T> {
    private boolean isSucess;
    private String errorMessage;
    private T result;

    public boolean isSucess() {
        return isSucess;
    }

    public void setSucess(boolean sucess) {
        isSucess = sucess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
