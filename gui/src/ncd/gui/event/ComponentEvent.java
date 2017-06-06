package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ComponentEvent<T> extends Event {

	private static final long							serialVersionUID	= -7894768327991760751L;

	public static final EventType<ComponentEvent<?>>	INPUT_VALIDATED		= new EventType<>(Event.ANY, "Input validated");
	public static final EventType<ComponentEvent<?>>	WARNING				= new EventType<>(Event.ANY, "Warning on input");
	public static final EventType<ComponentEvent<?>>	ERROR				= new EventType<>(Event.ANY, "Input error");

	private T											result;
	private String										message;

	public ComponentEvent(EventType<? extends Event> eventType, T result) {
		super(eventType);
		this.result = result;
	}

	@Override
	public String toString() {
		return "ComponentEvent [result=" + result + ", message=" + message + "]";
	}

	public ComponentEvent(EventType<? extends Event> eventType, T result, String message) {
		super(eventType);
		this.result = result;
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}

}
