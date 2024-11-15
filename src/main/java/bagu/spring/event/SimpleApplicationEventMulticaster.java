package bagu.spring.event;
import bagu.spring.anno.Component;

import java.util.Set;
/**
 * 广播器
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
@Component
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    /**
     * 广播
     * @param event the event to multicast
     */
    @Override
    public void multicastEvent(final ApplicationEvent event) {
        //1. 获取到所有匹配的监听器
        Set<ApplicationListener<ApplicationEvent>> listeners = getApplicationListeners(event);
        //2. 依次触发监听器的执行
        for (ApplicationListener<ApplicationEvent> listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

}