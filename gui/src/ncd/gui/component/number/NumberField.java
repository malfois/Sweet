package ncd.gui.component.number;

import org.apache.commons.lang.math.NumberUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import ncd.gui.component.ToolTipText;
import ncd.gui.event.ComponentEvent;
import ncd.logger.NcdLogger;
import ncd.parameter.Format;
import ncd.parameter.IRange;

public abstract class NumberField<T extends Number> extends TextField {

	protected ObjectProperty<IRange<T>>	range	= new SimpleObjectProperty<>();
	protected IRange<T>					initialValue;
	protected ObjectProperty<Format>	format	= new SimpleObjectProperty<>();;

	protected abstract void keyPressed(KeyEvent event);

	public NumberField(IRange<T> range, Format format) {
		this.range.set(range);
		this.range.addListener(new ChangeListener<IRange<T>>() {

			@Override
			public void changed(ObservableValue<? extends IRange<T>> value, IRange<T> oldValue, IRange<T> newValue) {
				setText(format.toText(newValue.getValue()));
				updateToolTipText();

			}
		});
		this.format.addListener(new ChangeListener<Format>() {

			@Override
			public void changed(ObservableValue<? extends Format> value, Format oldValue, Format newValue) {
				setText(newValue.toText(range.getValue()));
				updateToolTipText();

			}
		});

		this.format.set(format);

		this.initialValue = range.copy();

		this.setAlignment(Pos.CENTER_RIGHT);

	}

	public void setTextColor(Color color) {
		int r = (int) (color.getRed() * 255);
		int g = (int) (color.getGreen() * 255);
		int b = (int) (color.getBlue() * 255);
		int o = (int) (color.getOpacity() * 255);
		String c = String.format("rgba(%d, %d, %d, %d)", r, g, b, o);
		this.setStyle("-fx-text-inner-color: " + c + ";");
	}

	protected T getNumber() {
		return this.range.getValue().parse(this.getText().trim());
	}

	protected IRange<T> getRange() {
		return this.range.getValue();
	}

	protected void setRange(IRange<T> range) {
		this.range.set(range.copy());
	}

	protected void setFormat(Format format) {
		this.format.set(format);
	}

	public Format getFormat() {
		return format.getValue();
	}

	protected void updateToolTipText() {
		if (this.range == null && this.range.getValue() == null) return;
		Format f = this.format.get();
		IRange<T> range = this.range.get();
		String ttt = "Value: " + f.toText(range.getValue()) + "\n";
		ttt = "Minimum allowed: " + f.toText(range.getLowerLimit()) + "\n";
		ttt = ttt + "Maximum allowed: " + f.toText(range.getUpperLimit()) + "\n";
		this.setTooltip(new ToolTipText(ttt));
	}

	protected ComponentEvent<IRange<T>> checkInput() {
		IRange<T> r = this.range.getValue();
		Format f = this.format.getValue();
		String text = this.getText().trim();
		if (NumberUtils.isNumber(text)) {
			T value = this.getNumber();
			if (r.contains(value)) {
				this.setTextColor(Color.BLACK);
				r.setValue(value);
				this.range.set(r);
				String message = "Value  changed to " + f.toText(value);
				NcdLogger.Info(this.getClass().getSimpleName(), "valueChanged", message);
				ComponentEvent<IRange<T>> event = new ComponentEvent<>(ComponentEvent.INPUT_VALIDATED, r, message);
				return event;

			} else {
				this.setTextColor(Color.ORANGE);
				String message = "WARNING --- Value " + value + " is outside range [" + f.toText(r.getLowerLimit()) + "," + f.toText(r.getUpperLimit()) + "]";
				NcdLogger.Warning(this.getClass().getSimpleName(), "valueChanged", message);
				ComponentEvent<IRange<T>> event = new ComponentEvent<>(ComponentEvent.WARNING, r, message);
				return event;
			}
		} else {
			this.setTextColor(Color.RED);
			String message = "ERROR --- " + text + " is not a number";
			NcdLogger.Error(this.getClass().getSimpleName(), "valueChanged", message);
			ComponentEvent<IRange<T>> event = new ComponentEvent<>(ComponentEvent.ERROR, null, message);
			return event;
		}
	}

}
