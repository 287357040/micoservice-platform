package com.jim.framework.tbox.common.controller;

import com.jim.framework.tbox.common.mvc.error.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-12-27.
 */
@Controller
public class TboxErrorController implements ErrorController {
    public static final String ERROR_PATH = "/error";

    @Autowired
    private ErrorHandler errorHandler;

    @RequestMapping(value = ERROR_PATH)
    public String error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int httpStatus = response.getStatus();

        Throwable t = null;
        Object err = request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
        if (err instanceof Throwable) {
            t = (Throwable) err;
        } else {
            err = request.getAttribute("org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR");
            if (err instanceof Throwable) {
                t = (Throwable) err;
            } else {
                err = request.getAttribute("javax.servlet.error.exception");
                if (err instanceof Throwable) {
                    t = (Throwable) err;
                }
            }
        }
        this.errorHandler.handle(t, request, response);
        return null;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
