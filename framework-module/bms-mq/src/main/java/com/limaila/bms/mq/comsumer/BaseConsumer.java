package com.limaila.bms.mq.comsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(consumerGroup = "BaseConsumer", topic = "baseTopic")
@Slf4j
public class BaseConsumer extends AbstractConsumer {

    @Override
    void processMessage(String message) {

    }
}