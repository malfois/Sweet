package ncd.gui.component.initialvalue;

import javafx.scene.control.Control;

public class CDeterminateCheckBox extends CCheckBox<Boolean> implements IComponent<Boolean> {

	public CDeterminateCheckBox() {
		super(true);
		this.setIndeterminate(false);
		this.setSelected(initialValue);
	}

	public CDeterminateCheckBox(Boolean initialValue) {
		super(initialValue);
		this.setIndeterminate(false);
		this.setSelected(initialValue);
	}

	@Override
	public void setValue(Boolean value) {
		this.setSelected(value);
	}

	@Override
	public Boolean getValue() {
		return this.isSelected();
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public void reset() {
		this.setSelected(this.getInitialValue());
	}

	@Override
	public String actionToLogger() {
		return "Set selected: " + this.getValue();
	}

}
