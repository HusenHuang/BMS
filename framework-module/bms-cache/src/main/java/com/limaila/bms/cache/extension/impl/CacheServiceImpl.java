package com.limaila.bms.cache.extension.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaila.bms.cache.extension.ICacheService;
import com.limaila.bms.cache.extension.id.PrimaryId;

import java.io.Serializable;

public abstract class CacheServiceImpl<T extends PrimaryId> implements ICacheService<T> {

    public abstract BaseMapper<T> getBaseMapper();

    public abstract String cacheKey();

    @Override
    public T getById(Serializable id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public T add(T entity) {
        getBaseMapper().insert(entity);
        return entity;
    }

    @Override
    public T updateById(T entity) {
        getBaseMapper().updateById(entity);
        return entity;
    }

    @Override
    public boolean deleteById(Serializable id) {
        return getBaseMapper().deleteById(id) > 0;
    }
}
