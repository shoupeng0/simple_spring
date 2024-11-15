package bagu.spring.aop;

import bagu.spring.util.AopUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class DefaultMethodInvocation implements MethodInvocation {

    private Object target;
    private Method method;
    private Object[] args;
    List<?> methodInterceptorList;
    // 调用位置
    private int currentInterceptorIndex = -1;

    public DefaultMethodInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
        this.target = target;
        this.method = method;
        if (args == null) {
            this.args = new Object[0];
        } else {
            this.args = args;
        }
        this.methodInterceptorList = methodInterceptorList;
    }

    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.methodInterceptorList.size() - 1) {
            return invokeJoinpoint();
        }
        // 逐一调用通知, currentInterceptorIndex + 1
        Object methodInterceptor = this.methodInterceptorList.get(++currentInterceptorIndex);
        return ((MethodInterceptor) methodInterceptor).invoke(this);
    }

    protected Object invokeJoinpoint() throws Throwable {
        return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.args);
    }

    @Override
    public Object[] getArguments() {
        return this.args;
    }

    @Override
    public void setArguments(Object[] args) {
        this.args = args;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }
}