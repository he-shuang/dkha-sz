package com.dkha.commons.security.constant;

/**
 * 资源常量
 */
public class ResourceConstant {
    /**
     * 不进行认证的URL
     */
    public static final String [] IGNORING_URLS = {
        "/actuator/**",
        "/v2/api-docs",
        "/webjars/**",
        "/swagger/**",
        "/swagger-resources/**",
        "/fvscdevice/faceRegister",
        "/fvscdevice/faceOpenLog",
        "/fvscdevice/**",
        "/doc.html",
            "/ws/**"
    };

}
