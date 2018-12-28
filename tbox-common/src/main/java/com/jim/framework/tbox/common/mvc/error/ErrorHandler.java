package com.jim.framework.tbox.common.mvc.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-12-27.
 */
public interface ErrorHandler {
    void handle(Throwable t, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
