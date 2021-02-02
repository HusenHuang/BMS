package com.limaila.bms.tool.service;

import com.limaila.bms.cache.extension.ICacheService;
import com.limaila.bms.tool.bean.BannerEntity;
import org.springframework.cache.annotation.CacheConfig;


/**
 * CacheConfig 中的cacheNames是默认的value值
 */

@CacheConfig(cacheNames = {"banner3"})
public interface IBannerService extends ICacheService<BannerEntity> {

}
