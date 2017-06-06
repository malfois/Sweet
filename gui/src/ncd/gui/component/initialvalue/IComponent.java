package ncd.gui.component.initialvalue;

import javafx.scene.control.Control;

public interface IComponent<T> {

	public void setValue(T value);

	public T getValue();

	public Control getControl();

	public void reset();

	public T getInitialValue();

	public String actionToLogger();

}
