package ncd.gui.component.initialvalue;

import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import ncd.gui.event.ComponentHandler;

public class ColorChooser extends ColorPicker implements IComponent<Color> {

	private Color initialValue;

	public ColorChooser(Color initialValue) {
		this.initialValue = initialValue;
		this.setValue(this.initialValue);
		this.addEventHandler(ActionEvent.ACTION, new ComponentHandler<Color>(this));
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
	public Color getInitialValue() {
		return this.initialValue;
	}

	@Override
	public String actionToLogger() {
		return "New colour: " + this.getValue();
	}

}
