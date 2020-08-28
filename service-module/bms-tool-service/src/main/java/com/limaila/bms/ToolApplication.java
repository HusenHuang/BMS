package com.limaila.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/***
 说明: 
 @author MrHuang
 @date 2020/8/28 14:11
 @desc
 ***/
@SpringBootApplication
@EnableDiscoveryClient
public class ToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class, args);
    }
}
