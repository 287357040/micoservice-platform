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

package com.jim.framework.tbox.portal.admin.controller;

import com.google.common.collect.Sets;
import com.jim.framework.tbox.common.dto.admin.ServiceDTO;
import com.jim.framework.tbox.common.dto.admin.ServiceDetailDTO;
import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.core.enums.Env;
import com.jim.framework.tbox.core.enums.Tool;
import com.jim.framework.tbox.foundation.RestRequestProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.HttpMethod;
import java.util.*;

import static org.springframework.http.HttpMethod.GET;


@RestController
@RequestMapping("/envs/{env}/admin/service")
public class ServiceController {

    @Autowired
    private RestApiHandler restApiHandler;


    @RequestMapping(method = RequestMethod.GET)
    public Set<ServiceDTO> searchService(@RequestParam String pattern,
                                         @RequestParam String filter, @PathVariable String env) {
        ServiceDTO[] services = restApiHandler.handle("admin.searchService", env, new String[]{pattern, filter}, ServiceDTO[].class);
        return new HashSet<>(Arrays.asList(services));
    }

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public ServiceDetailDTO serviceDetail(@PathVariable String service, @PathVariable String env) {
        ServiceDetailDTO serviceDetailDTO = restApiHandler.handle("admin.serviceDetail", env, new String[]{service}, ServiceDetailDTO.class);
        return serviceDetailDTO;
    }
}
