package bagu.spring.aop;

import java.lang.reflect.Method;

public interface JoinPoint {
    Object[] getArgs();  // 获取方法的参数
    Method getMethod();   // 获取当前执行的方法

}
