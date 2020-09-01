package com.limaila.bms.tool.api.impl;

import com.limaila.bms.common.response.ApiRsp;
import com.limaila.bms.tool.api.IBannerApi;
import com.limaila.bms.tool.bean.Banner;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:51
 @desc
 ***/
@RestController
public class BannerApi implements IBannerApi {

    @Override
    public ApiRsp<List<Banner>> getBannerList() {
        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(Banner.builder()
                .id((long) new Random().nextInt(100))
                .location(1)
                .name(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .url("http://www.baidu.com")
                .build());
        return ApiRsp.<List<Banner>>builder().data(bannerList).build();
    }
}
