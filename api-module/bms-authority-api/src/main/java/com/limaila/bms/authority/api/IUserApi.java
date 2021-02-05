package com.limaila.bms.authority.api;

import com.limaila.bms.common.utils.BmsProjectCommon;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = BmsProjectCommon.BMS_AUTHORITY_SERVICE)
public interface IUserApi extends IUserMapping {

}
