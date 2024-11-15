package bagu.spring.event;

import bagu.spring.anno.Before;
import bagu.spring.anno.Component;
import bagu.spring.anno.EventListener;
import bagu.spring.aop.Aspect;


/**
 * @author ShouPeng
 * @description
 * @since 2024/11/14
 */
@Component
@EventListener
public class TestApplicationListener implements ApplicationListener<ContextRefreshedEvent>{

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("ContextRefreshedEvent event " + event);
    }

}
