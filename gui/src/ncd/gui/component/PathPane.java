package ncd.gui.component;

import java.io.File;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import ncd.utils.configuration.FileConfiguration;

public class PathPane extends GridPane {

	private Label				directoryField	= new Label("");
	private Label				file			= new Label();
	// private Label comment = new Label("");

	private BooleanProperty		fileExists		= new SimpleBooleanProperty();
	private StringProperty		directory		= new SimpleStringProperty();
	private StringProperty		filename		= new SimpleStringProperty();

	private FileConfiguration	fileConfiguration;

	public PathPane(List<ColumnConstraints> columnConstraints, String label, FileConfiguration fileConfiguration) {
		getColumnConstraints().addAll(columnConstraints);
		setVgap(3);
		setPadding(new Insets(5, 0, 5, 0));

		this.fileConfiguration = fileConfiguration;
		this.directory.set(fileConfiguration.getDirectory());
		this.filename.set(fileConfiguration.getFilename());

		this.updateFileExists();
		this.updateComment();
		directoryField.setText(this.fileConfiguration.getDirectory());
		file.setText(fileConfiguration.getFilename());

		// this.updateItems();

		Button changeFileButton = new Button("Change");

		add(new Label(label), 0, 0);
		add(this.directoryField, 1, 0, 5, 1);
		add(changeFileButton, 6, 0);
		add(new Label("File name:"), 0, 1);
		add(this.file, 1, 1, 3, 1);
		// add(this.comment, 4, 1, 2, 1);

		// this.comment.setWrapText(true);

		this.directory.addListener(e -> update());

		changeFileButton.setOnAction(event -> changeDirectory());
	}

	public BooleanProperty fileExistsProperty() {
		return this.fileExists;
	}

	public Boolean fileExists() {
		return this.fileExists.getValue();
	}

	private String getPath() {
		if (this.directory.get().endsWith(File.separator)) return this.directory.get() + filename.get();
		return this.directory.get() + File.separator + filename.get();
	}

	public String getFilename() {
		return filename.get();
	}

	public String getDirectory() {
		return this.directory.get();
	}

	public FileConfiguration getFileConfiguration() {
		return fileConfiguration;
	}

	// private void updateItems() {
	// this.fileComboBox.getItems().clear();
	// List<String> filenames = this.getFilenames(new File(this.directory.get()));
	//
	// this.fileComboBox.getItems().addAll(filenames);
	// if (filenames.contains(filename.getValue())) this.fileComboBox.setValue(filename.get());
	// }

	public void update() {
		// updateItems();
		updateFileExists();
		updateComment();
		this.fileConfiguration.setDirectory(directory.get());
	}

	// private List<String> getFilenames(File f) {
	// List<String> filenames = new ArrayList<>();
	// if (f.isDirectory()) {
	// File[] files = f.listFiles();
	// for (File file : files) {
	// if (file.isFile()) {
	// String ext = this.fileConfiguration.getFilter().getSimpleExtension();
	// if (ext.length() == 0) ext = this.filename.get();
	// if (file.getAbsolutePath().endsWith(ext)) {
	// filenames.add(file.getName());
	// }
	// }
	// }
	// }
	// return filenames;
	// }

	private void changeDirectory() {
		File file = new File(this.directoryField.getText());
		if (!file.exists()) file = new File(".");
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Configuration Path");
		directoryChooser.setInitialDirectory(file);
		final File selectedDirectory = directoryChooser.showDialog(this.getScene().getWindow());
		if (selectedDirectory != null) {
			this.directoryField.setText(selectedDirectory.getAbsolutePath());
			this.directory.set(selectedDirectory.getAbsolutePath());
		}
		this.updateComment();
	}

	private void updateFileExists() {
		File file = new File(this.getPath());
		this.fileExists.set(file.exists());
	}

	private void updateComment() {
		if (new File(this.directory.getValue()).exists()) {
			this.directoryField.setStyle("-fx-text-fill: black");
			this.directoryField.setTooltip(null);
		} else {
			this.directoryField.setTooltip(new ToolTipText(this.directory.getValue() + " NOT found"));
			this.directoryField.setStyle("-fx-text-fill: red");
		}

		if (this.fileExists.getValue()) {
			// this.comment.setText("");
			this.file.setStyle("-fx-text-fill: black");
			this.file.setTooltip(null);
			return;
		}

		// this.comment.setText("File " + this.filename.get() + " not found");
		this.file.setTooltip(new ToolTipText(this.filename.getValue() + " NOT found"));
		this.file.setStyle("-fx-text-fill: red");
	}

}
