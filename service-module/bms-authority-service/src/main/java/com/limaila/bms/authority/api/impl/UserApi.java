package com.limaila.bms.authority.api.impl;

import com.limaila.bms.authority.api.IUserMapping;
import com.limaila.bms.authority.bean.User;
import com.limaila.bms.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户API
 */
@RestController
@Slf4j
public class UserApi implements IUserMapping {

    /**
     * ROOT 账号
     */
    private static final User ROOT = new User(1L, "ROOT", 18);

    @Override
    public User getUserById(@RequestParam("userId") Long userId) {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        throw new CommonException("AAAA");
//        return ROOT;
    }
}
