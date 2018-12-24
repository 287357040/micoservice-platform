/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jim.framework.tbox.adminservice.controller;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.jim.framework.tbox.adminservice.service.ConsumerService;
import com.jim.framework.tbox.adminservice.service.ProviderService;
import com.jim.framework.tbox.common.domain.admin.Consumer;
import com.jim.framework.tbox.common.domain.admin.Provider;
import com.jim.framework.tbox.common.dto.admin.ServiceDTO;
import com.jim.framework.tbox.common.dto.admin.ServiceDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/admin/service")
public class ServiceController {

    private final ProviderService providerService;
    private final ConsumerService consumerService;

    @Autowired
    public ServiceController(ProviderService providerService, ConsumerService consumerService) {
        this.providerService = providerService;
        this.consumerService = consumerService;
    }

    @RequestMapping(value = "/pattern/{pattern}/filter/{filter}", method = RequestMethod.GET)
    public Set<ServiceDTO> searchService(@PathVariable String pattern,
                                         @PathVariable String filter) {

        List<Provider> providers = new ArrayList<>();
        if (!filter.contains("*") && !filter.contains("?")) {
            if (pattern.equals("ip")) {
                providers = providerService.findByAddress(filter);
            } else if (pattern.equals("serviceName")) {
                providers = providerService.findByService(filter);
            } else if (pattern.equals("application")) {
                providers = providerService.findByApplication(filter);
            }
        } else {
            List<String> candidates = Collections.emptyList();
            if (pattern.equals("serviceName")) {
                candidates = providerService.findServices();
            } else if (pattern.equals("application")) {
                candidates = providerService.findApplications();
            }
            filter = filter.toLowerCase().replace(".", "\\.");
            if (filter.startsWith("*")) {
                filter = "." + filter;
            }
            Pattern regex = Pattern.compile(filter);
            for (String candidate : candidates) {
                Matcher matcher = regex.matcher(candidate);
                if (matcher.matches() || matcher.lookingAt()) {
                    if (pattern.equals("serviceName")) {
                        providers.addAll(providerService.findByService(candidate));
                    } else {
                        providers.addAll(providerService.findByApplication(candidate));
                    }
                }
            }
        }

        Set<ServiceDTO> result = new TreeSet<>();
        for (Provider provider : providers) {
            Map<String, String> map = StringUtils.parseQueryString(provider.getParameters());
            String app = provider.getApplication();
            String service = map.get(Constants.INTERFACE_KEY);
            String group = map.get(Constants.GROUP_KEY);
            String version = map.get(Constants.VERSION_KEY);
            ServiceDTO s = new ServiceDTO();
            s.setAppName(app);
            s.setService(service);
            s.setGroup(group);
            s.setVersion(version);
            result.add(s);
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ServiceDetailDTO serviceDetail(@RequestParam(name = "service") String service) {
        service = service.replace("*", "/");
        List<Provider> providers = providerService.findByService(service);

        List<Consumer> consumers = consumerService.findByService(service);

        ServiceDetailDTO serviceDetailDTO = new ServiceDetailDTO();
        serviceDetailDTO.setConsumers(consumers);
        serviceDetailDTO.setProviders(providers);
        return serviceDetailDTO;
    }
}
