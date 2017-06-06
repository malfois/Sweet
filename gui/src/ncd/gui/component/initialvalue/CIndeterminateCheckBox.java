package ncd.gui.component.initialvalue;

import java.util.Arrays;

import javafx.scene.control.Control;

public class CIndeterminateCheckBox extends CCheckBox<Boolean[]> implements IComponent<Boolean[]> {

	public final static int	INDETERMINATE_INDEX	= 0;
	public final static int	SELECTION_INDEX		= 1;

	public CIndeterminateCheckBox() {
		super(new Boolean[] { true, true });
		this.update(this.getInitialValue());
	}

	public CIndeterminateCheckBox(final Boolean... inititalValue) {
		super(inititalValue);
		this.update(this.getInitialValue());
	}

	private void update(Boolean[] value) {
		this.setIndeterminate(value[INDETERMINATE_INDEX]);
		this.setSelected(value[SELECTION_INDEX]);
	}

	@Override
	public void setValue(Boolean[] value) {
		this.update(value);
	}

	@Override
	public Boolean[] getValue() {
		return new Boolean[] { this.isIndeterminate(), this.isSelected() };
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public void reset() {
		this.update(this.getInitialValue());
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return (getValue() != null ? Arrays.asList(getValue()).subList(0, Math.min(getValue().length, maxLen)) : null) + "]";
	}

	@Override
	public String actionToLogger() {
		Boolean[] values = this.getValue();
		return "Set indeterminate: " + values[INDETERMINATE_INDEX] + " Set selected: " + values[SELECTION_INDEX];
	}

}
