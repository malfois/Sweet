package ncd.gui.component.number;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ncd.gui.component.ToolTipText;
import ncd.gui.component.initialvalue.CLabel;
import ncd.gui.component.initialvalue.RowPane;
import ncd.gui.event.ComponentEvent;
import ncd.parameter.Format;
import ncd.parameter.IRange;

public class InputComponent<T extends Number> extends VBox {

	private CLabel		label	= new CLabel("");
	protected RowPane	field;
	private Label		message	= new Label();

	@SuppressWarnings("unchecked")
	protected InputComponent(String title, IRange<T> range, Format format, String toolTipText) {
		this.label.setText(title);
		label.setTooltip(new ToolTipText(toolTipText));

		this.field = new RowPane(label, new RangeField<T>(range, format));
		this.getChildren().addAll(this.field, this.message);

		this.message.setPrefWidth(400);
		this.message.setPrefHeight(25);
		this.field.setWidths(130, 130);

		this.message.setWrapText(true);
		this.message.setAlignment(Pos.TOP_LEFT);

		this.field.setAlignment(Pos.CENTER_LEFT);

		this.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> valueChanged((ComponentEvent<IRange<T>>) event));
		this.addEventHandler(ComponentEvent.ERROR, event -> notANumber((ComponentEvent<IRange<T>>) event));
		this.addEventHandler(ComponentEvent.WARNING, event -> outOfRange((ComponentEvent<IRange<T>>) event));
	}

	protected void setTooltipText(String text) {
		this.label.getTooltip().setText(text);
	}

	@SuppressWarnings("unchecked")
	protected IRange<T> getRange() {
		return (IRange<T>) this.field.getComponent(1).getValue();
	}

	@SuppressWarnings("unchecked")
	protected void setRange(IRange<T> range) {
		RangeField<T> nf = (RangeField<T>) this.field.getComponent(1);
		nf.setValue(range);
	}

	private void updateColor(Color color, String message) {

		int r = (int) (color.getRed() * 255);
		int g = (int) (color.getGreen() * 255);
		int b = (int) (color.getBlue() * 255);
		int o = (int) (color.getOpacity() * 255);
		String c = String.format("rgba(%d, %d, %d, %d)", r, g, b, o);
		String colour = "-fx-text-fill:" + c + ";";

		this.message.setStyle(colour);
		this.message.setText(message);
	}

	public void valueChanged(ComponentEvent<IRange<T>> ae) {
		this.clearMessage();
	}

	public void notANumber(ComponentEvent<IRange<T>> ae) {
		this.error(ae.getMessage());
	}

	protected void outOfRange(ComponentEvent<IRange<T>> ae) {
		this.warning(ae.getMessage());
	}

	@SuppressWarnings("unchecked")
	protected void setFormat(Format format) {
		RangeField<T> nf = (RangeField<T>) this.field.getComponent(1);
		nf.setFormat(format);
	}

	@SuppressWarnings("unchecked")
	protected Format getFormat() {
		RangeField<T> nf = (RangeField<T>) this.field.getComponent(1);
		return nf.getFormat();
	}

	@SuppressWarnings("unchecked")
	protected void clearMessage() {
		Color black = Color.BLACK;
		this.updateColor(black, "");
		NumberField<T> numberField = (NumberField<T>) this.field.getComponent(1);
		numberField.setTextColor(black);
	}

	@SuppressWarnings("unchecked")
	protected void warning(String message) {
		Color orange = Color.ORANGE;
		this.updateColor(orange, message);
		NumberField<T> numberField = (NumberField<T>) this.field.getComponent(1);
		numberField.setTextColor(orange);
	}

	@SuppressWarnings("unchecked")
	protected void error(String message) {
		Color red = Color.RED;
		this.updateColor(red, message);
		NumberField<T> numberField = (NumberField<T>) this.field.getComponent(1);
		numberField.setTextColor(red);
	}

}
