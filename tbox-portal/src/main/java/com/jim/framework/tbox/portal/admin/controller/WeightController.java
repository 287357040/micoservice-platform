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
import com.jim.framework.tbox.common.api.RestRequest;
import com.jim.framework.tbox.common.dto.admin.ServiceDTO;
import com.jim.framework.tbox.common.dto.admin.WeightDTO;
import com.jim.framework.tbox.foundation.RestRequestProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * dubbo admin在创建override，route后，再读取时会根据返回URL计算MD5值，来作为唯一ID号，显示给前端
 * appName即是appId
 */
@RestController
@RequestMapping("/envs/{env}/admin/rules/weight")
public class WeightController {

    @Autowired
    private RestApiHandler restApiHandler;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createWeight(@RequestBody WeightDTO weightDTO, @PathVariable String env) {
        restApiHandler.handle("admin.createWeight",env,null,weightDTO);
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean updateWeight(@PathVariable String id, @RequestBody WeightDTO weightDTO, @PathVariable String env) {
        restApiHandler.handle("admin.updateWeight",env,new String[]{id}, weightDTO);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<WeightDTO> searchWeight(@RequestParam(required = false) String service, @PathVariable String env) {
        WeightDTO[] result = restApiHandler.handle("admin.searchWeight",env,new String[]{service}, WeightDTO[].class);
        return Arrays.asList(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public WeightDTO detailWeight(@PathVariable String id, @PathVariable String env) {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteWeight(@PathVariable String id, @PathVariable String env) {
        return false;
    }
}
