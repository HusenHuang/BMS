package com.limaila.bms.mq.scheduling;


import com.alibaba.fastjson.JSON;
import com.limaila.bms.cache.RedisUtils;
import com.limaila.bms.mq.MQConstants;
import com.limaila.bms.mq.MQUtils;
import com.limaila.bms.mq.domain.RetryData;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulingTask {

    @Scheduled(fixedDelay = 3000, initialDelay = 20000)
    public void pushRetryMessage() {
        log.debug("SchedulingTask pushRetryMessage ........ ");
        int maxCount = 100;
        int count = 0;
        while (true) {
            count++;
            if (count >= maxCount) {
                // 跳过
                break;
            }
            String data = RedisUtils.rightPopForList(MQConstants.RETRY_TOPIC);
            if (StringUtils.isBlank(data)) {
                // 跳过
                break;
            }
            RetryData retryData = JSON.parseObject(data, RetryData.class);
            if (retryData == null) {
                // 跳过
                break;
            }
            log.info("SchedulingTask asyncSend ..");
            MQUtils.asyncSend(retryData.getDestination(), retryData.getPayload(), retryData.getRetryCount());
        }
        log.debug("SchedulingTask pushRetryMessage over ........ ");
    }

}
