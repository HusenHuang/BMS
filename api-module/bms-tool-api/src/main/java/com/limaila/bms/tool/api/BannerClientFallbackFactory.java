package com.limaila.bms.tool.api;

import com.limaila.bms.common.response.ApiRsp;
import com.limaila.bms.tool.bean.Banner;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:29
 @desc
 ***/
@Component
@Slf4j
public class BannerClientFallbackFactory implements FallbackFactory<IBannerClient> {
    @Override
    public IBannerClient create(Throwable throwable) {
        return new IBannerClient() {
            @Override
            public ApiRsp<List<Banner>> getBannerList() {
                ApiRsp<List<Banner>> rsp = ApiRsp.<List<Banner>>builder()
                        .isFallBack(true)
                        .data(Collections.emptyList())
                        .build();
                log.error("BannerApiFallbackFactory getBannerList fallback rsp = {}", rsp);
                return rsp;
            }
        };
    }
}
