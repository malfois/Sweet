package ncd.gui.icon;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import ncd.gui.component.ToolTipText;
import ncd.gui.event.ButtonEvent;

public class ButtonUtility {

	public static final String UNKNOWN = "Unknown";

	public static Button getButton(String name) {
		Button unknown = new Button(UNKNOWN);
		if (name == null || name.length() == 0) return unknown;
		if (name.equalsIgnoreCase(FileUtility.SAVE_IMAGE)) return SaveImage();
		if (name.equalsIgnoreCase(FileUtility.UNZOOM)) return Unzoom();
		if (name.equalsIgnoreCase(FileUtility.PRINT)) return Print();
		if (name.equalsIgnoreCase(FileUtility.CONFIGURATION)) return Configuration();
		if (name.equalsIgnoreCase(FileUtility.OPEN_FILE)) return OpenFile();
		if (name.equalsIgnoreCase(FileUtility.LOAD_FILE)) return LoadFile();
		return unknown;
	}

	private static Button LoadFile() {
		Button button = new Button();
		button.setGraphic(FileUtility.get(FileUtility.LOAD_FILE));
		button.setTooltip(new ToolTipText("Upload the last file"));
		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ButtonEvent(ButtonEvent.LOAD_FILE)));
		return button;
	}

	private static Button OpenFile() {
		Button button = new Button();
		button.setGraphic(FileUtility.get(FileUtility.OPEN_FILE));
		button.setTooltip(new ToolTipText("Open new files"));
		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ButtonEvent(ButtonEvent.OPEN_FILE)));
		return button;
	}

	private static Button Configuration() {
		Button button = new Button();
		button.setGraphic(FileUtility.get(FileUtility.CONFIGURATION));
		button.setTooltip(new ToolTipText("Change configuration"));
		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ButtonEvent(ButtonEvent.CONFIGURATION)));
		return button;
	}

	private static Button SaveImage() {
		Button button = new Button();
		button.setGraphic(FileUtility.get(FileUtility.SAVE_IMAGE));
		button.setTooltip(new ToolTipText("Save the chart into a file"));
		button.setOnAction(event -> button.fireEvent(new ButtonEvent(ButtonEvent.SAVE_IMAGE)));
		return button;
	}

	private static Button Unzoom() {
		Button button = new Button();
		button.setGraphic(FileUtility.get(FileUtility.UNZOOM));
		button.setTooltip(new ToolTipText("Reset the zoom - Full data view"));
		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ButtonEvent(ButtonEvent.UNZOOM)));
		return button;
	}

	private static Button Print() {
		Button button = new Button();
		button.setGraphic(FileUtility.get(FileUtility.PRINT));
		button.setTooltip(new ToolTipText("Print the chart"));
		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ButtonEvent(ButtonEvent.PRINT)));
		return button;
	}

}
