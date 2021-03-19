package com.limaila.bms.mq;

import com.limaila.bms.mq.callback.BaseSendCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQUtils {

    private static RocketMQTemplate template;

    @Autowired
    public MQUtils(RocketMQTemplate template) {
        MQUtils.template = template;
    }

    public static SendResult syncSend(String topic, String tag, String payload) {
        String destination = destination(topic, tag);
        SendResult sendResult = template.syncSend(destination, payload);
        if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
            new BaseSendCallback(destination, payload).onException(new RuntimeException("syncSend error!"));
        } else {
            new BaseSendCallback(destination, payload).onSuccess(sendResult);
        }
        return sendResult;
    }

    public static void asyncSend(String topic, String tag, String payload) {
        String destination = destination(topic, tag);
        asyncSend(destination, payload);
    }

    public static void asyncSend(String destination, String payload) {
        asyncSend(destination, payload, 0);
    }

    public static void asyncSend(String destination, String payload, int retryCount) {
        template.asyncSend(destination, payload, new BaseSendCallback(destination, payload, retryCount));
    }

    private static String destination(String topic, String tag) {
        return topic + ":" + tag;
    }

}
