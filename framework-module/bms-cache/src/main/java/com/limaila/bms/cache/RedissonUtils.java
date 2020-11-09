package com.limaila.bms.cache;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 说明: 
 @author MrHuang
 @date 2020/11/4 15:56
 @desc
 ***/
@Component
@Slf4j
public class RedissonUtils {

    private RedissonUtils() {
    }

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        RedissonUtils.redissonClient = redissonClient;
    }

    public static <K, V> RMap<K, V> getMap(String name) {
        return redissonClient.getMap(name);
    }

    public static <K, V> RMapCache<K, V> getMapCache(String name) {
        return redissonClient.getMapCache(name);
    }

    public static <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String name, LocalCachedMapOptions<K, V> options) {
        return redissonClient.getLocalCachedMap(name, options);
    }

}
