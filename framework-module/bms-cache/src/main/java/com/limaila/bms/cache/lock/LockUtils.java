package com.limaila.bms.cache.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RFuture;
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
     * @param <T>      泛型
     * @return
     */
    public static <T> T runWithLock(String key, LockCallback<T> callback) {
        RLock lock = redissonClient.getLock(key);
        lock.lock();
        try {
            return callback.callback();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 公平锁
     * 基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。
     *  同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     *  所有请求线程会在一个队列中排队,当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
     * 分布式锁执行逻辑
     * 如果获取不到锁会一直等待
     * @param key
     * @param callback
     * @param <T>
     * @return
     */
    public static <T> T runWithFairLock(String key, LockCallback<T> callback) {
        RLock lock = redissonClient.getFairLock(key);
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
    public static <T> T runWithTryLock(String key, long waitTimeMillis, LockCallback<T> callback) throws LockException {
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
