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
import com.jim.framework.tbox.adminservice.api.dto.BalancingDTO;
import com.jim.framework.tbox.common.TboxResponse;
import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.common.exception.ParamValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/envs/{env}/admin/rules/balancing")
public class LoadBalanceController {
    public static final Logger logger = LoggerFactory.getLogger(LoadBalanceController.class);
    @Autowired
    private RestApiHandler restApiHandler;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createLoadbalance(@RequestBody BalancingDTO balancingDTO, @PathVariable String env) throws ParamValidationException {
        String serviceName = balancingDTO.getService();
        if (StringUtils.isEmpty(serviceName)) {
            throw new ParamValidationException("serviceName is Empty!");
        }
        restApiHandler.handle(AdminAPIs.CREATE_LOADBALANCE, env, null, balancingDTO);
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean updateLoadbalance(@PathVariable String id, @RequestBody BalancingDTO balancingDTO, @PathVariable String env) throws ParamValidationException {
        restApiHandler.handle(AdminAPIs.UPDATE_LOADBALANCE, env, new String[]{id}, balancingDTO);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public TboxResponse<BalancingDTO[]> searchLoadbalances(@RequestParam(required = false) String service, @PathVariable String env) {
        BalancingDTO[] balancingDTOS = restApiHandler.handle(AdminAPIs.SEARCH_LOADBALANCES, env, new String[]{service}, BalancingDTO[].class);
        return TboxResponse.success(balancingDTOS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TboxResponse<BalancingDTO> detailLoadBalance(@PathVariable String id, @PathVariable String env) throws ParamValidationException {
        BalancingDTO balancingDTO = restApiHandler.handle(AdminAPIs.DETAIL_LOADBALANCE, env, new String[]{id}, BalancingDTO.class);
        return TboxResponse.success(balancingDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteLoadBalance(@PathVariable String id, @PathVariable String env) {
        if (id == null) {
            throw new ParamValidationException("Argument of id is null!");
        }
        restApiHandler.handle(AdminAPIs.DELETE_LOADBALANCE, env, new String[]{id});
        return true;
    }

}
