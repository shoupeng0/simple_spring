package bagu.spring.aop;

import bagu.spring.anno.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * AOP 代理创建者
 */
public class AopProxyCreator {

    public static Object createAopProxy(Object target,String beanName) {
        Set<Object> aspects = AspectProcessor.getAspects();
        List<MethodInterceptor> methods = new ArrayList<>();
        // 检查目标类是否有切面
        for (Object aspect : aspects) {
            List<Method> aroundMethods = AspectProcessor.getAdviceMethods(aspect);
            for (Method method : aroundMethods) {
                if (method.isAnnotationPresent(Around.class) && method.getAnnotation(Around.class).value().equals(beanName)){
                    methods.add(new AroundAdvice(method,aspect));
                } else if (method.isAnnotationPresent(Before.class) && method.getAnnotation(Before.class).value().equals(beanName)) {
                    methods.add(new BeforeAdvice(method,aspect));
                } else if (method.isAnnotationPresent(After.class) && method.getAnnotation(After.class).value().equals(beanName)) {
                    methods.add(new AfterAdvice(method,aspect));
                } else if (method.isAnnotationPresent(AfterReturning.class) && method.getAnnotation(AfterReturning.class).value().equals(beanName)) {
                    methods.add(new AfterReturningAdvice(method,aspect));
                }else if (method.isAnnotationPresent(AfterThrowing.class) && method.getAnnotation(AfterThrowing.class).value().equals(beanName)) {
                    methods.add(new AfterThrowingAdvice(method,aspect));
                }
            }
        }
        if (methods.isEmpty()){
            return target;
        }
        methods.sort((o1, o2) -> o2.order() - o1.order());
        System.out.println("创建代理对象>>>>>>>>>>>>>>>" + target.getClass().getSimpleName());
        return new JdkAopProxy(target, methods).getProxy();
    }
}