package com.limaila.bms.mq.comsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;

@Slf4j
public abstract class AbstractConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("AbstractConsumer content = {}", message);
        processMessage(message);
    }

    abstract void processMessage(String message);
}
