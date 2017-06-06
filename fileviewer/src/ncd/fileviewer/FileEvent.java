package ncd.fileviewer;

import javafx.event.Event;
import javafx.event.EventType;

public class FileEvent<T> extends Event {

	private static final long					serialVersionUID	= 7373267766954695473L;

	public static final EventType<FileEvent<?>>	UPDATE_DIRECTORY	= new EventType<>(Event.ANY, "UPDATE_DIRECTORY");
	public static final EventType<FileEvent<?>>	CLEAR_SELECTION		= new EventType<>(Event.ANY, "Clear Selection");

	private T									result;

	public FileEvent(T result) {
		super(UPDATE_DIRECTORY);
		this.result = result;
	}

	public FileEvent(EventType<FileEvent<?>> eventType, T result) {
		super(eventType);
		this.result = result;
	}

	public T getResult() {
		return this.result;
	}

}
