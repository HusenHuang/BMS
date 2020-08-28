package com.limaila.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/***
 说明: 
 @author MrHuang
 @date 2020/8/28 18:21
 @desc
 ***/
@SpringBootApplication
@EnableDiscoveryClient
public class AuthorityApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorityApplication.class, args);
    }
}
