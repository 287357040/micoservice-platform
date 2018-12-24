package com.jim.framework.tbox.portal.configuration;

import com.google.common.collect.Lists;
import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.foundation.RestRequestProvider;
import com.jim.framework.tbox.portal.service.api.RetryableRestTemplate;
import com.jim.framework.tbox.portal.service.api.StandardRestApiHandler;
import com.jim.framework.tbox.portal.service.api.ServiceAddressLocator;
import com.jim.framework.tbox.portal.PortalConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.jim.framework.tbox.portal.service.api.RestTemplateFactory;
/**
 * Created by celiang.hu on 2018-12-15.
 */
@Configuration
public class AppConfiguration {

    @Value("${tbox.portal.envs:DEV}")
    private String[] supportEnvs;

    @Value("${tbox.superAdmin:}")
    private String[] admins;

    @Value("${tbox.email.supported.envs:}")
    private String[] emailSupportedEnvs;

    @Value("${tbox.configView.memberOnly.envs:}")
    private String[] configViewMemberOnlyEnvs;

    @Value("${tbox.api.connectTimeout:3000}")
    private int connectTimeout;

    @Value("${tbox.api.readTimeout:10000}")
    private int readTimeout;

    @Value("${tbox.portal.address:}")
    private String portalAddress;

    @Value("${tbox.emergencyPublish.supported.envs:}")
    private String[] emergencyPublishSupportedEnvs;


    @Bean
    public PortalConfig portalConfig() {
        PortalConfig config = new PortalConfig(supportEnvs, emailSupportedEnvs, emergencyPublishSupportedEnvs,
                configViewMemberOnlyEnvs);
        config.setAdmin(Lists.newArrayList(admins));
        config.setConnectTimeout(connectTimeout);
        config.setReadTimeout(readTimeout);
        config.setProtalAddress(portalAddress);
        return config;
    }


    @Bean
    public RestTemplateFactory restTemplateFactory(final HttpMessageConverters converters) {
        return new RestTemplateFactory(portalConfig(), converters);
    }
    @Bean
    public ServiceAddressLocator serviceAddressLocator(){
        return new ServiceAddressLocator();
    }

    @Bean
    public RetryableRestTemplate retryableRestTemplate(){
        return new RetryableRestTemplate();
    }

    @Bean
    public RestApiHandler restApiHandler(){
        StandardRestApiHandler restApiHandler = new StandardRestApiHandler();
        restApiHandler.setRequestProvider(new RestRequestProvider.Default());
        restApiHandler.setRestTemplate(retryableRestTemplate());
        return restApiHandler;
    }
}
