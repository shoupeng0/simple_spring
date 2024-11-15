package bagu.spring.aop;

/**
 * ProceedingJoinPoint 扩展了 JoinPoint，并提供了一个可以继续执行目标方法的接口。
 */
public interface ProceedingJoinPoint extends JoinPoint {
    Object proceed() throws Throwable;  // 继续执行目标方法
    Object proceed(Object[] args) throws Throwable;  // 继续执行目标方法并传入参数
}