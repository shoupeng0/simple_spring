package bagu.spring.event;
import java.util.EventListener;


/**
 * 所有的监听器都要实现这个接口
 * 监听器针对事件要执行什么逻辑都写到了onApplicationEvent方法中
 * @author shoupeng
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	void onApplicationEvent(E event);

}