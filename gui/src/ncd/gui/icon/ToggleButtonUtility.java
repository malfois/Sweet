package ncd.gui.icon;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import ncd.gui.component.ToolTipText;
import ncd.gui.event.ToggleButtonEvent;

public class ToggleButtonUtility {

	public static final String UNKNOWN = "Unknown";

	public static ToggleButton getButton(String name) {
		ToggleButton unknown = new ToggleButton(UNKNOWN);
		if (name == null || name.length() == 0) return unknown;
		if (name.equalsIgnoreCase(PaneUtility.LOG_Y)) return LogY();
		return unknown;
	}

	private static ToggleButton LogY() {
		ToggleButton button = new ToggleButton();
		Pane pane = PaneUtility.get(PaneUtility.LOG_Y);

		button.setGraphic(pane);
		button.setTooltip(new ToolTipText("Plot in Log scale if pushed"));
		button.setOnAction(event -> button.fireEvent(new ToggleButtonEvent(ToggleButtonEvent.GRAPHIC_SCALE)));
		return button;
	}

}
