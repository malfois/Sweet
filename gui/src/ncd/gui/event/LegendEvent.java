package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class LegendEvent extends Event {

	private static final long			serialVersionUID			= 6169976402448211603L;

	public static final EventType<?>	SELECTION_CHANGED			= new EventType<>(Event.ANY, "Selection changed");
	public static final EventType<?>	PLOT_CHANGED				= new EventType<>(Event.ANY, "Plot changed");
	public static final EventType<?>	PLOT_AND_SELECTION_CHANGED	= new EventType<>(Event.ANY, "Plot and selection changed");

	public LegendEvent(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
