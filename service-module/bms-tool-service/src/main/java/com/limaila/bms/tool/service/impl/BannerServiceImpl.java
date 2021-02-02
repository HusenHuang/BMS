package com.limaila.bms.tool.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limaila.bms.cache.extension.impl.CacheServiceImpl;
import com.limaila.bms.tool.bean.BannerEntity;
import com.limaila.bms.tool.mapper.BannerMapper;
import com.limaila.bms.tool.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl extends CacheServiceImpl<BannerEntity> implements IBannerService {

    @Autowired
    private BannerMapper mapper;

    @Override
    public BaseMapper<BannerEntity> getBaseMapper() {
        return mapper;
    }

    @Override
    public String cacheKey() {
        return "banner";
    }
}
