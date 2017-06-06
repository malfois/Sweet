package ncd.gui.component.number;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import ncd.gui.component.initialvalue.CSeparator;
import ncd.gui.event.ComponentEvent;
import ncd.logger.NcdLogger;
import ncd.parameter.Format;
import ncd.parameter.IRange;

public class FormatDialog<T extends Number> extends Dialog<Format> {
	private Format				format;

	private OutputComponent<T>	value;
	private OutputComponent<T>	minimum;
	private OutputComponent<T>	maximum;
	private InputComponent<T>	precision;
	private InputComponent<T>	scientificNotation;

	@SuppressWarnings("unchecked")
	public FormatDialog(String title, T value, T min, T max, Format format) {
		this.format = format;

		setTitle(title);

		this.value = new OutputComponent<>("Value", value, this.format.copy(), "Value of the parameter");
		this.minimum = new OutputComponent<>("Minimum", min, this.format.copy(), "Minimum for the parameter");
		this.maximum = new OutputComponent<>("Maximum", max, this.format.copy(), "Maximum for the parameter");
		this.precision = new InputComponent<>("Precision", (IRange<T>) this.format.getPrecisionRange(), this.format.getFormat().copy(), this.format.helpPrecision());
		this.scientificNotation = new InputComponent<>("Scientific notation", (IRange<T>) this.format.getScientificNotationRange(), this.format.getFormat().copy(),
				this.format.helpScientificNotation());

		CSeparator sep1 = new CSeparator("Limits");
		sep1.setWidths(20.0, 50.0, 330.0);
		CSeparator sep2 = new CSeparator("Format");
		sep2.setWidths(20.0, 50.0, 330.0);
		VBox vBox = new VBox();
		vBox.getChildren().addAll(this.value, sep1, this.minimum, this.maximum, sep2, this.precision, this.scientificNotation);

		getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
		Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
		applyButton.setDefaultButton(false);

		getDialogPane().setContent(vBox);

		this.precision.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> precisionChanged((ComponentEvent<IRange<T>>) event));
		this.precision.addEventHandler(ComponentEvent.WARNING, event -> setButtonDisable(true));
		this.precision.addEventHandler(ComponentEvent.ERROR, event -> setButtonDisable(true));

		this.scientificNotation.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> scientificNotationChanged((ComponentEvent<IRange<T>>) event));
		this.scientificNotation.addEventHandler(ComponentEvent.WARNING, event -> setButtonDisable(true));
		this.scientificNotation.addEventHandler(ComponentEvent.ERROR, event -> setButtonDisable(true));

		setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.APPLY) {
				this.format.setPrecision((int) this.precision.getRange().getValue());
				this.format.setScientificNotation((int) this.scientificNotation.getRange().getValue());
				return this.format;
			}
			return null;
		});

	}

	private void scientificNotationChanged(ComponentEvent<IRange<T>> event) {
		Integer scientificNotation = (Integer) event.getResult().getValue();
		this.format.setScientificNotation(scientificNotation);
		this.value.setFormat(this.format.copy());
		this.minimum.setFormat(this.format.copy());
		this.maximum.setFormat(this.format.copy());
		this.setButtonDisable(false);
		this.scientificNotation.setTooltipText(this.format.helpScientificNotation());
		String message = "scientific Notation changed to " + this.format.toText(scientificNotation);
		NcdLogger.Info(this.getClass().getName(), "scientificNotationChanged", message);
	}

	private void precisionChanged(ComponentEvent<IRange<T>> event) {
		Integer precision = (Integer) event.getResult().getValue();
		this.format.setPrecision(precision);
		this.value.setFormat(this.format.copy());
		this.minimum.setFormat(this.format.copy());
		this.maximum.setFormat(this.format.copy());
		this.setButtonDisable(false);
		String message = "Precision changed to " + this.format.toText(precision);
		NcdLogger.Info(this.getClass().getName(), "precisionChanged", message);
	}

	private void setButtonDisable(boolean disable) {
		Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
		applyButton.setDisable(disable);
	}

}
