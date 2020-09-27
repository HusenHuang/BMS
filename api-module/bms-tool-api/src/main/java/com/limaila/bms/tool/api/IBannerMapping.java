package com.limaila.bms.tool.api;

import com.limaila.bms.common.response.RestRSP;
import com.limaila.bms.tool.bean.Banner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:59
 @desc
 ***/
@RequestMapping("/api/banner")
public interface IBannerMapping {

    /**
     * 获取Banner列表
     *
     * @return
     */
    @GetMapping("/getBannerList")
    RestRSP<List<Banner>> getBannerList();
}
