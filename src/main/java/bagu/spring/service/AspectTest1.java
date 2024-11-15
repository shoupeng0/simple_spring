package bagu.spring.service;

import bagu.spring.anno.*;
import bagu.spring.aop.Aspect;
import bagu.spring.aop.ProceedingJoinPoint;

import java.io.Closeable;

@Aspect
@Component
public class AspectTest1 implements AutoCloseable{

    @Around("testService")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("执行环绕通知before...");
        Object result = pjp.proceed();
        System.out.println("执行环绕通知after...");
        return result;
    }

    @Before("testService")
    public void aroundMethod2() throws Throwable {
        System.out.println("执行前置方法....限流！！！");
    }

    @After("testService")
    public void aroundMethod3() throws Throwable {
        System.out.println("执行后置方法....");
    }

    @AfterReturning("testService")
    public void aroundMethod4() throws Throwable {
        System.out.println("AfterReturning....");
    }


    @Override
    public void close() throws Exception {
        System.out.println("AspectTest1 close...");
    }
}