package ncd.scan.client;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Shape;
import ncd.gui.component.ToolTipText;
import ncd.gui.icon.ShapeUtility;
import ncd.scan.utils.Server;

public class ServerIcon extends Label {

	private String message;

	public ServerIcon() {
		super("", ShapeUtility.get(ShapeUtility.RED_BALL));

		Server.getInstance().connectedProperty().addListener(e -> Platform.runLater(() -> setState()));

		Server.getInstance().runningProperty().addListener(e -> Platform.runLater(() -> setState()));

	}

	public String getMessage() {
		return this.message;
	}

	public void setState() {
		Boolean running = Server.getInstance().isRunning();
		Boolean connected = Server.getInstance().isConnected();

		Shape image = ShapeUtility.get(ShapeUtility.RED_BALL);
		this.message = "Server NOT running";
		ToolTipText toolTip = new ToolTipText("Server NOT running");

		if (running && !connected) {
			image = ShapeUtility.get(ShapeUtility.ORANGE_BALL);
			this.message = "Server running and client NOT connected";
		}
		if (running && connected) {
			image = ShapeUtility.get(ShapeUtility.GREEN_BALL);
			this.message = "Server running and client connected";
		}

		toolTip.setText(this.message);
		setGraphic(image);
		Tooltip.install(this, toolTip);
	}

}
