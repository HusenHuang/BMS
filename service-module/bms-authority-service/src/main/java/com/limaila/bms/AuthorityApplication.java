package com.limaila.bms;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/***
 说明: 
 @author MrHuang
 @date 2020/8/28 18:21
 @desc
 ***/
@SpringBootApplication
@EnableDiscoveryClient
@EnableApolloConfig
@EnableFeignClients
public class AuthorityApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorityApplication.class, args);
    }
}
