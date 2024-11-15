package bagu.spring.aop;


import org.springframework.aop.aspectj.AspectInstanceFactory;

import java.lang.reflect.Method;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public class AfterThrowingAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public AfterThrowingAdvice(Method aspectJAdviceMethod, Object aspectInstance) {
        super(aspectJAdviceMethod, aspectInstance);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable ex) {
            if (shouldInvokeOnThrowing(ex)) {
                afterThrowing(ex);
            }
            throw ex;
        }
    }

    /**
     * 只有当抛出的异常是给定抛出类型的子类型时，才会调用 afterThrowing 通知。
     * @param ex
     * @return
     */
    private boolean shouldInvokeOnThrowing(Throwable ex) {
        return getDiscoveredThrowingType().isAssignableFrom(ex.getClass());
    }

    public void afterThrowing(Throwable ex) throws Throwable {
        invokeAdviceMethod(null, ex);
    }

    /**
     * 优先级
     */
    @Override
    public int order() {
        return 400;
    }
}
