package com.jim.framework.tbox.adminservice.controller;

import com.jim.framework.tbox.adminservice.api.domain.Route;
import com.jim.framework.tbox.adminservice.api.dto.AccessDTO;
import com.jim.framework.tbox.adminservice.service.RouteService;
import com.jim.framework.tbox.adminservice.util.route.RouteRule;
import com.jim.framework.tbox.common.exception.ParamValidationException;
import com.jim.framework.tbox.common.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * Created by celiang.hu on 2018-12-14.
 */
@RestController
@RequestMapping("/admin/rules/access")
public class AccessesController {
    private static final Logger logger =LoggerFactory.getLogger(AccessesController.class);

    private final RouteService routeService;

    @Autowired
    public AccessesController(RouteService routeService) {
        this.routeService = routeService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<AccessDTO> searchAccess(@RequestParam(required = false) String service) throws ParseException {
        List<AccessDTO> result = new ArrayList<>();
        List<Route> routes = new ArrayList<>();
        if (StringUtils.isNotBlank(service)) {
            Route route = routeService.getBlackwhitelistRouteByService(service.trim());
            if (route != null) {
                routes.add(route);
            }
        } else {
            routes = routeService.findAllForceRoute();
        }

        for (Route route : routes) {
            // Match WhiteBlackList Route
            if (route.getName().endsWith(AccessDTO.KEY_BLACK_WHITE_LIST)) {
                AccessDTO accessDTO = new AccessDTO();
                accessDTO.setId(route.getHash());
                accessDTO.setService(route.getService());
                Map<String, RouteRule.MatchPair> when = RouteRule.parseRule(route.getMatchRule());
                for (String key : when.keySet()) {
                    accessDTO.setWhitelist(when.get(key).getUnmatches());
                    accessDTO.setBlacklist(when.get(key).getMatches());
                }
                result.add(accessDTO);
            }
        }
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AccessDTO detailAccess(@PathVariable String id) throws ParseException {
        Route route = routeService.findRoute(id);
        if (route.getName().endsWith(AccessDTO.KEY_BLACK_WHITE_LIST)) {
            AccessDTO accessDTO = new AccessDTO();
            accessDTO.setId(route.getHash());
            accessDTO.setService(route.getService());
            Map<String, RouteRule.MatchPair> when = RouteRule.parseRule(route.getMatchRule());
            for (String key : when.keySet()) {
                accessDTO.setWhitelist(when.get(key).getUnmatches());
                accessDTO.setBlacklist(when.get(key).getMatches());
            }
            return accessDTO;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteAccess(@PathVariable String id) {
        routeService.deleteRoute(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccess(@RequestBody AccessDTO accessDTO) {
        if (StringUtils.isBlank(accessDTO.getService())) {
            throw new ParamValidationException("Service is required.");
        }
        if (accessDTO.getBlacklist() == null && accessDTO.getWhitelist() == null) {
            throw new ParamValidationException("One of Blacklist/Whitelist is required.");
        }

        Route route = routeService.getBlackwhitelistRouteByService(accessDTO.getService());

        if (route != null) {
            throw new ParamValidationException(accessDTO.getService() + " is existed.");
        }

        route = new Route();
        route.setService(accessDTO.getService());
        route.setForce(true);
        route.setName(accessDTO.getService() + " " + AccessDTO.KEY_BLACK_WHITE_LIST);
        route.setFilterRule("false");
        route.setEnabled(true);

        Map<String, RouteRule.MatchPair> when = new HashMap<>();
        RouteRule.MatchPair matchPair = new RouteRule.MatchPair(new HashSet<>(), new HashSet<>());
        when.put(Route.KEY_CONSUMER_HOST, matchPair);

        if (accessDTO.getWhitelist() != null) {
            matchPair.getUnmatches().addAll(accessDTO.getWhitelist());
        }
        if (accessDTO.getBlacklist() != null) {
            matchPair.getMatches().addAll(accessDTO.getBlacklist());
        }

        StringBuilder sb = new StringBuilder();
        RouteRule.contidionToString(sb, when);
        route.setMatchRule(sb.toString());
        routeService.createRoute(route);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateAccess(@PathVariable String id, @RequestBody AccessDTO accessDTO) {
        Route route = routeService.findRoute(id);
        if (Objects.isNull(route)) {
            throw new ResourceNotFoundException("Unknown ID!");
        }
        Map<String, RouteRule.MatchPair> when = new HashMap<>();
        RouteRule.MatchPair matchPair = new RouteRule.MatchPair(new HashSet<>(), new HashSet<>());
        when.put(Route.KEY_CONSUMER_HOST, matchPair);

        if (accessDTO.getWhitelist() != null) {
            matchPair.getUnmatches().addAll(accessDTO.getWhitelist());
        }
        if (accessDTO.getBlacklist() != null) {
            matchPair.getMatches().addAll(accessDTO.getBlacklist());
        }

        StringBuilder sb = new StringBuilder();
        RouteRule.contidionToString(sb, when);
        route.setMatchRule(sb.toString());

        routeService.updateRoute(route);
    }
}
