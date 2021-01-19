package com.limaila.bms;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/***
 说明: 
 @author MrHuang
 @date 2020/9/9 20:33
 @desc
 ***/
@SpringBootApplication
@EnableDiscoveryClient
@EnableApolloConfig
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
