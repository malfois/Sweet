package ncd.gui.component.initialvalue;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import ncd.gui.event.ComponentHandler;

public class CComboBox<T> extends ComboBox<T> implements IComponent<T> {

	T initialValue;

	public CComboBox(T value) {
		this.initialValue = value;
		this.addEventHandler(ActionEvent.ACTION, new ComponentHandler<T>(this));
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
	public T getInitialValue() {
		return this.initialValue;
	}

	public void setInitialValue(T initialValue) {
		this.initialValue = initialValue;
	}

	@Override
	public String actionToLogger() {
		return "New value: " + this.getValue();
	}

}
