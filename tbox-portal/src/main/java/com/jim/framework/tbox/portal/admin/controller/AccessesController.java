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

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.jim.framework.tbox.adminservice.api.AdminAPIs;
import com.jim.framework.tbox.adminservice.api.dto.AccessDTO;
import com.jim.framework.tbox.common.TboxResponse;
import com.jim.framework.tbox.common.api.RestApiHandler;
import com.jim.framework.tbox.portal.util.RequestArgumentsAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/envs/{env}/admin/rules/access")
public class AccessesController {
    private static final Logger logger = LoggerFactory.getLogger(AccessesController.class);

    @Autowired
    private RestApiHandler restApiHandler;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public TboxResponse<AccessDTO[]> searchAccess(@RequestParam(required = false) String service, @PathVariable String env) {
        AccessDTO[] accessDTOS = restApiHandler.handle(AdminAPIs.SEARCH_ACCESS, env, new String[]{service}, AccessDTO[].class);
        return TboxResponse.success(accessDTOS);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TboxResponse<AccessDTO> detailAccess(@PathVariable String id, @PathVariable String env) {
        AccessDTO accessDTO = restApiHandler.handle(AdminAPIs.DETAIL_ACCESS, env, new String[]{id}, AccessDTO.class);
        return TboxResponse.success(accessDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public TboxResponse deleteAccess(@PathVariable String id, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.DELETE_ACCESS, env, new String[]{id});
        return TboxResponse.success(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TboxResponse createAccess(@RequestBody AccessDTO accessDTO, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.CREATE_ACCESS, env, null, accessDTO);
        return TboxResponse.success(null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TboxResponse updateAccess(@PathVariable String id, @RequestBody AccessDTO accessDTO, @PathVariable String env) {
        restApiHandler.handle(AdminAPIs.UPDATE_ACCESS, env, new String[]{id}, accessDTO);
        return TboxResponse.success(null);
    }
}
