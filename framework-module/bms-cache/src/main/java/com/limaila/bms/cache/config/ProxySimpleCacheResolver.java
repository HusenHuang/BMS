package com.limaila.bms.cache.config;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProxySimpleCacheResolver extends SimpleCacheResolver {

    /**
     * Construct a new {@code SimpleCacheResolver}.
     *
     * @see #setCacheManager
     */
    public ProxySimpleCacheResolver() {
    }

    /**
     * Construct a new {@code SimpleCacheResolver} for the given {@link CacheManager}.
     *
     * @param cacheManager the CacheManager to use
     */
    public ProxySimpleCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(context.getTarget());
        CacheConfig cacheConfig = AnnotationUtils.findAnnotation(targetClass, CacheConfig.class);
        if (cacheConfig != null && cacheConfig.cacheNames().length > 0) {
            // 如果代理对象上面有则取代理对象上面的
            return Arrays.stream(cacheConfig.cacheNames()).collect(Collectors.toSet());
        } else {
            return super.getCacheNames(context);
        }
    }
}
