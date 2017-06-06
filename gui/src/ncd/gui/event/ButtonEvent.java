package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ButtonEvent extends Event {

	private static final long					serialVersionUID	= 9063736160207104418L;

	public static final EventType<ButtonEvent>	UNZOOM				= new EventType<>(Event.ANY, "Unzoom");
	public static final EventType<ButtonEvent>	SAVE_IMAGE			= new EventType<>(Event.ANY, "Save image");
	public static final EventType<ButtonEvent>	PRINT				= new EventType<>(Event.ANY, "Print");
	public static final EventType<ButtonEvent>	CONFIGURATION		= new EventType<>(Event.ANY, "Configuration");
	public static final EventType<ButtonEvent>	OPEN_FILE			= new EventType<>(Event.ANY, "Open file");
	public static final EventType<ButtonEvent>	LOAD_FILE			= new EventType<>(Event.ANY, "Load file");

	public ButtonEvent(EventType<ButtonEvent> eventType) {
		super(eventType);
	}

}
