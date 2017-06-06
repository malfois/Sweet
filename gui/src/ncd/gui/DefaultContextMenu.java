package ncd.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import ncd.gui.event.ContextMenuEvent;

public class DefaultContextMenu extends ContextMenu {

	public final static String	CONFIGURATION	= "Configuration";
	public final static String	SAVE			= "Save";

	private MenuItem			configuration	= new MenuItem(CONFIGURATION);
	private MenuItem			save			= new MenuItem(SAVE);

	public DefaultContextMenu() {
		this.getItems().addAll(this.configuration, this.save);

		this.configuration.setOnAction(this::rendererSelected);
		this.save.setOnAction(this::saveData);
	}

	private void saveData(ActionEvent event) {
		event.consume();
		ContextMenuEvent<MenuItem> e = new ContextMenuEvent<>(ContextMenuEvent.SAVE_DATA, this.save);
		this.fireEvent(e);
	}

	private void rendererSelected(ActionEvent event) {
		event.consume();
		ContextMenuEvent<MenuItem> e = new ContextMenuEvent<>(ContextMenuEvent.RENDERER_ACTION, this.configuration);
		this.fireEvent(e);
	}
}
