package ncd.scan.client;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ncd.gui.component.AlternateButton;
import ncd.gui.component.PathPane;
import ncd.gui.component.ToolTipText;
import ncd.gui.event.AlternateButtonEvent;
import ncd.scan.AConfigurationPane;
import ncd.scan.configuration.Communication;
import ncd.scan.configuration.ServerFiles;
import ncd.scan.utils.Server;

public class ServerPanel extends AConfigurationPane {

	private ServerConfig			serverConfiguration;
	private CommandConfiguration	commandConfiguration;
	private Status					status;

	public ServerPanel() {
		super(new ImageView(new Image("file:./icon/Server.png")));
		this.getMenuButton().setTooltip(new ToolTipText("Server configuration"));

		TitledPane pane = new TitledPane();
		pane.setText("Server");
		pane.setExpanded(true);
		pane.setCollapsible(false);
		pane.setAlignment(Pos.CENTER);
		pane.setFont(Font.font(pane.getFont().getFamily(), FontWeight.BOLD, pane.getFont().getSize()));

		List<ColumnConstraints> columnConstraints = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			columnConstraints.add(new ColumnConstraints(100));
		}

		this.status = new Status(columnConstraints);
		this.serverConfiguration = new ServerConfig(columnConstraints);
		this.commandConfiguration = new CommandConfiguration(columnConstraints);

		VBox box = new VBox(3);
		box.setPadding(new Insets(3));
		box.getChildren().addAll(this.status, new Separator(), this.commandConfiguration, new Separator(), this.serverConfiguration, new Separator());
		pane.setContent(box);

		this.status.setState();

		initialise(pane);
	}

	private class Status extends GridPane {

		private Label		status	= new Label();
		private ServerIcon	icon	= new ServerIcon();

		public Status(List<ColumnConstraints> columnConstraints) {
			getColumnConstraints().addAll(columnConstraints);
			setVgap(3);
			setPadding(new Insets(5, 0, 5, 0));

			this.icon.setState();

			add(new Label("State:"), 0, 0);
			add(this.status, 1, 0, 5, 1);
			add(this.icon, 6, 0);

			Server.getInstance().runningProperty().addListener(e -> Platform.runLater(() -> setState()));
			Server.getInstance().connectedProperty().addListener(e -> Platform.runLater(() -> setState()));

		}

		public void setState() {
			this.status.setText(this.icon.getMessage());
		}

	}

	private class CommandConfiguration extends GridPane {

		private PathPane		javaPathPane;
		private PathPane		serverPathPane;
		private AlternateButton	runButton	= new AlternateButton("Run", "Stop");

		public CommandConfiguration(List<ColumnConstraints> columnConstraints) {
			getColumnConstraints().addAll(columnConstraints);
			setVgap(3);
			setPadding(new Insets(5, 0, 5, 0));

			Label commandLine = new Label();
			Label command = new Label("Command");

			ServerFiles configuration = Server.getInstance().getServerFiles();
			javaPathPane = new PathPane(columnConstraints, "Java path", configuration.getJreFile());
			serverPathPane = new PathPane(columnConstraints, "Jar path", configuration.getJarFile());
			add(javaPathPane, 0, 0, 7, 2);
			add(serverPathPane, 0, 2, 7, 2);
			add(command, 0, 4);
			add(commandLine, 1, 4, 6, 1);
			add(runButton, 6, 4, 1, 1);

			runButton.setDefaultModeSelected(!Server.getInstance().isRunning());
			if (runButton.isDefaultModeSelected()) {
				runButton.setDisable(!(this.serverPathPane.fileExists() && this.javaPathPane.fileExists()));
			} else {
				runButton.setDisable(!Server.getInstance().isRunning());
			}

			runButton.addEventFilter(AlternateButtonEvent.DEFAULT, event -> Server.getInstance().start());
			runButton.addEventFilter(AlternateButtonEvent.ALTERNATE, event -> Server.getInstance().stop());

			commandLine.setWrapText(true);
			commandLine.setText(javaPathPane.getFilename() + " -jar " + serverPathPane.getFilename());

			this.javaPathPane.fileExistsProperty().addListener(e -> configuration.setJreFile(javaPathPane.getFileConfiguration()));
			this.serverPathPane.fileExistsProperty().addListener(e -> configuration.setJarFile(serverPathPane.getFileConfiguration()));
			Bindings.and(this.serverPathPane.fileExistsProperty(), this.javaPathPane.fileExistsProperty()).addListener(e -> updateButtonDisability());
			Server.getInstance().connectedProperty().addListener(e -> updateButtonDisability());
		}

		private void updateButtonDisability() {
			if (runButton.isDefaultModeSelected()) {
				runButton.setDisable(!(this.serverPathPane.fileExists() && this.javaPathPane.fileExists()));
			} else {
				runButton.setDisable(!Server.getInstance().isConnected());
			}
		}
	}

	private class ServerConfig extends GridPane {

		private PathPane		pathPane;
		private Label			machineField	= new Label("");
		private Label			portField		= new Label("");
		private AlternateButton	connectButton	= new AlternateButton("Connect", "Disconn.");

		public ServerConfig(List<ColumnConstraints> columnConstraints) {
			getColumnConstraints().addAll(columnConstraints);
			setVgap(3);
			setPadding(new Insets(5, 0, 5, 0));

			ServerFiles configuration = Server.getInstance().getServerFiles();
			pathPane = new PathPane(columnConstraints, "Directory", configuration.getServerConnection());

			this.updateServerParameter();

			if (!configuration.getServerConnection().exists()) connectButton.setDisable(true);

			add(this.pathPane, 0, 0, 7, 2);
			add(new Label("Machine:"), 0, 2);
			add(this.machineField, 1, 2, 3, 1);
			add(new Label("Port:"), 0, 3);
			add(this.portField, 1, 3, 3, 1);
			add(connectButton, 6, 3);

			connectButton.setDefaultModeSelected(!Server.getInstance().isConnected());
			if (connectButton.isDefaultModeSelected()) {
				connectButton.setDisable(!(this.pathPane.fileExists()));
			} else {
				connectButton.setDisable(!Server.getInstance().isConnected());
			}

			this.pathPane.fileExistsProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue) {
						ServerFiles configuration = Server.getInstance().getServerFiles();
						configuration.setServerConnection(pathPane.getFileConfiguration());
						updateServerParameter();
					}
				}
			});

			Server.getInstance().runningProperty().addListener(e -> Platform.runLater(() -> updateState()));
			Server.getInstance().connectedProperty().addListener(e -> Platform.runLater(() -> updateState()));

			connectButton.addEventFilter(AlternateButtonEvent.DEFAULT, event -> Server.getInstance().connect());
			connectButton.addEventFilter(AlternateButtonEvent.ALTERNATE, event -> Server.getInstance().disconnect());

		}

		private void updateState() {
			if (Server.getInstance().isRunning()) {
				if (Server.getInstance().isConnected()) {
					connectButton.setDisable(false);
					connectButton.setDefaultModeSelected(false);
				} else {
					if (Server.getInstance().getServerFiles().getServerConnection().exists()) {
						connectButton.setDisable(false);
						connectButton.setDefaultModeSelected(true);

						pathPane.update();

						Communication communication = Server.getInstance().getServerFiles().getCommunication();
						portField.setText(communication.getPortText());
						machineField.setText(communication.getHostName());
					} else {
						connectButton.setDisable(true);
						connectButton.setDefaultModeSelected(true);

						pathPane.update();
						portField.setText("N/A");
						machineField.setText("N/A");
					}
				}
			} else {
				connectButton.setDisable(true);
				connectButton.setDefaultModeSelected(true);
			}

		}

		private void updateServerParameter() {
			ServerFiles configuration = Server.getInstance().getServerFiles();
			String hostName = "N/A";
			String port = "N/A";

			if (configuration.getServerConnection().exists()) {
				Communication communication = configuration.getCommunication();
				hostName = communication.getHostName();
				port = communication.getPortText();
			}
			this.machineField.setText(hostName);
			this.portField.setText(port);
		}
	}

	@Override
	protected void save() {
		System.out.println("Saving");
		Server.getInstance().getServerConfigurationFile().write();
		System.out.println(ScanFitGUI.PREFIX + " Writing configuration in " + Server.getInstance().getServerConfigurationFile().getXmlFile().getCanonicalPath());
	}

}
