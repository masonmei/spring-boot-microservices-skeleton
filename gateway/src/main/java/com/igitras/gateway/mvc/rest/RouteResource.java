package com.igitras.gateway.mvc.rest;

import com.codahale.metrics.annotation.Timed;
import com.igitras.gateway.mvc.dto.RouteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Router Resources.
 *
 * @author mason
 */
@RestController
@RequestMapping("api/gateway/routes")
public class RouteResource {
    private static final Logger LOG = LoggerFactory.getLogger(RouteResource.class);

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public List<RouteDto> activeRoutes() {
        List<Route> routes = routeLocator.getRoutes();
        return routes.stream()
                .map(route -> new RouteDto().setPath(route.getFullPath())
                        .setServiceId(route.getId())
                        .setServiceInstances(discoveryClient.getInstances(route.getId())))
                .collect(Collectors.toList());
    }
}
