package ncd.gui.component.initialvalue;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import ncd.gui.event.ComponentHandler;

public abstract class CCheckBox<T> extends CheckBox implements IComponent<T> {

	protected T initialValue;

	protected CCheckBox(T inititalValue) {
		this.initialValue = inititalValue;
		this.addEventHandler(ActionEvent.ACTION, new ComponentHandler<>(this));
	}

	public T getInitialValue() {
		return this.initialValue;
	}
}
