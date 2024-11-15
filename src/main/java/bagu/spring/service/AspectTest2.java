package bagu.spring.service;

import bagu.spring.anno.*;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
@bagu.spring.aop.Aspect
@Component
public class AspectTest2 implements AutoCloseable{

    @AfterThrowing(value = "testService")
    public void afterThrowing(Exception e) throws Throwable {
        System.out.println("afterThrowing...." + e.getMessage());
    }

    @Override
    public void close() throws Exception {
        System.out.println("AspectTest2 close...");
    }
}