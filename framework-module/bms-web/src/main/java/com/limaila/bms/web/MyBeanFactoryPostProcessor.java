package com.limaila.bms.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/***
 说明: 
 @author MrHuang
 @date 2020/10/22 15:30
 @desc
 ***/
@Component
@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("MyBeanFactoryPostProcessor...postProcessBeanFactory...");
        int count = beanFactory.getBeanDefinitionCount();
        String[] names = beanFactory.getBeanDefinitionNames();
        log.info("当前BeanFactory中有" + count + " 个Bean");
        log.info("当前BeanFactory中存在 {}", Arrays.asList(names));
    }
}
