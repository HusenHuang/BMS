package com.limaila.bms.tool;

import com.limaila.bms.mq.MQUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMq {



    @Test
    public void test() {
        System.out.println("OK  !");
        for (int i = 0; i < 10000; i++) {
            MQUtils.asyncSend("baseTopic", "content: count = " + i);
        }
        System.out.println("OK over !");
    }
}
