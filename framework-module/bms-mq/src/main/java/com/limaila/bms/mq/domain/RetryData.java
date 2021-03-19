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

    private String destination;

    private String payload;

    private Integer retryCount;

    private String retryTime;
}
