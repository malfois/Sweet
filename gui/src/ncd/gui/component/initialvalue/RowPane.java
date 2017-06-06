package ncd.gui.component.initialvalue;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import ncd.logger.NcdLogger;

public class RowPane extends HBox {

	private ObservableList<IComponent<?>> components = FXCollections.observableArrayList();

	public RowPane(ObservableList<IComponent<?>> components) {
		super(1);

		this.components = components;

		for (IComponent<?> component : this.components) {
			this.getChildren().add(component.getControl());
		}

	}

	public RowPane(final IComponent<?>... components) {
		super(5);

		ObservableList<IComponent<?>> c = FXCollections.observableArrayList();

		for (IComponent<?> component : components) {
			c.add(component);
			this.getChildren().add(component.getControl());
		}
		this.components = c;
	}

	public void setComponent(int index, IComponent<?> component) {
		this.components.set(index, component);
		this.getChildren().set(index, component.getControl());
	}

	public IComponent<?> getComponent(int index) {
		return this.components.get(index);
	}

	public void setWidths(final double... widths) {
		if (widths.length != this.components.size()) {
			String message = "Number of components and number of widths different";
			NcdLogger.Error(this.getClass().getSimpleName(), "setWidths", message);
			IOException e = new IOException(message);
			e.printStackTrace();
			return;
		}

		int n = widths.length;
		for (int i = 0; i < n; i++) {
			this.components.get(i).getControl().setPrefWidth(widths[i]);
		}
	}
}
