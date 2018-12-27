package com.jim.framework.tbox.common.mvc.error;


import com.google.gson.Gson;
import com.jim.framework.tbox.common.TboxResponse;
import com.jim.framework.tbox.common.exception.RestApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-12-27.
 */


public class DefaultErrorHandler implements ErrorHandler{
    private static final Logger logger = LoggerFactory.getLogger(DefaultErrorHandler.class);
    private Gson gson = new Gson();

    @Override
    public void handle(Throwable t, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TboxResponse tboxResponse = new TboxResponse("fail",null,t.getMessage());
        outputJsonResponse(response, tboxResponse);
    }


    public void handleError(RestApiException t, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TboxResponse tboxResponse = TboxResponse.fail(t.getMessage());
        outputJsonResponse(response, tboxResponse);
    }

    private void outputJsonResponse(HttpServletResponse response, TboxResponse tboxResponse) throws IOException {
        byte[] byteResponse = gson.toJson(tboxResponse).getBytes();
        response.setContentType("application/json");
        response.setContentLength(byteResponse.length);
        response.getOutputStream().write(byteResponse);
    }

}
