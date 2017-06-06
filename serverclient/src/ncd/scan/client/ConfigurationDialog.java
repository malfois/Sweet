package ncd.scan.client;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;

public class ConfigurationDialog extends Dialog<Void> {

	VBox				box		= new VBox();

	private ToolBar		toolBar	= new ToolBar();

	private ServerPanel	server	= new ServerPanel();
	private SpockPanel	spock	= new SpockPanel();

	public ConfigurationDialog(String title) {
		ToggleGroup group = new ToggleGroup();
		this.server.getMenuButton().setToggleGroup(group);
		this.spock.getMenuButton().setToggleGroup(group);
		this.server.getMenuButton().setSelected(true);

		this.toolBar.getItems().addAll(this.server.getMenuButton(), this.spock.getMenuButton());

		setTitle(title);

		box.getChildren().addAll(this.toolBar, this.server);

		getDialogPane().setContent(box);

		ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().add(buttonTypeOk);

		group.selectedToggleProperty().addListener((observableValue, oldvalue, newValue) -> showPane(newValue));
		// server.getMenuButton().setOnAction(event -> serverConfiguration());
		// spock.getMenuButton().setOnAction(event -> spockConfiguration());

	}

	private void showPane(Toggle button) {
		VBox box = getSelectedPane(button);
		int nChildren = this.box.getChildren().size();
		if (nChildren > 1) {
			this.box.getChildren().set(1, box);
		} else {
			this.box.getChildren().add(1, box);
		}
	}

	private VBox getSelectedPane(Toggle button) {
		VBox vbox = this.server;
		if (button.equals(this.spock.getMenuButton())) vbox = this.spock;
		return vbox;
	}

}
