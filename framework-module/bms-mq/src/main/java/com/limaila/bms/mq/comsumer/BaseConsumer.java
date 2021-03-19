package com.limaila.bms.mq.comsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(consumerGroup = "BaseConsumer", topic = "baseTopic")
@Slf4j
public class BaseConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String content) {
        log.info("BaseConsumer content = {}", content);
    }
}