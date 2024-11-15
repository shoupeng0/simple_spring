package bagu.spring.aop;


import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.AspectInstanceFactory;

import java.lang.reflect.Method;

public class AroundAdvice extends CommonAdvice implements Advice, MethodInterceptor,Order {

    public AroundAdvice(Method aspectJAdviceMethod, Object aspectInstance) {
        super(aspectJAdviceMethod, aspectInstance);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ProceedingJoinPoint pjp = getProceedingJoinPoint(invocation);
        return around(pjp);
    }

    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        return invokeAdviceMethod(pjp, null);
    }

    protected ProceedingJoinPoint getProceedingJoinPoint(MethodInvocation mi) {
        return new MethodInvocationProceedingJoinPoint(mi);
    }

    @Override
    public int order() {
        return 9999;
    }

}