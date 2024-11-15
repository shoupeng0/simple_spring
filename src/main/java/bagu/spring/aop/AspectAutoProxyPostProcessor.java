package bagu.spring.aop;

import bagu.spring.anno.Component;
import bagu.spring.interfaces.BeanPostProcessor;
import bagu.spring.interfaces.SmartInstantiationAwareBeanPostProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */

@Component
public class AspectAutoProxyPostProcessor implements BeanPostProcessor, SmartInstantiationAwareBeanPostProcessor {

    /**
     * 记录哪些 bean 尝试过提前创建代理，无论这个 bean 是否创建了代理增强，都记录下来，
     * 等到初始化阶段进行创建代理时，检查缓存，避免重复创建代理。
     * 存储的值就是 beanName
     */
    private final Set<String> earlyProxyReferences = new HashSet<>();

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    private Object wrapIfNecessary(Object bean, String beanName) {
        return AopProxyCreator.createAopProxy(bean,beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            if (!earlyProxyReferences.contains(beanName)) {
                return wrapIfNecessary(bean, beanName);
            } else {
                // earlyProxyReferences 中包含当前 beanName，不再重复进行代理创建，直接返回
                earlyProxyReferences.remove(beanName);
            }
        }
        return bean;
    }

}
