package com.dkha.commons.xxl.job.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.dkha.commons.xxl.job.properties.XxlJobProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Slf4j
@Configuration
public class XxlJobConfig {
    @Autowired
    private XxlJobProperties properties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(properties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppName(properties.getExecutor().getAppName());
        xxlJobSpringExecutor.setIp(properties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(properties.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(properties.getExecutor().getAccessToken());
        xxlJobSpringExecutor.setLogPath(properties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(properties.getExecutor().getLogRetentionDays());

        return xxlJobSpringExecutor;
    }
}
