package com.limaila.bms.tool.api.impl;

import com.limaila.bms.cache.RedisUtils;
import com.limaila.bms.cache.lock.LockUtils;
import com.limaila.bms.common.response.RestRSP;
import com.limaila.bms.json.JSONUtils;
import com.limaila.bms.tool.api.IBannerApi;
import com.limaila.bms.tool.api.IBannerMapping;
import com.limaila.bms.tool.bean.Banner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/***
 说明: 
 @author MrHuang
 @date 2020/9/1 19:51
 @desc
 ***/
@RestController
@Slf4j
public class BannerApi implements IBannerMapping {

    @Override
    public RestRSP<List<Banner>> getBannerList() {
        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(Banner.builder()
                .id((long) new Random().nextInt(100))
                .location(1)
                .name(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .url("http://www.baidu.com")
                .build());
//        log.info("setCache : " + RedisUtils.setForString("key", "getBannerList"));

        new Thread(() -> LockUtils.runWithLock("lock1", () -> {
            log.info("A runWithLock执行开始 !");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("A runWithLock执行结束 !");
            return true;
        })).start();


        new Thread(() -> LockUtils.runWithLock("lock1", () -> {
            log.info("B runWithLock执行开始 !");
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("B runWithLock执行结束 !");
            return true;
        })).start();
        return RestRSP.success(bannerList);
    }
}
