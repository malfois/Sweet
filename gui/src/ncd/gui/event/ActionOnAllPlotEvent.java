package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ActionOnAllPlotEvent extends Event {

	private static final long			serialVersionUID	= 6380949264647020246L;

	public static final EventType<?>	CLEAR_ALL			= new EventType<>(Event.ANY, "Clear all curves");
	public static final EventType<?>	PLOT_ALL			= new EventType<>(Event.ANY, "Plot all curves");

	public ActionOnAllPlotEvent(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
