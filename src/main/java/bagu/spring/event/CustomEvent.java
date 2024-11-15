package bagu.spring.event;



/**
 * 自定义事件
 * @author ShouPeng
 * @description
 * @since 2024/11/14
 */
public class CustomEvent extends ApplicationContextEvent {

	private String message;

	public CustomEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}