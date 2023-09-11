package com.oguzhan.bektas.gateway.config;

import com.oguzhan.bektas.gateway.dto.RoutePropertiesDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "gateway.cloud")
public class RouteInfoProperty {

    private List<RoutePropertiesDto> routes;

    public List<RoutePropertiesDto> getRoutes() {

        return routes;
    }

    public void setRoutes(List<RoutePropertiesDto> routes) {
        this.routes = routes;
    }
}