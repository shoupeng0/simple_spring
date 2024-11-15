package bagu.spring.aop;

import bagu.spring.ApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AspectProcessor 用来扫描和处理 Aspect 注解的切面类
 */
public class AspectProcessor {

    private static final Set<Object> aspects = new HashSet<>();


    /**
     * 获取所有切面类实例
     */
    public static Set<Object> getAspects(ApplicationContext applicationContext) {
        System.out.println(applicationContext.getAllBeanClass());
        for (Class<?> clazz : applicationContext.getAllBeanClass()) {
            if (clazz.isAnnotationPresent(Aspect.class)) {
                try {
                    Object aspect = clazz.getDeclaredConstructor().newInstance();
                    aspects.add(aspect);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return aspects;
    }

    public static Set<Object> getAspects() {
        return aspects;
    }

    /**
     * 获取切面类的方法，如果该方法有注解，则返回
     */
    public static List<Method> getAdviceMethods(Object aspect) {
        List<Method> methods = new ArrayList<>();
        for (Method method : aspect.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(bagu.spring.anno.Around.class)
                    || method.isAnnotationPresent(bagu.spring.anno.Before.class)
                    || method.isAnnotationPresent(bagu.spring.anno.AfterReturning.class)
                    || method.isAnnotationPresent(bagu.spring.anno.AfterThrowing.class)
                    || method.isAnnotationPresent(bagu.spring.anno.After.class) ) {
                methods.add(method);
            }
        }
        return methods;
    }
}