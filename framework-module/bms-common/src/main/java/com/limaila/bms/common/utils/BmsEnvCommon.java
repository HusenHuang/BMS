package com.limaila.bms.common.utils;

import java.util.Optional;

/***
 说明: 
 @author MrHuang
 @date 2020/9/2 20:07
 @desc
 ***/
public class BmsEnvCommon {

    /**
     * K8S NODE NAME
     */
    private final static String ENV_NODE_NAME = "NODE_NAME";

    /**
     * K8S POD IP
     */
    private final static String ENV_POD_IP = "POD_IP";

    /**
     * 环境
     */
    private final static String ENV_SERVER_ENV = "SERVER_ENV";

    private BmsEnvCommon() {

    }

    public static String getNodeName() {
        return Optional.ofNullable(System.getenv(ENV_NODE_NAME)).orElse("N/A");
    }

    public static String getPodIp() {
        return Optional.ofNullable(System.getenv(ENV_POD_IP)).orElse("N/A");
    }

    public static String getServerEnv() {
        return Optional.ofNullable(System.getenv(ENV_SERVER_ENV)).orElse("N/A");
    }




}
