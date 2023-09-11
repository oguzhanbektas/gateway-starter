package com.oguzhan.bektas.gateway.config;


import com.oguzhan.bektas.gateway.dto.ApplicationPropertiesDto;
import com.oguzhan.bektas.gateway.dto.RouteDto;
import com.oguzhan.bektas.gateway.dto.RoutePropertiesDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.oguzhan.bektas.gateway.util.RouteUtil.getRouteDetail;


@Configuration
@ConditionalOnProperty(
        value = "debug.enabled",
        havingValue = "true")
public class GatewayRouteConfigurationDebug {

    private final RouteInfoPropertyForDebug property;
    private final RouteInfoProperty routes;

    public GatewayRouteConfigurationDebug(RouteInfoPropertyForDebug applications, RouteInfoProperty routes) {
        this.property = applications;
        this.routes = routes;
    }

    @Bean
    public RouteLocator debugRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder route = builder.routes();
        for (ApplicationPropertiesDto application : property.getApplications()) {

            List<RoutePropertiesDto> routePropertiesDtoStream = routes.getRoutes().stream()
                    .filter(item -> item.getId().equalsIgnoreCase(application.getId()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(routePropertiesDtoStream)){
                continue;
            }

            RoutePropertiesDto routeProperties = routePropertiesDtoStream.get(0);

            RouteDto routeDetail = getRouteDetail(routeProperties, true);

            if (routeDetail == null) {
                continue;
            }

            routeDetail.setOrder(1);
            routeDetail.setRewriteRegex("/" + routeDetail.getId() + routeDetail.getRewriteRegex());
            route = route.route(p -> p.order(routeDetail.getOrder())
                    .path(routeDetail.getPath())
                    .filters(f -> f.rewritePath(routeDetail.getRewriteRegex(), routeDetail.getRewriteReplace()))
                    .uri(application.getUri()));
        }

        if (route == null) {
            return null;
        }

        return route.build();
    }
}