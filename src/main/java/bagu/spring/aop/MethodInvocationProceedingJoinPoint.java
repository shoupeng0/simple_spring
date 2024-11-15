package bagu.spring.aop;


import java.lang.reflect.Method;

public class MethodInvocationProceedingJoinPoint implements ProceedingJoinPoint {

    private final MethodInvocation methodInvocation;

    public MethodInvocationProceedingJoinPoint(MethodInvocation mi) {
        this.methodInvocation = mi;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodInvocation.proceed();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        this.methodInvocation.setArguments(args);
        return this.methodInvocation.proceed();
    }

    @Override
    public Object[] getArgs() {
        return this.methodInvocation.getArguments().clone();
    }

    @Override
    public Method getMethod() {
        return this.methodInvocation.getMethod();
    }

}