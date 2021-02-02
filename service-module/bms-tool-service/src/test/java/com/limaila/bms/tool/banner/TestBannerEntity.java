package com.limaila.bms.tool.banner;

import com.limaila.bms.tool.bean.BannerEntity;
import com.limaila.bms.tool.service.IBannerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestBannerEntity {

    @Autowired
    private IBannerService bannerService;

    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        BannerEntity entity = new BannerEntity();
        String uuid = UUID.randomUUID().toString();
        entity.setName(uuid);
        entity.setLocation(1);
        entity.setUrl("http://www.s.com/" +  uuid);
        System.out.println("add ------ " + bannerService.add(entity));
        System.out.println("get ------" + bannerService.getById(entity.getId()));
        System.out.println("get2 ------" + bannerService.getById(entity.getId()));
        entity.setLocation(2);
        System.out.println("updateByPrimaryKey ------" + bannerService.updateById(entity));
//        Object de = ApplicationUtils.invokeMethod(IBannerService.class, "deleteById", entity.getId());
//        System.out.println(de);
//        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
//        System.out.println(runtimeMXBean);

//        Sequence sequence = new Sequence();
//        System.out.println(sequence);
    }

}
