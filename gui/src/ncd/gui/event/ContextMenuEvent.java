package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ContextMenuEvent<T> extends Event {

	private static final long			serialVersionUID	= 884926824052281039L;

	public static final EventType<?>	RENDERER_ACTION		= new EventType<>(Event.ANY, "Modify the renderer configuration");
	public static final EventType<?>	SAVE_DATA			= new EventType<>(Event.ANY, "Save the data");

	private T							result;

	public ContextMenuEvent(EventType<? extends Event> eventType, T result) {
		super(eventType);
		this.result = result;
	}

	public T getResult() {
		return result;
	}

}
