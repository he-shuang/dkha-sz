package com.dkha.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger资源信息
 */
@Data
@Configuration
@ConfigurationProperties("swagger")
public class SwaggerRoute {
    private List<SwaggerRouteProperties> routes;
}
