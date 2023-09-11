package com.oguzhan.bektas.gateway.config;


import com.oguzhan.bektas.gateway.dto.RouteDto;
import com.oguzhan.bektas.gateway.dto.RoutePropertiesDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static com.oguzhan.bektas.gateway.util.RouteUtil.getRouteDetail;

@Configuration
public class GatewayRouteConfiguration {

    @Value("${debug.enabled}")
    private Boolean debugEnable = false;

    @Value("${debug.maingateway}")
    private String mainGateway;

    private final RouteInfoProperty property;

    @Autowired
    public GatewayRouteConfiguration(RouteInfoProperty property) {
        this.property = property;
    }

    @Bean
    public RouteLocator defaultRoutes(RouteLocatorBuilder builder) {

        RouteLocatorBuilder.Builder routesBuilder = builder.routes();
        for (RoutePropertiesDto application : property.getRoutes()) {
            RouteDto routeDetail = getRouteDetail(application, false);
            if (routeDetail == null) {
                continue;
            }
            if (debugEnable) {
                routeDetail.setUri(mainGateway);
            } else {
                if (StringUtils.isNotBlank(routeDetail.getRewriteRegex()) &&
                        routeDetail.getRewriteRegex().contains("/(?<segment>.*)")) {
                    routeDetail.setRewriteRegex("/" + routeDetail.getId() + routeDetail.getRewriteRegex());
                }
                routeDetail.setUri(application.getUri());
            }
            routesBuilder.route(UUID.randomUUID().toString(), predicateSpec -> setPredicateSpec(predicateSpec, routeDetail));
        }
        return routesBuilder.build();
    }


    private Buildable<Route> setPredicateSpec(PredicateSpec predicateSpec, RouteDto routeDetail) {
        BooleanSpec booleanSpec = predicateSpec.path(routeDetail.getPath());
        booleanSpec.and().order(routeDetail.getOrder());
        return booleanSpec.uri(routeDetail.getUri());
    }


}