package ncd.scan;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class AConfigurationPane extends VBox {

	private ToggleButton	menuButton	= new ToggleButton();
	private Button			save		= new Button("Save");

	protected abstract void save();

	protected AConfigurationPane(ImageView image) {
		this.menuButton.setGraphic(image);
		this.save.setOnAction(event -> save());
	}

	protected AConfigurationPane(Pane pane) {
		this.menuButton.setGraphic(pane);
		this.save.setOnAction(event -> save());
	}

	public ToggleButton getMenuButton() {
		return menuButton;
	}

	protected void initialise(Control pane) {
		this.getChildren().add(pane);
		this.setStyle("-fx-text-fill: -fx-text-base-color;");

		HBox hBox = new HBox(3);
		hBox.setAlignment(Pos.CENTER);
		hBox.setPadding(new Insets(5, 0, 5, 0));
		hBox.getChildren().add(save);
		this.getChildren().add(hBox);
	}
}
