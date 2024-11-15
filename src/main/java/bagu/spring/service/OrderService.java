package bagu.spring.service;

import bagu.spring.anno.Autowired;
import bagu.spring.anno.Component;
import bagu.spring.aware.BeanNameAware;
import bagu.spring.interfaces.DisposableBean;
import bagu.spring.interfaces.InitializingBean;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/13
 */
@Component
public class OrderService extends BeanNameAware implements DisposableBean, InitializingBean {

    @Autowired
    public T testService;

    @Override
    public void destroy() throws Exception {
        System.out.println("orderService destroy...");
    }

    public void hello() throws Exception {
        System.out.println("orderService hello...");
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("orderService setBeanName-->>>>" + beanName);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("orderService InitializingBean..." );
    }

}
