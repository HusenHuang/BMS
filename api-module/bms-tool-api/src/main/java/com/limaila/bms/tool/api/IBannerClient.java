package com.limaila.bms.tool.api;

import com.limaila.bms.common.utils.BmsProjectCommon;
import org.springframework.cloud.openfeign.FeignClient;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:24
 @desc
 ***/
@FeignClient(name = BmsProjectCommon.BMS_TOOL_SERVICE, fallbackFactory = BannerClientFallbackFactory.class)
public interface IBannerClient extends IBannerApi {


}
