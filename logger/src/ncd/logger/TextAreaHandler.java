package ncd.logger;

import java.util.logging.LogRecord;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TextAreaHandler extends TitledPane {

	private static volatile TextAreaHandler	INSTANCE;

	private Label							label	= new Label();

	private TextAreaHandler() {
		this.setText("Message");
		label.setPrefHeight(25);
		label.setWrapText(true);
		VBox box = new VBox();
		box.getChildren().add(label);
		this.setContent(box);
		box.setPrefHeight(25);
		this.setMaxHeight(50);
		this.setCollapsible(false);
		box.setAlignment(Pos.CENTER_LEFT);
		label.setAlignment(Pos.CENTER_LEFT);
	}

	public void info(LogRecord record) {
		SingleLineFormatter slf = new SingleLineFormatter();
		String message = slf.format(record);
		label.setTextFill(Color.BLACK);
		label.setText(message);
	}

	public void warning(LogRecord record) {
		SingleLineFormatter slf = new SingleLineFormatter();
		String message = slf.format(record);
		label.setTextFill(Color.ORANGE);
		label.setText(message);
	}

	public void error(LogRecord record) {
		SingleLineFormatter slf = new SingleLineFormatter();
		String message = slf.format(record);
		label.setTextFill(Color.RED);
		label.setText(message);
	}

	private static synchronized TextAreaHandler tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextAreaHandler();
		}
		return INSTANCE;
	}

	protected static TextAreaHandler getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		TextAreaHandler s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

}