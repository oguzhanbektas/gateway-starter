package com.oguzhan.bektas.gateway.config;

import com.oguzhan.bektas.gateway.dto.ApplicationPropertiesDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "debug.cloud")
public class RouteInfoPropertyForDebug {

    private List<ApplicationPropertiesDto> applications;

    public List<ApplicationPropertiesDto> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationPropertiesDto> applications) {
        this.applications = applications;
    }
}