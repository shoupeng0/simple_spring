package bagu.spring.event;

import bagu.spring.anno.Autowired;
import bagu.spring.anno.Component;

/**
 * @author ShouPeng
 * @description
 * @since 2024/11/14
 */
@Component
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher{

    @Autowired
    private SimpleApplicationEventMulticaster simpleApplicationEventMulticaster;

    @Override
    public void publishEvent(ApplicationEvent event) {
        simpleApplicationEventMulticaster.multicastEvent(event);
    }


}
