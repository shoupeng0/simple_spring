package bagu.spring.event;

import bagu.spring.ApplicationContext;

/**
 * Base class for events raised for an {@code ApplicationContext}.
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

	/**
	 * Create a new ContextStartedEvent.
	 * @param source the {@code ApplicationContext} that the event is raised for
	 * (must not be {@code null})
	 */
	public ApplicationContextEvent(Object  source) {
		super(source);
	}

}