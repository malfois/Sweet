package ncd.gui.component;

import java.util.Hashtable;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import ncd.gui.event.AlternateButtonEvent;

public class AlternateButton extends Button {

	private Map<Boolean, String>	hashTable			= new Hashtable<>();

	private BooleanProperty			defaultModeSelected	= new SimpleBooleanProperty(true);

	public AlternateButton(String defaultText, String alternateText) {
		hashTable.put(true, defaultText);
		hashTable.put(false, alternateText);
		setText(defaultText);

		this.addEventFilter(ActionEvent.ACTION, event -> changeText());

		this.defaultModeSelected.addListener((observable, oldValue, newValue) -> setText(hashTable.get(newValue)));

	}

	public void setDefaultModeSelected(Boolean selected) {
		this.defaultModeSelected.set(selected);
	}

	public Boolean isDefaultModeSelected() {
		return this.defaultModeSelected.getValue();
	}

	private void changeText() {
		this.defaultModeSelected.set(!this.defaultModeSelected.get());
		if (this.defaultModeSelected.get()) {
			this.fireEvent(new AlternateButtonEvent(AlternateButtonEvent.ALTERNATE));
		} else {
			this.fireEvent(new AlternateButtonEvent(AlternateButtonEvent.DEFAULT));
		}
	}
}
