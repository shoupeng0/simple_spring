package bagu.spring.event;



/**
 * 事件广播器
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public interface ApplicationEventMulticaster {

	/**
	 * Add a listener to be notified of all events.
	 */
	void addApplicationListener(ApplicationListener<?> listener);


	/**
	 * Remove a listener from the notification list.
	 */
	void removeApplicationListener(ApplicationListener<?> listener);


	/**
	 * 广播
	 * @param event the event to multicast
	 */
	void multicastEvent(ApplicationEvent event);


}