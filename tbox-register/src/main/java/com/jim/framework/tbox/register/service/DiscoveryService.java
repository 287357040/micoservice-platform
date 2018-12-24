package com.jim.framework.tbox.register.service;

import com.jim.framework.tbox.core.ServiceNameConsts;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DiscoveryService {

    public static final Logger logger = LoggerFactory.getLogger(DiscoveryService.class);

    @Autowired
    private EurekaClient eurekaClient;

    public List<InstanceInfo> getApolloConfigServiceInstances() {
        Application application = eurekaClient.getApplication(ServiceNameConsts.TBOX_APOLLO_CONFIGSERVICE);
        if (application == null) {
            logger.info("Tbox.EurekaDiscovery.NotFound,{0}", ServiceNameConsts.TBOX_APOLLO_CONFIGSERVICE);
            // Tracer.logEvent("Apollo.EurekaDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
        }
        return application != null ? application.getInstances() : Collections.<InstanceInfo>emptyList();
    }

    public List<InstanceInfo> getMetaServiceInstances() {
        Application application = eurekaClient.getApplication(ServiceNameConsts.TBOX_REGISTERSERVICE);
        if (application == null) {
            logger.info("Tbox.EurekaDiscovery.NotFound,{0}", ServiceNameConsts.TBOX_REGISTERSERVICE);

//            Tracer.logEvent("Apollo.EurekaDiscovery.NotFound", ServiceNameConsts.APOLLO_METASERVICE);
        }
        return application != null ? application.getInstances() : Collections.<InstanceInfo>emptyList();
    }

    public List<InstanceInfo> getAdminServiceInstances() {
        Application application = eurekaClient.getApplication(ServiceNameConsts.TBOX_ADMINSERVICE);
        if (application == null) {
            logger.info("Tbox.EurekaDiscovery.NotFound,{0}", ServiceNameConsts.TBOX_ADMINSERVICE);

//            Tracer.logEvent("Apollo.EurekaDiscovery.NotFound", ServiceNameConsts.APOLLO_METASERVICE);
        }
        return application != null ? application.getInstances() : Collections.<InstanceInfo>emptyList();
    }

    public List<InstanceInfo> getApolloAdminServiceInstances() {
        Application application = eurekaClient.getApplication(ServiceNameConsts.TBOX_APOLLO_ADMINSERVICE);
        if (application == null) {
            logger.info("Tbox.EurekaDiscovery.NotFound,{0}", ServiceNameConsts.TBOX_APOLLO_ADMINSERVICE);

//            Tracer.logEvent("Apollo.EurekaDiscovery.NotFound", ServiceNameConsts.APOLLO_ADMINSERVICE);
        }
        return application != null ? application.getInstances() : Collections.<InstanceInfo>emptyList();
    }
}

