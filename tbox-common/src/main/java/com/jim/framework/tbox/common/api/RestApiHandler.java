package com.jim.framework.tbox.common.api;

/**
 * Created by celiang.hu on 2018-12-07.
 */
public interface RestApiHandler {
    void handle(String uniqueId, String env, String[] urlValiables);
    <T> T handle(String uniqueId, String env, String[] urlValiables,Class<T> clazz);
    void handle(String uniqueId, String env,String[] urlValiables, Object metaObject);
    <T> T handle(String uniqueId, String env,String[] urlValiables, Object metaObject,Class<T> clazz);

    void handle(RestRequest request);
    <T> T handle(RestRequest request,Class<T> clazz);
}
