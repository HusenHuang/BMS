package com.limaila.bms.mq.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryData implements Serializable {

    /**
     * destination
     */
    private String destination;

    /**
     * payload
     */
    private String payload;

    /**
     * retryCount
     */
    private Integer retryCount;

    /**
     * retryTime
     */
    private String retryTime;
}
