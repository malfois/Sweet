package ncd.gui.component.number;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import ncd.gui.component.initialvalue.IComponent;
import ncd.parameter.Format;

public class NumberLabel<T extends Number> extends Label implements IComponent<T> {

	protected T			number;
	protected T			initialValue;
	protected Format	format;

	public NumberLabel(T value, Format format) {
		this.number = value;
		this.setFormat(format);
		this.initialValue = value;

		this.setAlignment(Pos.CENTER_RIGHT);

		setStyle("-fx-background-color: rgba(150,150,150,0.75), rgba(255,255,255,0.75), linear-gradient(to bottom,-fx-base 0%,-fx-base 100%);"
				+ "-fx-background-insets: 0 1 1 0, 1 0 0 1, 1; -fx-padding: 3px; -fx-background-radius: 3px; -fx-border-radius: 3px; -fx-text-fill: black;");
	}

	public void setFormat(Format format) {
		this.format = format;
		setText(format.toText(number));
	}

	public Format getFormat() {
		return format;
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public void reset() {
		this.setValue(this.initialValue);
	}

	@Override
	public String actionToLogger() {
		return "New value: " + this.getValue();
	}

	@Override
	public void setValue(T value) {
		this.number = value;
		setText(format.toText(number));
	}

	@Override
	public T getValue() {
		return this.number;
	}

	@Override
	public T getInitialValue() {
		return this.initialValue;
	}

}
