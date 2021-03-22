package com.limaila.bms.mq;

public class MQConstants {

    private MQConstants() {}

    /**
     * 重试Topic
     */
    public static final String RETRY_TOPIC = "RETRY_TOPIC";

    /**
     * 重试失败Topic
     */
    public static final String RETRY_FAILED_TOPIC = "RETRY_FAILED_TOPIC";

    /**
     * 最大重试次数
     */
    public static final int MAX_RETRY_COUNT = 3;
}
