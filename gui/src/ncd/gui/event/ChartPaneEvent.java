package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ChartPaneEvent extends Event {

	private static final long						serialVersionUID	= 1110462648804797299L;

	public static final EventType<ChartPaneEvent>	SELECTION_CHANGED	= new EventType<>(Event.ANY, ChartPaneEvent.class.getSimpleName() + "VIEW CHANGED");
	public static final EventType<ChartPaneEvent>	PLOT_CHANGED	= new EventType<>(Event.ANY, ChartPaneEvent.class.getSimpleName() + "PLOT CHANGED");
	
	public ChartPaneEvent(EventType<? extends Event> eventType) {
		super(eventType);
	}

}
