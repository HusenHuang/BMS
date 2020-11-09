package com.limaila.bms.cache.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/***
 说明: 
 @author MrHuang
 @date 2020/9/29 11:56
 @desc
 ***/
@Component
@Slf4j
public class LockUtils {

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        LockUtils.redissonClient = redissonClient;
    }


    /**
     * 可重入锁
     * 基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
     * 分布式锁执行逻辑
     * 如果获取不到锁会一直等待
     *
     * @param key      锁Key
     * @param callback 回调执行函数
     * @param fair     是否公平锁
     * @param <T>      泛型
     * @return
     */
    public static <T> T toRun(String key, LockCallback<T> callback, boolean fair) {
        RLock lock = fair ? redissonClient.getFairLock(key) : redissonClient.getLock(key);
        lock.lock();
        try {
            return callback.callback();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 分布式锁执行逻辑
     * 如果在waitTimeMillis毫秒数获取不到锁，则不会执行callback
     *
     * @param key      锁Key
     * @param callback 回调执行函数
     * @param <T>      泛型
     * @return
     */
    public static <T> T tryRun(String key, long waitTimeMillis, LockCallback<T> callback) throws LockException {
        boolean res = false;
        RLock lock = redissonClient.getLock(key);
        try {
            res = lock.tryLock(waitTimeMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new LockException("获取分布式锁异常", e);
        }
        if (res) {
            try {
                return callback.callback();
            } finally {
                lock.unlock();
            }
        } else {
            throw new LockException("获取分布式锁失败");
        }
    }

}
