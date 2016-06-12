package com.igitras.gateway.mvc.dto;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author mason
 */
public class RouteDto {
    private String path;
    private String serviceId;
    private List<ServiceInstance> serviceInstances;

    public String getPath() {
        return path;
    }

    public RouteDto setPath(String path) {
        this.path = path;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public RouteDto setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public List<ServiceInstance> getServiceInstances() {
        return serviceInstances;
    }

    public RouteDto setServiceInstances(List<ServiceInstance> serviceInstances) {
        this.serviceInstances = serviceInstances;
        return this;
    }
}
