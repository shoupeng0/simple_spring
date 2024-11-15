package bagu.spring.aop;


import org.springframework.aop.aspectj.AspectInstanceFactory;

import java.lang.reflect.Method;


/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public class AfterReturningAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    /**
     * 默认拦截所有的异常类型
     */
    private Class<?> discoveredThrowingType = Exception.class;

    /**
     * @AfterThrowing(" throwing = "java.lang.ClassNotFoundException")
     * throwing 的值
     */
    private String throwingName;

    public AfterReturningAdvice(Method aspectJAdviceMethod, Object aspectInstance) {
        super(aspectJAdviceMethod, aspectInstance);
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retVal = invocation.proceed();
        afterReturning();
        return retVal;
    }

    public void afterReturning() throws Throwable {
        invokeAdviceMethod(null,null);
    }



    /**
     * 优先级
     */
    @Override
    public int order() {
        return 300;
    }

}
