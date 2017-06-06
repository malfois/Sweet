package ncd.gui.event;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;

public class MousePositionEvent extends Event {

	private static final long							serialVersionUID	= -7076513628748726429L;

	public static final EventType<MousePositionEvent>	MOUSE_MOVED			= new EventType<>(Event.ANY, "Mouse moved on selected curve");

	private Point2D										point;

	public MousePositionEvent(Point2D point) {
		super(MOUSE_MOVED);
		this.point = point;
	}

	public Point2D getPoint() {
		return point;
	}

}
