package com.limaila.bms.cache.lock;

/***
 说明: 
 @author MrHuang
 @date 2020/9/29 14:51
 @desc
 ***/
@FunctionalInterface
public interface LockCallback<T> {

    /**
     * callback函数
     *
     * @return
     */
    T callback();
}
