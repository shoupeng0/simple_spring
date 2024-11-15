package bagu.spring.service;

import bagu.spring.anno.Autowired;
import bagu.spring.anno.Component;
import bagu.spring.anno.Polite;
import bagu.spring.aop.AopProxy;
import bagu.spring.interfaces.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
@Component("testService")
public class TestService implements T {

    @Autowired
    public OrderService orderService;

    @Override
    @Polite
    public String t(String name){
        try {
            orderService.hello();
        }catch (Exception e){}

        return name + "...";
    }

    public String t2(String name){
        System.out.println(1/0);
        return name + "...";
    }

}
