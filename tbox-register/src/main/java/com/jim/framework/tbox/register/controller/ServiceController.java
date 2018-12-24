package com.jim.framework.tbox.register.controller;

import com.jim.framework.tbox.core.dto.ServiceDTO;
import com.jim.framework.tbox.register.service.DiscoveryService;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private DiscoveryService discoveryService;


    @RequestMapping("/meta")
    public List<ServiceDTO> getMetaService() {
        List<InstanceInfo> instances = discoveryService.getMetaServiceInstances();
        List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {

            @Override
            public ServiceDTO apply(InstanceInfo instance) {
                return getServiceDTO(instance);
            }

        }).collect(Collectors.toList());
        return result;
    }

    @RequestMapping("/config")
    public List<ServiceDTO> getConfigService(
            @RequestParam(value = "appId", defaultValue = "") String appId,
            @RequestParam(value = "ip", required = false) String clientIp) {
        List<InstanceInfo> instances = discoveryService.getApolloConfigServiceInstances();
        List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {
            @Override
            public ServiceDTO apply(InstanceInfo instance) {
                return getServiceDTO(instance);
            }
        }).collect(Collectors.toList());
        return result;
    }

    @RequestMapping("/admin")
    public List<ServiceDTO> getAdminService() {
        List<InstanceInfo> instances = discoveryService.getAdminServiceInstances();
        List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {

            @Override
            public ServiceDTO apply(InstanceInfo instance) {
                return getServiceDTO(instance);
            }

        }).collect(Collectors.toList());
        return result;
    }

    @RequestMapping("/config-admin")
    public List<ServiceDTO> getApolloAdminService() {
        List<InstanceInfo> instances = discoveryService.getApolloAdminServiceInstances();
        List<ServiceDTO> result = instances.stream().map(new Function<InstanceInfo, ServiceDTO>() {

            @Override
            public ServiceDTO apply(InstanceInfo instance) {
                return getServiceDTO(instance);
            }

        }).collect(Collectors.toList());
        return result;
    }

    private ServiceDTO getServiceDTO(InstanceInfo instance) {
        ServiceDTO service = new ServiceDTO();
        service.setAppName(instance.getAppName());
        service.setInstanceId(instance.getInstanceId());
        service.setHomepageUrl(instance.getHomePageUrl());
        return service;
    }
}
