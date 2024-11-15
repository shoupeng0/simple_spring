package bagu.spring.event;



/**
 * 上下文刷新事件
 * @author ShouPeng
 * @description
 * @since 2024/11/14
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

	public ContextRefreshedEvent(Object source) {
		super(source);
	}


}