package com.limaila.bms;

import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/***
 @Author:MrHuang
 @Date: 2019/12/4 15:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Component
public class ApplicationUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationUtils.applicationContext = applicationContext;
    }


    /**
     * 获取Spring中的Bean
     * @param beanClass beanClass
     * @param <T> 泛型
     * @return
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 获取Spring中的Bean
     * @param beanName beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }


    /**
     * 从Spring容器获取具体值反射调用方法
     * @param beanClass
     * @param methodName
     * @param args
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> Object invokeMethod(Class<T> beanClass, String methodName, Object ... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        T bean = getBean(beanClass);
        return MethodUtils.invokeMethod(bean, methodName, args);
    }

    /**
     * 从Spring容器获取具体值反射调用方法
     * @param beanName
     * @param methodName
     * @param args
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object invokeMethod(String beanName, String methodName, Object ... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object bean = getBean(beanName);
        return MethodUtils.invokeMethod(bean, methodName, args);
    }

    /**
     *
     * @param event
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }
}
