package bagu.spring.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public class AfterAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public AfterAdvice(Method aspectJAdviceMethod, Object aspectInstance) {
        super(aspectJAdviceMethod, aspectInstance);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } finally {
            after();
        }
    }

    public void after() throws Throwable {
        invokeAdviceMethod(null,null);
    }

    /**
     * 优先级
     */
    @Override
    public int order() {
        return 200;
    }

}