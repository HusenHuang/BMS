package com.limaila.bms.mq;

import com.limaila.bms.mq.callback.BaseSendCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class MQUtils {

    private static RocketMQTemplate template;

    @Autowired
    public MQUtils(RocketMQTemplate template) {
        MQUtils.template = template;
    }

    public static TransactionSendResult sendMessageInTransaction(String topic, String tag, String payload) {
        String destination = destination(topic, tag);
        String transactionId = UUID.randomUUID().toString();
        Message<String> msg = MessageBuilder.withPayload(payload).setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId).build();
        return template.sendMessageInTransaction(destination, msg, null);
    }

    /**
     * 同步发送
     */
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

    /**
     * 异步发送
     */
    public static void asyncSend(String topic, String tag, String payload) {
        String destination = destination(topic, tag);
        asyncSend(destination, payload);
    }

    /**
     * 异步发送
     */
    public static void asyncSend(String destination, String payload) {
        asyncSend(destination, payload, 0);
    }

    /**
     * 异步发送
     */
    public static void asyncSend(String destination, String payload, int retryCount) {
        template.asyncSend(destination, payload, new BaseSendCallback(destination, payload, retryCount));
    }

    private static String destination(String topic, String tag) {
        return topic + ":" + tag;
    }

}
