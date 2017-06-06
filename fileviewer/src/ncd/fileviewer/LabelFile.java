package ncd.fileviewer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import ncd.utils.maths.Maths;

public class LabelFile extends Label {

	private File			file;
	private BooleanProperty	selected	= new SimpleBooleanProperty(false);
	private BooleanProperty	focused		= new SimpleBooleanProperty(false);
	private IntegerProperty	state		= new SimpleIntegerProperty();

	public LabelFile(File file) {
		super(file.getName());
		this.file = file;

		int index = file.getName().indexOf(".lnk");
		if (index > 0) {
			this.setText(file.getName().substring(0, index));
		}

		this.setOnMouseEntered(event -> this.focused.set(true));
		this.setOnMouseExited(event -> this.focused.set(false));
		this.setOnMouseClicked(event -> this.clickPerformed(event));

		this.updateState();
		this.state.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String style = "-fx-background-color:  -fx-background ;";
				if (newValue.intValue() == 1) {
					style = "-fx-background-color: lightgrey;";
				}
				if (newValue.intValue() == 2) {
					style = "-fx-background-color: -fx-accent ;";
				}
				setStyle(style);
			}
		});

		this.selected.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateState();
			}
		});

		this.focused.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				updateState();
			}
		});

	}

	public void setSelected(Boolean selected) {
		this.selected.set(selected);
	}

	private void clickPerformed(MouseEvent event) {
		this.selected.set(true);
		if (event.getClickCount() == 2) {
			if (this.file.isDirectory()) {
				FileEvent<File> e = new FileEvent<>(this.file);
				this.fireEvent(e);
			} else {
				new Thread(() -> {
					try {
						Desktop.getDesktop().open(this.file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}).start();

			}
		}
		FileEvent<LabelFile> e = new FileEvent<LabelFile>(FileEvent.CLEAR_SELECTION, this);
		this.fireEvent(e);

	}

	private void updateState() {
		int i = this.getFocusInteger() + this.getSelectionInteger() - 1;
		i = (i < 0) ? 0 : i;
		this.state.set(i);
	}

	private int getFocusInteger() {
		return Maths.getInteger(this.focused.getValue());
	}

	private int getSelectionInteger() {
		int val = 2 * Maths.getInteger(this.selected.getValue());
		return val;
	}

}
