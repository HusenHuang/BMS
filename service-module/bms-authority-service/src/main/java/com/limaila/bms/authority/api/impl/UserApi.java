package com.limaila.bms.authority.api.impl;

import com.limaila.bms.authority.api.IUserApiMapping;
import com.limaila.bms.authority.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户API
 */
@RestController
@Slf4j
public class UserApi implements IUserApiMapping {

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
        throw new RuntimeException("AAAA");
//        return ROOT;
    }
}
