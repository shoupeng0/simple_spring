package bagu.spring.aop;

import bagu.spring.ProxyResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * JDK 动态代理实现，用于方法环绕通知
 */
public class JdkAopProxy implements AopProxy {

    //被代理的目标对象
    private Object target;

    //环绕通知的方法
    private List<MethodInterceptor> adviceMethods;

    public JdkAopProxy(Object target, List<MethodInterceptor> adviceMethods) {
        this.target = target;
        this.adviceMethods = adviceMethods;
    }

    @Override
    public Object getProxy() {
        return ProxyResolver.createProxy(target,new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (adviceMethods.isEmpty()){
                    return method.invoke(target, args);
                }
                System.out.println("拦截方法:" + method.getName());
                DefaultMethodInvocation methodInvocation = new DefaultMethodInvocation(target, method, args, adviceMethods);

                Object result = methodInvocation.proceed();

                return result;
            }
        });
    }

}