package com.jim.framework.tbox.portal.service.api;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.core.RegisterDomainConsts;
import com.jim.framework.tbox.core.dto.ServiceDTO;
import com.jim.framework.tbox.core.enums.Env;
import com.jim.framework.tbox.core.enums.Tool;
import com.jim.framework.tbox.portal.PortalConfig;
import com.jim.framework.tbox.portal.util.TboxThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by celiang.hu on 2018-12-15.
 */
public class ServiceAddressLocator {
    public static final Logger logger = LoggerFactory.getLogger(ServiceAddressLocator.class);
    private static final long NORMAL_REFRESH_INTERVAL = 5 * 60 * 1000;
    private static final long OFFLINE_REFRESH_INTERVAL = 10 * 1000;
    private static final String ADMIN_SERVICE_URL_PATH = "/services/admin";
    private static final String CONFIG_SERVICE_URL_PATH = "/services/config-admin";
    private static final int RETRY_TIMES = 3;


    private Map<Env, List<ServiceDTO>> adminCache = new ConcurrentHashMap<>();
    private Map<Env, List<ServiceDTO>> configCache = new ConcurrentHashMap<>();
    private ScheduledExecutorService refreshServerAddressService;
    private RestTemplate restTemplate;
    @Autowired
    private PortalConfig portalConfig;
    @Autowired
    private RestTemplateFactory restTemplateFactory;

    private List<Env> allEnvs;

    @PostConstruct
    private void init() {
        restTemplate = restTemplateFactory.getObject();
        allEnvs = portalConfig.getSupportEnvs();
        refreshServerAddressService = Executors.newScheduledThreadPool(1, TboxThreadFactory.create("ServiceLocator", true));
        refreshServerAddressService.schedule(new RefreshServerAddressTask(), 1, TimeUnit.MILLISECONDS);
    }

    public List<ServiceDTO> getServiceList(RestRequest request) {
        List<ServiceDTO> services = Collections.emptyList();
        // TODO 后续考虑要支持所有的工具
        if (request.getToolType() == Tool.ADMIN) {
            services = adminCache.get(request.getEnv());
        }
        if (request.getToolType() == Tool.CONFIG) {
            services = configCache.get(request.getEnv());
        }
        List<ServiceDTO> randomAdminServices = Lists.newArrayList(services);
        Collections.shuffle(randomAdminServices);
        return randomAdminServices;
    }

    private class RefreshServerAddressTask implements Runnable {
        @Override
        public void run() {
            boolean refreshSuccess = false;

            for (Env env : allEnvs) {
                boolean currentEnvRefreshResult = refreshServerAddressCache(env);
                refreshSuccess = refreshSuccess && currentEnvRefreshResult;
            }
            if (refreshSuccess) {
                refreshServerAddressService.schedule(new RefreshServerAddressTask(), NORMAL_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
            } else {
                refreshServerAddressService.schedule(new RefreshServerAddressTask(), OFFLINE_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);

            }
        }
    }

    private boolean refreshServerAddressCache(Env env) {
        for (int i = 0; i < RETRY_TIMES; i++) {
            try {
                List<ServiceDTO> adminServers = getAdminServerAddress(env);
                List<ServiceDTO> configServers = getConfigServerAddress(env);
                // TODO 这里考虑应用场景
                if (CollectionUtils.isEmpty(adminServers) && CollectionUtils.isEmpty(configServers)) {
                    continue;
                }
                adminCache.put(env, adminServers);
                configCache.put(env, configServers);
                return true;
            } catch (Throwable e) {
                logger.error(String.format("Get admin server address from meta server failed. env: %s, meta server address:%s",
                        env, RegisterDomainConsts.getDomain(env)), e);
            }
        }
        return false;
    }

    private List<ServiceDTO> getAdminServerAddress(Env env) {
        String domainName = RegisterDomainConsts.getDomain(env);
        String url = domainName + ADMIN_SERVICE_URL_PATH;
        ServiceDTO[] result = restTemplate.getForObject(url, ServiceDTO[].class);
        return Lists.newArrayList(result);
    }

    private List<ServiceDTO> getConfigServerAddress(Env env) {
        String domainName = RegisterDomainConsts.getDomain(env);
        String url = domainName + CONFIG_SERVICE_URL_PATH;
        ServiceDTO[] result = restTemplate.getForObject(url, ServiceDTO[].class);
        return Lists.newArrayList(result);
    }
}

