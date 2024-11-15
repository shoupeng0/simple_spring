package bagu.spring.aop;

public interface MethodInterceptor extends Interceptor,Order {

    Object invoke(MethodInvocation invocation) throws Throwable;
}