package com.limaila.bms.common.context;

/***
 @author:MrHuang
 @date: 2020/2/27 3:51
 @desc:
 @version: 1.0
 ***/
public class RequestContextHolder {

    /**
     * 分布式调用上下文
     */
    private static final ThreadLocal<RequestContext> CONTEXT = ThreadLocal.withInitial(RequestContext::newInstance);


    /**
     * 获取上下文
     * @return 上下文
     */
    public static RequestContext getContext() {
        return CONTEXT.get();
    }

    /**
     * 设置上下文
     * @param requestContext 上下文
     */
    public static void setContext(RequestContext requestContext) {
        CONTEXT.set(requestContext);
    }

    /**
     * 删除上下文
     */
    public static void removeContext() {
        CONTEXT.remove();
    }
}
