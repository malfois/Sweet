package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class RowEvent<T> extends Event {

	private static final long			serialVersionUID	= 6935405241652999945L;

	public static final EventType<?>	SELECTION_MODIFIED	= new EventType<>(Event.ANY, "Selection modified");
	public static final EventType<?>	CURVE_PLOT_ADDED	= new EventType<>(Event.ANY, "Curve plot added");
	public static final EventType<?>	CURVE_PLOT_REMOVED	= new EventType<>(Event.ANY, "Curve plot removed");
	public static final EventType<?>	VISIBILITY_CHANGED	= new EventType<>(Event.ANY, "Visibility changed");
	public static final EventType<?>	DATA_PLOT_CLICKED	= new EventType<>(Event.ANY, "dataset checkbox clicked");
	public static final EventType<?>	DATA_PLOT_ADDED		= new EventType<>(Event.ANY, "All data curves added to the plot");
	public static final EventType<?>	DATA_PLOT_REMOVED	= new EventType<>(Event.ANY, "All data curves removed from the plot");

	private T							result;

	public RowEvent(EventType<? extends Event> eventType, T result) {
		super(eventType);
		this.result = result;
	}

	public T getResult() {
		return result;
	}

}
