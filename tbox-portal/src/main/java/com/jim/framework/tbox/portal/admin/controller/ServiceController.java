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
import com.jim.framework.tbox.adminservice.api.AdminAPIs;
import com.jim.framework.tbox.adminservice.api.dto.ServiceDTO;
import com.jim.framework.tbox.adminservice.api.dto.ServiceDetailDTO;
import com.jim.framework.tbox.common.TboxResponse;
import com.jim.framework.tbox.common.api.RestApiHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/envs/{env}/admin/service")
public class ServiceController {

    @Autowired
    private RestApiHandler restApiHandler;


    @RequestMapping(method = RequestMethod.GET)
    public TboxResponse<Set<ServiceDTO>> searchService(@RequestParam String pattern,
                                                       @RequestParam String filter, @PathVariable String env) {
        ServiceDTO[] services = restApiHandler.handle(AdminAPIs.SEARCH_SERVICE, env, new String[]{pattern, filter}, ServiceDTO[].class);
        return TboxResponse.success(Sets.newHashSet(services));
    }

    @RequestMapping(value = "/{service}", method = RequestMethod.GET)
    public TboxResponse<ServiceDetailDTO> serviceDetail(@PathVariable String service, @PathVariable String env) {
        ServiceDetailDTO serviceDetailDTO = restApiHandler.handle(AdminAPIs.SERVICE_DETAIL, env, new String[]{service}, ServiceDetailDTO.class);
        return TboxResponse.success(serviceDetailDTO);
    }
}
