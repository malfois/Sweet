package ncd.utils.configuration;

import java.io.File;
import java.util.Optional;

import javax.xml.bind.annotation.XmlType;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;

@XmlType(propOrder = { "name", "directory", "filename" })
public class ScanFileConfiguration {

	private static final String DEFAULT_DIRECTORY = System.getProperty("user.home");

	private String name;
	private String directory;
	private String filename;

	public ScanFileConfiguration() {
	}

	public ScanFileConfiguration(String name, String filename) {
		this.filename = filename;
		this.name = name;
	}

	public void defaultConfiguration() {
		this.directory = DEFAULT_DIRECTORY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "ScanFileConfiguration [name=" + name + ", directory=" + directory + ", filename=" + filename + "]";
	}

	public String getPath() {
		if (this.directory.endsWith(File.separator)) {
			return this.directory + this.filename;
		}
		return this.directory + File.separator + this.filename;
	}

	private void selectDirectory() {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			this.directory = selectedDirectory.getAbsolutePath();
		}
	}

	protected void alert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Directory selection");
		alert.setHeaderText("Directory are not defined for " + this.name + "\n\nDefault directory is " + DEFAULT_DIRECTORY);
		alert.setContentText("Do you want to change it?");
		ButtonType buttonTypeChange = new ButtonType("Change");
		ButtonType buttonTypeAccept = new ButtonType("Accept");

		alert.getButtonTypes().setAll(buttonTypeChange, buttonTypeAccept);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeChange) {
			this.selectDirectory();
		} else if (result.get() == buttonTypeAccept) {
			this.directory = DEFAULT_DIRECTORY;
		}
	}

}
