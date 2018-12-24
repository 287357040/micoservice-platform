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
import com.jim.framework.tbox.common.dto.admin.OverrideDTO;
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
@RequestMapping("/envs/{env}/admin/rules/override")
public class OverridesController {

    public static final Logger logger = LoggerFactory.getLogger(OverridesController.class);
    @Autowired
    private RestApiHandler restApiHandler;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createOverride(@RequestBody OverrideDTO overrideDTO, @PathVariable String env) {
        String serviceName = overrideDTO.getService();
        if (StringUtils.isEmpty(serviceName)) {
            throw new ParamValidationException("serviceName is Empty!");
        }
        restApiHandler.handle("admin.createOverride", env, null, overrideDTO);
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean updateOverride(@PathVariable String id, @RequestBody OverrideDTO overrideDTO, @PathVariable String env) {
        restApiHandler.handle("admin.updateOverride", env, new String[]{id}, overrideDTO);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OverrideDTO> searchOverride(@RequestParam(required = false) String service, @PathVariable String env) {
        OverrideDTO[] result = restApiHandler.handle("admin.searchOverride", env, new String[]{service}, OverrideDTO[].class);
        return Arrays.asList(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public OverrideDTO detailOverride(@PathVariable String id, @PathVariable String env) {
        OverrideDTO result = restApiHandler.handle("admin.detailOverride", env, new String[]{id}, OverrideDTO.class);
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteOverride(@PathVariable String id, @PathVariable String env) {
        try {
            restApiHandler.handle("admin.deleteOverride", env, new String[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/enable/{id}", method = RequestMethod.PUT)
    public boolean enableRoute(@PathVariable String id, @PathVariable String env) {
        try {
            restApiHandler.handle("admin.enableRoute", env, new String[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/disable/{id}", method = RequestMethod.PUT)
    public boolean disableRoute(@PathVariable String id, @PathVariable String env) {
        try {
            restApiHandler.handle("admin.disableRoute", env, new String[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
