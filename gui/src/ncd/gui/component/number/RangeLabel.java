package ncd.gui.component.number;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Window;
import ncd.gui.component.ToolTipText;
import ncd.gui.component.initialvalue.IComponent;
import ncd.gui.event.ComponentEvent;
import ncd.parameter.Format;
import ncd.parameter.IRange;

public class RangeLabel<T extends Number> extends NumberLabel<T> implements IComponent<T> {

	private T	minimum;
	private T	maximum;

	public RangeLabel(IRange<T> range, Format format) {
		super(range.getValue(), format);

		this.minimum = range.getLowerLimit();
		this.maximum = range.getUpperLimit();

		this.updateToolTipText();

		ContextMenu contextMenu = new ContextMenu();
		MenuItem item = new MenuItem("Preferences");
		contextMenu.getItems().add(item);
		this.setContextMenu(contextMenu);

		item.addEventHandler(ActionEvent.ACTION, new PreferencesHandler(this));

	}

	public void updateToolTipText() {
		Format f = this.format;
		String ttt = "Value fixed to " + f.toText(this.number) + "\n";
		ttt = "Minimum allowed: " + f.toText(this.minimum) + "\n";
		ttt = ttt + "Maximum allowed: " + f.toText(this.maximum) + "\n";
		this.setTooltip(new ToolTipText(ttt));
	}

	public T getMinimum() {
		return minimum;
	}

	public T getMaximum() {
		return maximum;
	}

	public void setRange(IRange<T> range) {
		this.setValue(range.getValue());
		this.minimum = range.getLowerLimit();
		this.maximum = range.getUpperLimit();
		this.updateToolTipText();
	}

	private class PreferencesHandler implements EventHandler<ActionEvent> {

		private RangeLabel<T> field;

		public PreferencesHandler(RangeLabel<T> field) {
			this.field = field;
		}

		@Override
		public void handle(ActionEvent arg0) {
			Window window = this.field.getScene().getWindow();
			String title = "Parameter preference";
			FormatDialog<T> dialog = new FormatDialog<T>(title, this.field.getValue(), this.field.getMinimum(), this.field.getMaximum(), this.field.getFormat());
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(window);
			Optional<Format> result = dialog.showAndWait();
			if (result.isPresent()) {
				Format format = result.get();
				this.field.setFormat(format);
				ComponentEvent<Format> event = new ComponentEvent<>(ComponentEvent.INPUT_VALIDATED, format);
				this.field.fireEvent(event);
			}
		}
	}

}
