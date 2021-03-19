package com.limaila.bms.mq;

public class MQConstants {

    private MQConstants(){}

    public static final String RETRY_TOPIC = "RETRY_TOPIC";

    public static final String RETRY_FAILED_TOPIC = "RETRY_FAILED_TOPIC";

    public static final int MAX_RETRY_COUNT = 3;
}
