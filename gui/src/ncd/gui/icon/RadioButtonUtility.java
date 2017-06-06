package ncd.gui.icon;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import ncd.gui.component.ToolTipText;
import ncd.gui.event.ToggleButtonEvent;

public class RadioButtonUtility {

	public static final String UNKNOWN = "Unknown";

	public static RadioButton getButton(String name) {
		RadioButton unknown = new RadioButton(UNKNOWN);
		if (name == null || name.length() == 0) return unknown;
		if (name.equalsIgnoreCase(FileUtility.ZOOM)) return Zoom();
		if (name.equalsIgnoreCase(FileUtility.FIT)) return Fit();
		return unknown;
	}

	private static RadioButton Zoom() {
		RadioButton button = new RadioButton();
		button.setGraphic(FileUtility.get(FileUtility.ZOOM));
		button.setTooltip(new ToolTipText("Zoom mode - If pushed, dragging the mouse on the chart will perform a zoom"));
		button.getStyleClass().remove("radio-button");
		button.getStyleClass().add("toggle-button");

		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ToggleButtonEvent(ToggleButtonEvent.ZOOM)));
		return button;
	}

	private static RadioButton Fit() {
		RadioButton button = new RadioButton();
		ImageView imageView = FileUtility.get(FileUtility.FIT);
		Font defaultFont = Font.getDefault();
		Font font = Font.font(defaultFont.getName(), FontPosture.ITALIC, 13);
		Text text = new Text("fit");
		text.setFont(font);
		text.setTranslateX(5);
		StackPane pane = new StackPane();
		StackPane.setAlignment(imageView, Pos.CENTER_LEFT);
		StackPane.setAlignment(text, Pos.CENTER_RIGHT);
		pane.getChildren().addAll(imageView, text);
		button.setGraphic(pane);
		button.setTooltip(new ToolTipText("Fit mode - If pushed, dragging the mouse on the chart will perform a fit of the data"));
		button.getStyleClass().remove("radio-button");
		button.getStyleClass().add("toggle-button");

		button.addEventFilter(ActionEvent.ACTION, event -> button.fireEvent(new ToggleButtonEvent(ToggleButtonEvent.FIT)));
		return button;
	}

}
