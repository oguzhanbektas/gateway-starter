package com.oguzhan.bektas.gateway.config;


import com.oguzhan.bektas.gateway.dto.RouteDto;
import com.oguzhan.bektas.gateway.dto.RoutePropertiesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

import java.io.IOException;
import java.util.UUID;

import static com.oguzhan.bektas.gateway.util.RouteUtil.getRouteDetail;
import static com.oguzhan.bektas.gateway.util.RouteUtil.getRoutesFromEnvironment;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.cloud.gateway.enabled", matchIfMissing = true)
@EnableConfigurationProperties
@AutoConfigureBefore({ HttpHandlerAutoConfiguration.class, WebFluxAutoConfiguration.class })
@AutoConfigureAfter({ GatewayReactiveLoadBalancerClientAutoConfiguration.class,
        GatewayClassPathWarningAutoConfiguration.class })
@ConditionalOnClass(DispatcherHandler.class)
public class GatewayRouteConfiguration {

    @Value("${debug.enabled}")
    private Boolean debugEnable = false;
/*
    @Value("${gateway.routes-overriding-active}")
    private Boolean routeOverrideActive = false;


    @Value("${gateway.routes-override-fileb64}")
    private String routesOverrideFile = "na";

    @Value("${gateway.routes-overriding2-active}")
    private Boolean routeOverride2Active = false;

    @Value("${gateway.routes-override2-fileb64}")
    private String routesOverrideFile2 = "na";
*/
    @Value("${debug.maingateway}")
    private String mainGateway;

    private final RouteInfoProperty property;

    @Autowired
    public GatewayRouteConfiguration(RouteInfoProperty property) {
        this.property = property;
    }

    @Bean
    public RouteLocator defaultRoutes(RouteLocatorBuilder builder) throws IOException {

        RouteLocatorBuilder.Builder routesBuilder = null;
        routesBuilder = builder.routes();
/*
        if (routeOverrideActive) {
            List<RoutePropertiesDto> routesFromEnvironment = getRoutesFromEnvironment(routesOverrideFile);
            if (routesFromEnvironment == null) {
                System.out.println("Environment file can not be bind check json format!");
            } else {
                if (routeOverride2Active) {
                    List<RoutePropertiesDto> routesFromEnvironment2 = getRoutesFromEnvironment(routesOverrideFile2);
                    if (routesFromEnvironment2 == null) {
                        System.out.println("Environment file2 can not be bind check json format!");
                    } else {
                        routesFromEnvironment.addAll(routesFromEnvironment2);
                    }
                }
                property.setRoutes(routesFromEnvironment);
            }
        }
  */
        for (RoutePropertiesDto application : property.getRoutes()) {

            RouteDto routeDetail = getRouteDetail(application, false);
            if (routeDetail == null) {
                continue;
            }

            //Degistirmeyin! debug aktif ise debug edilmeyen proje maine gitmelidir!
            if (debugEnable) {
                routeDetail.setUri(mainGateway);
            } else {
                if (routeDetail.getRewriteRegex().equals("/(?<segment>.*)")) {
                    routeDetail.setRewriteRegex("/" + routeDetail.getId() + routeDetail.getRewriteRegex());
                }
                routeDetail.setUri(application.getUri());
            }


            routesBuilder.route(UUID.randomUUID().toString(), predicateSpec -> setPredicateSpec(predicateSpec, routeDetail));


            //    if (!routeDetail.getHost1().isEmpty() && !routeDetail.getHost2().isEmpty() && !routeDetail.getHost3().isEmpty()) {
            //        routesBuilder.route(p -> p.order(routeDetail.getOrder())
            //                .host(routeDetail.getHost1(), routeDetail.getHost2(), routeDetail.getHost3())
            //                .filters(f -> f.rewritePath(routeDetail.getRewriteRegex(), routeDetail.getRewriteReplace()))
            //                .uri(routeDetail.getUri()));
            //    } else {
            //        routesBuilder.route(p -> p.order(routeDetail.getOrder())
            //                .path(routeDetail.getPath())
            //                .filters(f -> f.rewritePath(routeDetail.getRewriteRegex(), routeDetail.getRewriteReplace()))
            //                .uri(routeDetail.getUri()));
            //    }


        }

        return routesBuilder.build();
    }


    private Buildable<Route> setPredicateSpec(PredicateSpec predicateSpec, RouteDto routeDetail) {

        BooleanSpec booleanSpec = predicateSpec.path(routeDetail.getPath());

        booleanSpec.and().order(routeDetail.getOrder());

        if (!routeDetail.getHost1().isEmpty() && !routeDetail.getHost2().isEmpty() && !routeDetail.getHost3().isEmpty()) {
            booleanSpec.and().host(routeDetail.getHost1(), routeDetail.getHost2(), routeDetail.getHost3());
        }
        if (!routeDetail.getRemoteAddr().isEmpty() || !routeDetail.getRemoteAddr2().isEmpty()) {
            booleanSpec.and().remoteAddr(routeDetail.getRemoteAddr(), routeDetail.getRemoteAddr2());
        }

        booleanSpec.filters(f -> f.rewritePath(routeDetail.getRewriteRegex(), routeDetail.getRewriteReplace()));

        return booleanSpec.uri(routeDetail.getUri());

    }


}