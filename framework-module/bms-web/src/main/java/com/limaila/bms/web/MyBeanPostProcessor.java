package com.limaila.bms.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/***
 说明: 
 @author MrHuang
 @date 2020/10/22 15:24
 @desc
 ***/
@Component
@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("MyBeanPostProcessor postProcessBeforeInitialization beanName = {} bean = {}", beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("MyBeanPostProcessor postProcessAfterInitialization beanName = {} bean = {}", beanName, bean);
        return bean;
    }
}
