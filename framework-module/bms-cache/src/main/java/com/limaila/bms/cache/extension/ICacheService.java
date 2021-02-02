package com.limaila.bms.cache.extension;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;


public interface ICacheService<T extends PrimaryId> {

    T add(T entity);

    @Cacheable(key = "'id_' + #id")
    T getById(Serializable id);

    @CacheEvict(key = "'id_' + #entity.id")
    T updateById(T entity);

    @CacheEvict(key = "'id_' + #id")
    boolean deleteById(Serializable id);
}
