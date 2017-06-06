package ncd.gui.component.number;

import java.util.Optional;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import ncd.gui.component.ToolTipText;
import ncd.gui.component.initialvalue.CSeparator;
import ncd.gui.component.initialvalue.IComponent;
import ncd.gui.event.ComponentEvent;
import ncd.logger.NcdLogger;
import ncd.parameter.Format;
import ncd.parameter.IParameter;
import ncd.parameter.IRange;

public class ParameterField<T extends Number> extends NumberField<T> implements IComponent<IParameter<T>> {

	protected ObjectProperty<IParameter<T>>	parameter	= new SimpleObjectProperty<>();
	protected IParameter<T>					initialValue;

	public ParameterField(IParameter<T> parameter) {
		super(parameter.getValueRange(), parameter.getFormat());
		this.parameter.set(parameter);
		this.initialValue = parameter.copy();

		this.setAlignment(Pos.CENTER_RIGHT);

		// this.setPrefHeight(25);

		ContextMenu contextMenu = new ContextMenu();
		MenuItem item1 = new MenuItem("Preferences");
		contextMenu.getItems().add(item1);
		this.setContextMenu(contextMenu);

		item1.addEventHandler(ActionEvent.ACTION, new PreferencesHandler(this));
		this.addEventHandler(KeyEvent.KEY_PRESSED, event -> keyPressed(event));
	}

	@Override
	protected void keyPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			ComponentEvent<IRange<T>> evt = this.checkInput();
			ComponentEvent<IParameter<T>> e = new ComponentEvent<>(ComponentEvent.ERROR, null, evt.getMessage());

			if (evt.getEventType().equals(ComponentEvent.WARNING)) {
				e = new ComponentEvent<>(ComponentEvent.WARNING, this.parameter.getValue(), evt.getMessage());
			}
			if (evt.getEventType().equals(ComponentEvent.INPUT_VALIDATED)) {
				this.parameter.get().setValue(evt.getResult().getValue());
				e = new ComponentEvent<>(ComponentEvent.INPUT_VALIDATED, this.parameter.getValue(), evt.getMessage());
			}
			this.fireEvent(e);
		}
	}

	protected void updateToolTipText() {
		super.updateToolTipText();
		ToolTipText toolTip = (ToolTipText) this.getTooltip();
		String ttt = toolTip.getText();
		ttt = ttt + "\nRight click on the mouse for changing the preferences";
		toolTip.setText(ttt);
	}

	@Override
	public IParameter<T> getInitialValue() {
		return this.initialValue;
	}

	@Override
	public String actionToLogger() {
		return "New value: " + this.getValue();
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public void setValue(IParameter<T> value) {
		this.parameter.set(value.copy());
		this.setRange(this.parameter.get().getValueRange());
	}

	@Override
	public IParameter<T> getValue() {
		if (this.parameter != null) {
			return this.parameter.get().copy();
		}
		return null;
	}

	@Override
	public void reset() {
		this.setValue(this.initialValue);
	}

	private class PreferencesHandler implements EventHandler<ActionEvent> {

		private ParameterField<T> field;

		public PreferencesHandler(ParameterField<T> field) {
			this.field = field;
		}

		@Override
		public void handle(ActionEvent arg0) {
			Window window = this.field.getScene().getWindow();
			String title = "Parameter preference";
			ParameterDialog dialog = new ParameterDialog(title, this.field.getValue());
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(window);
			Optional<IParameter<T>> result = dialog.showAndWait();
			if (result.isPresent()) {
				IParameter<T> iNumber = result.get();
				boolean theSame = iNumber.equals(this.field.getValue());
				this.field.setValue(iNumber);
				if (!theSame) {
					ComponentEvent<IParameter<T>> event = new ComponentEvent<IParameter<T>>(ComponentEvent.INPUT_VALIDATED, iNumber);
					this.field.fireEvent(event);
				}
			}
		}
	}

	private class ParameterDialog extends Dialog<IParameter<T>> {

		private IParameter<T>		parameter;
		private Format				format;

		private InputComponent<T>	value;
		private InputComponent<T>	minimum;
		private InputComponent<T>	maximum;
		private InputComponent<T>	precision;
		private InputComponent<T>	scientificNotation;

		@SuppressWarnings("unchecked")
		public ParameterDialog(String title, IParameter<T> parameter) {
			this.parameter = parameter.copy();
			this.format = this.parameter.getFormat();

			setTitle(title);

			this.value = new InputComponent<>("Value", this.parameter.getValueRange(), this.format.copy(), "Value of the parameter");
			this.minimum = new InputComponent<>("Minimum", this.parameter.getMinimumRange(), this.format.copy(), "Minimum for the parameter");
			this.maximum = new InputComponent<>("Maximum", this.parameter.getMaximumRange(), this.format.copy(), "Maximum for the parameter");
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

			this.value.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> valueChanged((ComponentEvent<IRange<T>>) event));
			this.value.addEventHandler(ComponentEvent.WARNING, event -> valueWarning());
			this.value.addEventHandler(ComponentEvent.ERROR, event -> valueError());

			this.minimum.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> minimumChanged((ComponentEvent<IRange<T>>) event));
			this.minimum.addEventHandler(ComponentEvent.WARNING, event -> setButtonDisable(true));
			this.minimum.addEventHandler(ComponentEvent.ERROR, event -> setButtonDisable(true));

			this.maximum.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> maximumChanged((ComponentEvent<IRange<T>>) event));
			this.maximum.addEventHandler(ComponentEvent.WARNING, event -> setButtonDisable(true));
			this.maximum.addEventHandler(ComponentEvent.ERROR, event -> setButtonDisable(true));

			this.precision.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> precisionChanged((ComponentEvent<IRange<T>>) event));
			this.precision.addEventHandler(ComponentEvent.WARNING, event -> setButtonDisable(true));
			this.precision.addEventHandler(ComponentEvent.ERROR, event -> setButtonDisable(true));

			this.scientificNotation.addEventHandler(ComponentEvent.INPUT_VALIDATED, event -> scientificNotationChanged((ComponentEvent<IRange<T>>) event));
			this.scientificNotation.addEventHandler(ComponentEvent.WARNING, event -> setButtonDisable(true));
			this.scientificNotation.addEventHandler(ComponentEvent.ERROR, event -> setButtonDisable(true));

			setResultConverter(dialogButton -> {
				if (dialogButton == ButtonType.APPLY) {
					this.parameter.setValue(this.value.getRange().getValue());
					this.parameter.setMinimumRange(this.minimum.getRange());
					this.parameter.setMaximumRange(this.maximum.getRange());
					this.parameter.getFormat().setFormat((int) this.precision.getRange().getValue(), (int) this.scientificNotation.getRange().getValue());
					return this.parameter;
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

		private void maximumChanged(ComponentEvent<IRange<T>> event) {
			IRange<T> result = event.getResult();
			IRange<T> min = this.minimum.getRange();
			IParameter<T> param = this.parameter.copy();
			if (param.setLimits(min, result)) {
				if (!param.setValue(this.value.getRange().getValue())) {
					this.setButtonDisable(true);
					String message = "W --- Maximum [" + this.format.toText(result.getValue()) + "] greater than value [" + this.format.toText(this.value.getRange().getValue())
							+ "]";
					this.maximum.warning(message);
					this.value.warning(message);
				} else {
					this.setButtonDisable(false);
					IRange<T> range = this.value.getRange().copy();
					range.setMaximum(result.getValue());
					this.value.setRange(range);
					this.value.clearMessage();
					String message = "Maximum changed to " + this.format.toText(result.getValue());
					NcdLogger.Info(this.getClass().getName(), "maximumChanged", message);

				}
			} else {
				this.setButtonDisable(true);
				this.maximum
						.error("E --- Maximum [" + this.format.toText(result.getValue()) + "] less than minimum [" + this.format.toText(this.minimum.getRange().getValue()) + "]");
			}
		}

		private void minimumChanged(ComponentEvent<IRange<T>> event) {
			IRange<T> result = event.getResult();
			IRange<T> max = this.maximum.getRange();
			IParameter<T> param = this.parameter.copy();
			if (param.setLimits(result, max)) {
				if (!param.setValue(this.value.getRange().getValue())) {
					this.setButtonDisable(true);
					String message = "W --- Minimum [" + this.format.toText(result.getValue()) + "] greater than value [" + this.format.toText(this.value.getRange().getValue())
							+ "]";
					this.minimum.warning(message);
					this.value.warning(message);
				} else {
					this.setButtonDisable(false);
					IRange<T> range = this.value.getRange().copy();
					range.setMinimum(result.getValue());
					this.value.setRange(range);
					this.value.clearMessage();
					String message = "Minimum changed to " + this.format.toText(result.getValue());
					NcdLogger.Info(this.getClass().getName(), "minimumChanged", message);
				}
			} else {
				this.setButtonDisable(true);
				this.minimum.error(
						"ERROR --- Minimum [" + this.format.toText(result.getValue()) + "] greater than maximum [" + this.format.toText(this.maximum.getRange().getValue()) + "]");
			}
		}

		private void valueError() {
			this.setButtonDisable(true);
			this.setAllOtherDisabled(this.value, true);
		}

		private void valueWarning() {
			this.setButtonDisable(true);
		}

		private void valueChanged(ComponentEvent<IRange<T>> event) {
			this.setAllOtherDisabled(this.value, false);
			this.setButtonDisable(false);
			this.clearAllMessage();
		}

		@SuppressWarnings("unchecked")
		private void setAllOtherDisabled(InputComponent<T> component, boolean disable) {
			VBox box = (VBox) getDialogPane().getContent();
			ObservableList<Node> nodes = box.getChildren();
			for (Node node : nodes) {
				if (node instanceof InputComponent) {
					InputComponent<T> c = (InputComponent<T>) node;
					if (!c.equals(component)) {
						c.setDisable(disable);
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		private void clearAllMessage() {
			VBox box = (VBox) getDialogPane().getContent();
			ObservableList<Node> nodes = box.getChildren();
			for (Node node : nodes) {
				if (node instanceof InputComponent) {
					InputComponent<T> c = (InputComponent<T>) node;
					c.clearMessage();
				}
			}
		}

		private void setButtonDisable(boolean disable) {
			Button applyButton = (Button) getDialogPane().lookupButton(ButtonType.APPLY);
			applyButton.setDisable(disable);
		}

	}

}
