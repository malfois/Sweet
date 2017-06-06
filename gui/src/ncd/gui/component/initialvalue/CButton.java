package ncd.gui.component.initialvalue;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import ncd.gui.event.ComponentHandler;

public class CButton extends Button implements IComponent<String> {

	private String initialValue;

	public CButton(String text) {
		this.initialValue = text;
		this.setText(this.initialValue);
		this.addEventHandler(ActionEvent.ACTION, new ComponentHandler<>(this));
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
		return "Button [" + this.getText() + "] clicked";
	}

}
