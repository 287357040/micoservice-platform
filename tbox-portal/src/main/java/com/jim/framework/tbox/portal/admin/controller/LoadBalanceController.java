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

import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.common.dto.admin.BalancingDTO;
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
        try {
            restApiHandler.handle("admin.createLoadbalance", env, null, balancingDTO);
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean updateLoadbalance(@PathVariable String id, @RequestBody BalancingDTO balancingDTO, @PathVariable String env) throws ParamValidationException {
        try {
            restApiHandler.handle("admin.updateLoadbalance", env, new String[]{id}, balancingDTO);
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BalancingDTO> searchLoadbalances(@RequestParam(required = false) String service, @PathVariable String env) {
        try {
            BalancingDTO[] balancingDTOS = restApiHandler.handle("admin.searchLoadbalances", env, new String[]{service}, BalancingDTO[].class);
            return Arrays.asList(balancingDTOS);
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BalancingDTO detailLoadBalance(@PathVariable String id, @PathVariable String env) throws ParamValidationException {
        try {
            BalancingDTO balancingDTO = restApiHandler.handle("admin.detailLoadBalance", env, new String[]{id}, BalancingDTO.class);
            return balancingDTO;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteLoadBalance(@PathVariable String id, @PathVariable String env) {
        if (id == null) {
            throw new IllegalArgumentException("Argument of id is null!");
        }
        try {
            restApiHandler.handle("admin.deleteLoadBalance", env, new String[]{id});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
