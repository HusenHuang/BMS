package com.limaila.bms.tool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.limaila.bms.tool.bean.BannerEntity;
import com.limaila.bms.tool.mapper.BannerMapper;
import com.limaila.bms.tool.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class BannerServiceImpl implements IBannerService {

    @Autowired
    private BannerMapper mapper;

    @Override
    public BannerEntity add(BannerEntity entity) {
        mapper.insert(entity);
        return entity;
    }

    @Override
    public boolean deleteByPrimaryKey(Serializable id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public BannerEntity updateByPrimaryKey(BannerEntity entity) {
         mapper.updateById(entity);
         return entity;
    }

    @Override
    public BannerEntity getByPrimaryKey(Serializable id) {
        return mapper.selectById(id);
    }

    @Override
    public BannerEntity getByName(String name) {
        QueryWrapper<BannerEntity> wrapper = Wrappers.emptyWrapper();
        return mapper.selectOne(wrapper);
    }
}
