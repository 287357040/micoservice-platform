package com.jim.framework.tbox.common.api;

/**
 * Created by celiang.hu on 2018-12-07.
 */
public interface RestApiHandler {
    void handle(String uniqueId, String env, String[] requestParams);
    <T> T handle(String uniqueId, String env, String[] requestParams,Class<T> clazz);
    void handle(String uniqueId, String env,String[] requestParams, Object metaObject);
    <T> T handle(String uniqueId, String env,String[] requestParams, Object metaObject,Class<T> clazz);

    void handle(RestRequest request);
    <T> T handle(RestRequest request,Class<T> clazz);
}
