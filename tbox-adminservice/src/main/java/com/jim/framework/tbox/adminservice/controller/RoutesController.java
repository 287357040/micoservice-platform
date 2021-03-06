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

import com.jim.framework.tbox.adminservice.api.domain.Route;
import com.jim.framework.tbox.adminservice.api.dto.RouteDTO;
import com.jim.framework.tbox.common.exception.ParamValidationException;
import com.jim.framework.tbox.common.exception.ResourceNotFoundException;
import com.jim.framework.tbox.adminservice.service.RouteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/rules/route")
public class RoutesController {

    private final RouteService routeService;

    @Autowired
    public RoutesController(RouteService routeService) {
        this.routeService = routeService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public boolean createRule(@RequestBody RouteDTO routeDTO) {
        String serviceName = routeDTO.getService();
        String app = routeDTO.getApp();
        if (StringUtils.isEmpty(serviceName) && StringUtils.isEmpty(app)) {
            throw new ParamValidationException("serviceName and app is Empty!");
        }
        if (StringUtils.isNotEmpty(serviceName)) {
            //2.6
            routeDTO.setService(serviceName);

            Route route = convertRouteDTOtoRoute(routeDTO, null);
            routeService.createRoute(route);

        } else {
            //new feature in 2.7
        }
        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean updateRule(@PathVariable String id, @RequestBody RouteDTO routeDTO) {
        Route route = routeService.findRoute(id);
        if (route == null) {
            throw new ResourceNotFoundException("Unknown ID!");
        }
        routeDTO.setService(route.getService());
        Route newRoute = convertRouteDTOtoRoute(routeDTO, id);
        routeService.updateRoute(newRoute);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RouteDTO> searchRoutes(@RequestParam(required = false) String app,
                                       @RequestParam(required = false) String service) {
        List<Route> routes;
        if (StringUtils.isNotEmpty(app)) {
            // app scope in 2.7
        }
        if (StringUtils.isNotEmpty(service)) {
            routes = routeService.findByService(service);
        } else {
            routes = routeService.findAll();
        }
        List<RouteDTO> routeDTOS = new ArrayList<>();
        for (Route route : routes) {
            RouteDTO routeDTO = convertRoutetoRouteDTO(route, route.getHash());
            routeDTOS.add(routeDTO);
        }
        return routeDTOS;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RouteDTO detailRoute(@PathVariable String id) {
        Route route = routeService.findRoute(id);
        if (route == null) {
            throw new ResourceNotFoundException("Unknown ID!");
        }
        RouteDTO routeDTO = convertRoutetoRouteDTO(route, id);
        return routeDTO;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
        return true;
    }

    @RequestMapping(value = "/enable/{id}", method = RequestMethod.PUT)
    public boolean enableRoute(@PathVariable String id) {

        routeService.enableRoute(id);
        return true;
    }

    @RequestMapping(value = "/disable/{id}", method = RequestMethod.PUT)
    public boolean disableRoute(@PathVariable String id) {
        routeService.disableRoute(id);
        return true;
    }

    private String parseCondition(String[] conditions) {
        StringBuilder when = new StringBuilder();
        StringBuilder then = new StringBuilder();
        for (String condition : conditions) {
            condition = condition.trim();
            if (condition.contains("=>")) {
                String[] array = condition.split("=>", 2);
                String consumer = array[0].trim();
                String provider = array[1].trim();
                if (consumer != "") {
                    if (when.length() != 0) {
                        when.append(" & ").append(consumer);
                    } else {
                        when.append(consumer);
                    }
                }
                if (provider != "") {
                    if (then.length() != 0) {
                        then.append(" & ").append(provider);
                    } else {
                        then.append(provider);
                    }
                }
            }
        }
        return (when.append(" => ").append(then)).toString();
    }

    private Route convertRouteDTOtoRoute(RouteDTO routeDTO, String id) {
        Route route = new Route();
        String[] conditions = routeDTO.getConditions();
        String rule = parseCondition(conditions);
        route.setService(routeDTO.getService());
        route.setEnabled(routeDTO.isEnabled());
        route.setForce(routeDTO.isForce());
        route.setDynamic(routeDTO.isDynamic());
        route.setRuntime(routeDTO.isRuntime());
        route.setPriority(routeDTO.getPriority());
        route.setRule(rule);
        if (id != null) {
            route.setHash(id);
        }
        return route;
    }

    private RouteDTO convertRoutetoRouteDTO(Route route, String id) {
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setDynamic(route.isDynamic());
        routeDTO.setConditions(new String[]{route.getRule()});
        routeDTO.setEnabled(route.isEnabled());
        routeDTO.setForce(route.isForce());
        routeDTO.setPriority(route.getPriority());
        routeDTO.setRuntime(route.isRuntime());
        routeDTO.setService(route.getService());
        if (id != null) {
            routeDTO.setId(route.getHash());
        }
        return routeDTO;
    }
}