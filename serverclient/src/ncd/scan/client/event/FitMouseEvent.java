package ncd.scan.client.event;

import javafx.event.Event;
import javafx.event.EventType;

public class FitMouseEvent extends Event {

	private static final long						serialVersionUID	= -8693641777545063962L;

	public static final EventType<FitMouseEvent>	FIT_PERFORMED		= new EventType<>(Event.ANY, "Fit performed");

	public FitMouseEvent() {
		super(FIT_PERFORMED);
	}

}
