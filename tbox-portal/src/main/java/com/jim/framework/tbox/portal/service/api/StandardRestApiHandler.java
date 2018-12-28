package com.jim.framework.tbox.portal.service.api;

import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.common.exception.RestApiException;
import com.jim.framework.tbox.foundation.RestRequestProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by celiang.hu on 2018-12-16.
 */
public class StandardRestApiHandler implements RestApiHandler {
    private static final Logger logger = LoggerFactory.getLogger(StandardRestApiHandler.class);

    private RetryableRestTemplate restTemplate;
    private RestRequestProvider requestProvider;

    public RetryableRestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RetryableRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestRequestProvider getRequestProvider() {
        return requestProvider;
    }

    public void setRequestProvider(RestRequestProvider requestProvider) {
        this.requestProvider = requestProvider;
    }

    @Override
    public void handle(String uniqueId, String env, String[] requestParams) {
        RestRequest restRequest = requestProvider.resolveRequest(uniqueId,
                env, requestParams);
        handle(restRequest);
    }

    @Override
    public <T> T handle(String uniqueId, String env, String[] requestParams, Class<T> clazz) {
        RestRequest restRequest = requestProvider.resolveRequest(uniqueId,
                env, requestParams, clazz);
        return handle(restRequest, clazz);
    }

    @Override
    public void handle(String uniqueId, String env, String[] requestParams, Object metaObject) {
        RestRequest restRequest = requestProvider.resolveRequest(uniqueId,
                env, requestParams, metaObject);
        handle(restRequest);
    }

    @Override
    public <T> T handle(String uniqueId, String env, String[] requestParams, Object metaObject, Class<T> clazz) {
        RestRequest restRequest = requestProvider.resolveRequest(uniqueId,
                env, requestParams, metaObject);
        return handle(restRequest, clazz);
    }

    @Override
    public void handle(RestRequest restRequest) {
        handle(restRequest, Void.class);
    }

    @Override
    public <T> T handle(RestRequest restRequest, Class<T> clazz) {
        return doHandle(restRequest, clazz);
    }

    private <T> T doHandle(RestRequest restRequest, Class<T> clazz) {
        try {
            if (restRequest.getHttpMethod() == HttpMethod.POST) {
                return restTemplate.post(restRequest, restRequest.getUrl(), restRequest.getMetaObject(), clazz, restRequest.getUrlValiables());
            }
            if (restRequest.getHttpMethod() == (HttpMethod.GET)) {
                return restTemplate.get(restRequest, restRequest.getUrl(), clazz, restRequest.getUrlValiables());
            }
            if (restRequest.getHttpMethod() == HttpMethod.PUT) {
                restTemplate.put(restRequest, restRequest.getUrl(), restRequest.getMetaObject(), restRequest.getUrlValiables());
            }
            if (restRequest.getHttpMethod() == (HttpMethod.DELETE)) {
                restTemplate.delete(restRequest, restRequest.getUrl(), restRequest.getUrlValiables());
            }
            logger.error("invalid http metod of " + restRequest.getHttpMethod());
        } catch (HttpClientErrorException e) {
            throw new RestApiException("send request failed", e);
        } catch (Exception e) {
            throw new RestApiException("send request failed", e);
        }
        throw new RestApiException("invalid http metod of " + restRequest.getHttpMethod());
    }


}
