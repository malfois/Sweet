package ncd.gui.component.initialvalue;

import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class CLabel extends Label implements IComponent<String> {

	private String initialValue;

	public CLabel() {
		this.initialValue = "";
		this.setText(this.initialValue);
	}

	public CLabel(String value) {
		this.initialValue = value;
		this.setText(value);
	}

	public CLabel(String value, Node node) {
		this.initialValue = value;
		this.setText(value);
		this.setGraphic(node);
	}

	public CLabel(Node node) {
		this.initialValue = "";
		this.setText("");
		this.setGraphic(node);
	}

	@Override
	public void setValue(String value) {
		this.setText(value);
	}

	@Override
	public String getValue() {
		return this.getText();
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public void reset() {
		this.setText(this.initialValue);
	}

	@Override
	public String getInitialValue() {
		return this.initialValue;
	}

	@Override
	public String actionToLogger() {
		return this.getText();
	}

}
