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

import com.jim.framework.tbox.adminservice.api.AdminAPIs;
import com.jim.framework.tbox.adminservice.api.dto.RouteDTO;
import com.jim.framework.tbox.common.TboxResponse;
import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.common.exception.ParamValidationException;
import com.jim.framework.tbox.common.exception.RestApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/envs/{env}/admin/rules/route")
public class RoutesController {

    public static final Logger logger = LoggerFactory.getLogger(OverridesController.class);
    @Autowired
    private RestApiHandler restApiHandler;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createRule(@RequestBody RouteDTO routeDTO, @PathVariable String env) {
        String serviceName = routeDTO.getService();
        String app = routeDTO.getApp();
        if (StringUtils.isEmpty(serviceName) && StringUtils.isEmpty(app)) {
            throw new ParamValidationException("serviceName and app is Empty!");
        }
        restApiHandler.handle(AdminAPIs.CREATE_RULE, env, null, routeDTO);
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean updateRule(@PathVariable String id, @RequestBody RouteDTO routeDTO, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.UPDATE_OVERRIDE, env, new String[]{id}, routeDTO);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public TboxResponse<RouteDTO[]> searchRoutes(@RequestParam(required = false) String app,
                                                    @RequestParam(required = false) String service, @PathVariable String env) {

        RouteDTO[] result = restApiHandler.handle(AdminAPIs.SEARCH_ROUTES, env, new String[]{app, service}, RouteDTO[].class);
        return TboxResponse.success(result);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TboxResponse<RouteDTO> detailRoute(@PathVariable String id, @PathVariable String env) {
        RouteDTO result = restApiHandler.handle(AdminAPIs.DETAIL_ROUTE, env, new String[]{id}, RouteDTO.class);
        return TboxResponse.success(result);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteRoute(@PathVariable String id, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.DELETE_ROUTE, env, new String[]{id});
        return true;
    }

    @RequestMapping(value = "/enable/{id}", method = RequestMethod.PUT)
    public boolean enableRoute(@PathVariable String id, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.ENABLE_ROUTE, env, new String[]{id});
        return true;
    }

    @RequestMapping(value = "/disable/{id}", method = RequestMethod.PUT)
    public boolean disableRoute(@PathVariable String id, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.DISABLE_ROUTE, env, new String[]{id});
        return true;
    }
}