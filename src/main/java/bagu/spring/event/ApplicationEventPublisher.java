package bagu.spring.event;

/**
 * @author ShouPeng
 * @description 事件发布者
 * @since 2024/11/12
 */
public interface ApplicationEventPublisher {

	void publishEvent(ApplicationEvent event);

}