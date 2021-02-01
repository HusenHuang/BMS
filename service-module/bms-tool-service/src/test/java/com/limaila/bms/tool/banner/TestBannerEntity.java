package com.limaila.bms.tool.banner;

import com.limaila.bms.ApplicationUtils;
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
        System.out.println("get ------" + bannerService.getByPrimaryKey(entity.getId()));
        System.out.println("get2 ------" + bannerService.getByPrimaryKey(entity.getId()));

        entity.setLocation(2);
        System.out.println("updateByPrimaryKey ------" + bannerService.updateByPrimaryKey(entity));

        Object de = ApplicationUtils.invokeMethod(IBannerService.class, "deleteByPrimaryKey", entity.getId());
        System.out.println(de);
    }
}
