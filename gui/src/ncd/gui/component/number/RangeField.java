package ncd.gui.component.number;

import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ncd.gui.component.initialvalue.IComponent;
import ncd.gui.event.ComponentEvent;
import ncd.parameter.Format;
import ncd.parameter.IRange;

public class RangeField<T extends Number> extends NumberField<T> implements IComponent<IRange<T>> {

	public RangeField(IRange<T> range, Format format) {
		super(range, format);

		this.addEventHandler(KeyEvent.KEY_PRESSED, event -> keyPressed(event));
	}

	@Override
	public void setValue(IRange<T> value) {
		this.range.set(value.copy());
	}

	@Override
	public IRange<T> getValue() {
		return this.range.getValue().copy();
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public void reset() {
		this.setRange(this.initialValue);
	}

	@Override
	public IRange<T> getInitialValue() {
		return this.initialValue;
	}

	@Override
	public String actionToLogger() {
		return "New value: " + this.getValue();
	}

	@Override
	protected void keyPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			ComponentEvent<IRange<T>> evt = this.checkInput();
			this.fireEvent(evt);
		}
	}

}
