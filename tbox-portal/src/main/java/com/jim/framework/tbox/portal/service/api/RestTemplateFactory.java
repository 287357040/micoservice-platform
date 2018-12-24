package com.jim.framework.tbox.portal.service.api;

import com.jim.framework.tbox.portal.PortalConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by celiang.hu on 2018-12-16.
 */

public class RestTemplateFactory implements FactoryBean<RestTemplate>, InitializingBean {

    private final PortalConfig portalConfig;
    private final HttpMessageConverters messageConverters;

    private RestTemplate restTemplate;

    public RestTemplateFactory(PortalConfig portalConfig, HttpMessageConverters messageConverters) {
        this.portalConfig = portalConfig;
        this.messageConverters = messageConverters;
    }

    @Override
    public RestTemplate getObject() {
        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        restTemplate = new RestTemplate(messageConverters.getConverters());
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(portalConfig.getConnectTimeout());
        requestFactory.setReadTimeout(portalConfig.getReadTimeout());
        restTemplate.setRequestFactory(requestFactory);

    }
}
