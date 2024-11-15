package bagu.spring.event;

import java.util.EventObject;

/**
 * Class to be extended by all application events. Abstract as it
 * doesn't make sense for generic events to be published directly.
 * @author ShouPeng
 * @description
 * @since 2024/11/12
 */
public abstract class ApplicationEvent extends EventObject {

	private static final long serialVersionUID = 7099057708183571937L;

	public ApplicationEvent(Object source) {
		super(source);
	}

}