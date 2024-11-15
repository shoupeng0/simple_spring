package bagu.spring;

import bagu.spring.anno.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyResolver {

    public static  <T> T createProxy(T bean, InvocationHandler handler) {
        return (T)Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return handler.invoke(bean, method, args);
                    }
                });
    }
}