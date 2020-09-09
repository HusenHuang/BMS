package com.limaila.bms.gateway.route;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/***
 说明: 
 @author MrHuang
 @date 2020/9/9 21:02
 @desc
 ***/
@Slf4j
@Component
public class GatewayRouteRefresher implements ApplicationContextAware, ApplicationEventPublisherAware {

    private GatewayProperties gatewayProperties;

    private ApplicationEventPublisher applicationEventPublisher;

    private ApplicationContext applicationContext;

    private RouteDefinitionWriter routeDefinitionWriter;

    public GatewayRouteRefresher(GatewayProperties gatewayProperties, RouteDefinitionWriter routeDefinitionWriter) {
        this.gatewayProperties = gatewayProperties;
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    /**
     * 监听路由修改
     *
     * @param changeEvent
     */
    @ApolloConfigChangeListener(value = "application-route.yaml")
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean gatewayPropertiesChanged = false;
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("spring.cloud.gateway")) {
                gatewayPropertiesChanged = true;
                break;
            }
        }

        if (gatewayPropertiesChanged) {
            GatewayPropertiesRefresher(changeEvent);
        }
    }

    /**
     * 刷新路由信息
     *
     * @param changeEvent
     */
    private void GatewayPropertiesRefresher(ConfigChangeEvent changeEvent) {
        log.info("GatewayRouteRefresher gateway properties running !");
        //更新配置
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        //更新路由
        gatewayProperties.getRoutes().forEach(definition -> {
            // 删除旧的路由
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
            // 新增新的路由
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        });
        log.info("GatewayRouteRefresher gateway properties success !");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
