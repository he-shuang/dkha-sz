
package com.dkha.route;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Nacos动态路由支持
 */
@Slf4j
@Component
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository{
    private ApplicationEventPublisher publisher;
    private NacosConfigProperties nacosConfigProperties;
    private NacosConfigManager nacosConfigManager;
    /**
     * Nacos DATA_ID
     */
    private static final String DATA_ID = "gateway.json";

    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher, NacosConfigProperties nacosConfigProperties) {
        this.publisher = publisher;
        this.nacosConfigProperties = nacosConfigProperties;
        this.nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
        nacosListener();
    }

    /**
     * Nacos监听器
     */
    private void nacosListener() {
        try {
            nacosConfigManager.getConfigService().addListener(DATA_ID, nacosConfigProperties.getGroup(), new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("nacosListener error", e);
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String routeConfig = nacosConfigManager.getConfigService()
                    .getConfig(DATA_ID, nacosConfigProperties.getGroup(),3000);

            List<RouteDefinition> routeDefinitionList = new ArrayList<>();
            if(StringUtils.isNotBlank(routeConfig)){
                routeDefinitionList = JSON.parseArray(routeConfig, RouteDefinition.class);
            }

            return Flux.fromIterable(routeDefinitionList);
        } catch (NacosException e) {
            log.error("getRouteDefinitions error ", e);
        }

        return Flux.fromIterable(CollUtil.newArrayList());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
