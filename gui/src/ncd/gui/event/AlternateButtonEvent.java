package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class AlternateButtonEvent extends Event {

	private static final long							serialVersionUID	= -5036540238033774264L;

	public static final EventType<AlternateButtonEvent>	DEFAULT				= new EventType<>(Event.ANY, "Default");
	public static final EventType<AlternateButtonEvent>	ALTERNATE			= new EventType<>(Event.ANY, "Alternate");

	public AlternateButtonEvent(EventType<AlternateButtonEvent> eventType) {
		super(eventType);
	}

}
