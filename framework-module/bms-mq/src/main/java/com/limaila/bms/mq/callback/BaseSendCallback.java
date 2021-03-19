package com.limaila.bms.mq.callback;

import com.alibaba.fastjson.JSON;
import com.limaila.bms.cache.RedisUtils;
import com.limaila.bms.mq.MQConstants;
import com.limaila.bms.mq.domain.RetryData;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class BaseSendCallback implements SendCallback {

    private final String destination;

    private final String payload;

    private final int retryCount;

    public BaseSendCallback(String destination, String payload, int retryCount) {
        this.destination = destination;
        this.payload = payload;
        this.retryCount = retryCount;
    }

    public BaseSendCallback(String destination, String payload) {
        this.destination = destination;
        this.payload = payload;
        this.retryCount = 0;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("BaseSendCallback onSuccess destination = [{}] payload = [{}] result = [{}]", destination, payload, sendResult);
    }

    @Override
    public void onException(Throwable throwable) {
        log.info("BaseSendCallback onException destination = [{}] payload = [{}] throwable = [{}]", destination, payload, throwable);
        // 加入重发队列
//        template.send
        int retryNum = retryCount + 1;
        RetryData retryData = RetryData.builder()
                .destination(destination)
                .payload(payload)
                .retryCount(retryNum)
                .retryTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                .build();
        if (retryNum >= MQConstants.MAX_RETRY_COUNT) {
            log.error("BaseSendCallback onException retryCount to Max !!!! destination = [{}]  payload = [{}] ", destination, payload);
            RedisUtils.leftPushForList(MQConstants.RETRY_FAILED_TOPIC, JSON.toJSONString(retryData));
            return;
        }
        Long result = RedisUtils.leftPushForList(MQConstants.RETRY_TOPIC, JSON.toJSONString(retryData));
        if (result == null) {
            // double !!!
            result = RedisUtils.leftPushForList(MQConstants.RETRY_TOPIC, JSON.toJSONString(retryData));
            if (result == null) {
                log.error("BaseSendCallback onException retry failed !!! destination = [{}]  payload = [{}]  retryCount = [{}]", destination, payload, retryNum);
                RedisUtils.leftPushForList(MQConstants.RETRY_FAILED_TOPIC, JSON.toJSONString(retryData));
            }
        }

    }
}
