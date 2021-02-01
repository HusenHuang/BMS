package com.limaila.bms.tool.service;

import com.limaila.bms.tool.bean.BannerEntity;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;


/**
 * CacheConfig 中的cacheNames是默认的value值
 */
@CacheConfig(cacheNames = {"banner"})
public interface IBannerService {

    BannerEntity add(BannerEntity entity);

    @CacheEvict(key = "'_id_' + #id")
    boolean deleteByPrimaryKey(Serializable id);

    @CacheEvict(key = "'_id_' + #entity.id")
    BannerEntity updateByPrimaryKey(BannerEntity entity);

    @Cacheable(key = "'_id_' + #id")
    BannerEntity getByPrimaryKey(Serializable id);

    @Cacheable(key = "'_name_' + #name")
    BannerEntity getByName(String name);
}
