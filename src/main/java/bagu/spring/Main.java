package bagu.spring;

import bagu.spring.aop.AspectProcessor;
import bagu.spring.config.AppConfig;
import bagu.spring.event.ContextRefreshedEvent;
import bagu.spring.event.CustomEvent;
import bagu.spring.event.SimpleApplicationEventPublisher;
import bagu.spring.service.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.*;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        System.out.println("======================= start test aop =======================");
        T testService = (T)applicationContext.getBean("testService");
        OrderService orderService = (OrderService) applicationContext.getBean("orderService");
        System.out.println(applicationContext.getAllBeanClass());
        Set<Object> aspects = AspectProcessor.getAspects();
        System.out.println(aspects);
        orderService.testService.t("tom");
        System.out.println("======================= end test aop =======================");

        System.out.println("======================= start test event =======================");
        SimpleApplicationEventPublisher simpleApplicationEventPublisher = (SimpleApplicationEventPublisher) applicationContext.getBean("simpleApplicationEventPublisher");
        System.out.println(simpleApplicationEventPublisher);

        simpleApplicationEventPublisher.publishEvent(new CustomEvent(applicationContext,"你好!"));
        simpleApplicationEventPublisher.publishEvent(new CustomEvent(applicationContext,"你好2!"));
        simpleApplicationEventPublisher.publishEvent(new CustomEvent(applicationContext,"你好3!"));
        simpleApplicationEventPublisher.publishEvent(new CustomEvent(applicationContext,"你好4!"));
        simpleApplicationEventPublisher.publishEvent(new ContextRefreshedEvent(applicationContext));
        System.out.println("======================= end test event =======================");

        applicationContext.close();

    }

}
