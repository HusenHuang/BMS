package com.limaila.bms.authority.api;

import com.limaila.bms.common.utils.BmsProjectCommon;
import com.limaila.bms.feign.annotation.FeignExceptionApi;
import com.limaila.bms.feign.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = BmsProjectCommon.BMS_AUTHORITY_SERVICE, configuration = FeignConfiguration.class)
@FeignExceptionApi
public interface IUserApi extends IUserApiMapping {

}
