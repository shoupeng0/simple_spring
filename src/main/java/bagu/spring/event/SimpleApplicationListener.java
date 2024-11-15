package bagu.spring.event;

import bagu.spring.anno.Component;
import bagu.spring.anno.EventListener;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/14
 */
@Component
@EventListener
public class SimpleApplicationListener implements ApplicationListener<CustomEvent>{

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("Received custom event with message: " + event.getMessage());
    }

}
