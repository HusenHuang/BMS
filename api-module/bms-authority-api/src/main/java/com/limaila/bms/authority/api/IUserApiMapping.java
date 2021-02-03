package com.limaila.bms.authority.api;

import com.limaila.bms.authority.bean.User;
import com.limaila.bms.common.response.RestResponse;
import com.limaila.bms.feign.annotation.FeignExceptionApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/user")
public interface IUserApiMapping {

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/getUserById")
    @FeignExceptionApi
    RestResponse<User> getUserById(@RequestParam("userId") Long userId);

}
