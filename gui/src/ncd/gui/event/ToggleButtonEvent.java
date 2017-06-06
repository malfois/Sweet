package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ToggleButtonEvent extends Event {

	private static final long							serialVersionUID	= -1842188300195435465L;

	public static final EventType<ToggleButtonEvent>	ZOOM				= new EventType<>(Event.ANY, "Zoom");
	public static final EventType<ToggleButtonEvent>	FIT					= new EventType<>(Event.ANY, "Fit");
	public static final EventType<ToggleButtonEvent>	GRAPHIC_SCALE		= new EventType<>(Event.ANY, "Graphic scale changed");

	public ToggleButtonEvent(EventType<ToggleButtonEvent> eventType) {
		super(eventType);
	}

}
