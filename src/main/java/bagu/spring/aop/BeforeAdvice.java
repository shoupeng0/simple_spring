package bagu.spring.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.AspectInstanceFactory;

import java.lang.reflect.Method;

public class BeforeAdvice extends CommonAdvice implements Advice, MethodInterceptor {

    public BeforeAdvice(Method aspectJAdviceMethod, Object aspectInstance) {
        super(aspectJAdviceMethod, aspectInstance);
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        before();
        return invocation.proceed();
    }

    public void before () throws Throwable {
        invokeAdviceMethod(null,null);
    }

    @Override
    public int order() {
        return 300;
    }
}